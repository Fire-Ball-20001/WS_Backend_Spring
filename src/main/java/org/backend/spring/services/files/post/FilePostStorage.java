package org.backend.spring.services.files.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.backend.spring.actions.filters.Filter;
import org.backend.spring.controllers.dto.post.PostDto;
import org.backend.spring.events.BinaryEvent;
import org.backend.spring.exceptions.NotFoundException;
import org.backend.spring.controllers.mappers.PostMapper;
import org.backend.spring.models.PostEmployee;
import org.backend.spring.services.DataStorage;
import org.backend.spring.utils.PostUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.backend.spring.utils.Files.deleteFileOrDirectory;

@ConditionalOnProperty(prefix = "data", name = "source", havingValue = "files", matchIfMissing = true)
@CommonsLog
@Component
@RequiredArgsConstructor
public class FilePostStorage implements DataStorage<PostEmployee> {

    private Map<UUID, PostEmployee> posts = new HashMap<>();
    @Value("${data.path-to-posts}")
    private final String pathStr;
    private final ObjectMapper objectMapper;
    private final PostMapper postMapper;
    private final BinaryEvent<PostEmployee, PostEmployee> event;

    @PostConstruct
    private void postConstruct() {
        try {
            loadData();
        } catch (RuntimeException e) {
            log.error("Error load post data");
            posts = Arrays.stream(new PostEmployee[]{
                    new PostEmployee("Младший", UUID.randomUUID()),
                    new PostEmployee("Средний", UUID.randomUUID()),
                    new PostEmployee("Старший", UUID.randomUUID())
            }).collect(Collectors.toMap(PostEmployee::getId, Function.identity()));
        }
    }

    @Override
    public PostEmployee get(Filter<PostEmployee> filter) {
        Optional<PostEmployee> tempPostEmployee = posts.values().stream()
                .filter(filter::match)
                .findFirst();
        if (!tempPostEmployee.isPresent()) {
            if (filter.match(PostUtils.getDefaultPost())) {
                return PostUtils.getDefaultPost();
            }
            throw new NotFoundException("Not found post object.");
        }
        return tempPostEmployee.get();
    }

    @Override
    public Optional<PostEmployee> getOptional(Filter<PostEmployee> filter) {
        return posts.values().stream()
                .filter(filter::match)
                .findFirst();
    }

    @Override
    public void set(PostEmployee argument) {
        if (!posts.containsKey(argument.getId())) {
            throw new NotFoundException("Not found post object.");
        }
        event.call(posts.get(argument.getId()), argument);
        posts.replace(argument.getId(), argument);
        saveData();
    }

    public PostEmployee[] getArray(Filter<PostEmployee> filter) {
        return posts.values().stream().filter(filter::match).toArray(PostEmployee[]::new);
    }

    @Override
    public void add(PostEmployee argument) {
        posts.put(argument.getId(), argument);
        saveData();
    }

    @Override
    public void remove(Filter<PostEmployee> filter) {
        if (posts.values().stream().noneMatch(filter::match)) {
            throw new NotFoundException("Not found post object.");
        }
        for (PostEmployee post :
                posts.values().toArray(new PostEmployee[0])) {
            if (filter.match(post) && !filter.match(PostUtils.getDefaultPost())) {
                event.call(post, PostUtils.getDefaultPost());
                posts.remove(post.getId());
            }
        }
        saveData();
    }


    private void loadData() {
        String posts_string;
        PostDto[] posts;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(pathStr))) {
            posts_string = fileReader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Error loading data");
        }

        try {
            posts = objectMapper.readValue(posts_string, PostDto[].class);
        } catch (Exception e) {
            throw new RuntimeException("Error loading data");
        }
        this.posts = Arrays.stream(postMapper.toEntity(posts))
                .collect(
                        Collectors.toMap(PostEmployee::getId, Function.identity()));
    }

    private void saveData() {
        String posts_string;
        PostDto[] posts;
        if (!deleteFileOrDirectory(new File(pathStr))) {
            throw new RuntimeException("Error save posts data");
        }
        if (this.posts.size() == 0) {
            return;
        }
        posts = postMapper.toDto(this.posts.values().toArray(new PostEmployee[0]));
        try (FileWriter fw = new FileWriter(pathStr)) {
            fw.write(objectMapper.writeValueAsString(posts));
            fw.flush();
        } catch (Exception e) {
            throw new RuntimeException("Error save posts data ", e);
        }
    }


}
