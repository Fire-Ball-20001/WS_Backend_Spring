package org.backend.spring.services.files;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.backend.spring.actions.BinaryAction;
import org.backend.spring.actions.filters.Filter;
import org.backend.spring.dto.FullPostDto;
import org.backend.spring.events.BinaryEvent;
import org.backend.spring.exceptions.NotFoundException;
import org.backend.spring.mappers.MapperBase;
import org.backend.spring.mappers.PostMapper;
import org.backend.spring.models.PostEmployee;
import org.backend.spring.services.DataStorage;
import org.backend.spring.services.utils.PostUtils;
import org.springframework.beans.factory.annotation.Qualifier;
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

import static org.backend.spring.services.utils.Files.deleteFileOrDirectory;

@ConditionalOnProperty(prefix = "data",name = "source",havingValue = "files",matchIfMissing = true)
@CommonsLog
@Component
@RequiredArgsConstructor
public class FilePostStorage implements DataStorage<PostEmployee> {

    private Map<UUID,PostEmployee> objects = new HashMap<>();
    @Value("${data.path_posts}")
    private String path_str;
    private final ObjectMapper objectMapper;
    private final PostMapper postMapper;
    private final BinaryEvent<PostEmployee,PostEmployee> event;
    @PostConstruct
    private void postConstruct()
    {
        try{
            loadData();
        }
        catch (RuntimeException e)
        {
            log.error("Error load post data");
            objects = Arrays.stream(new PostEmployee[] {
                    new PostEmployee("Младший", UUID.randomUUID()),
                    new PostEmployee("Средний", UUID.randomUUID()),
                    new PostEmployee("Старший", UUID.randomUUID())
            }).collect(Collectors.toMap(PostEmployee::getId, Function.identity()));
        }
    }

    @Override
    public PostEmployee getObject(Filter<PostEmployee> filter) {
        Optional<PostEmployee> tempPostEmployee = objects.values().stream()
                .filter(filter::match)
                .findFirst();
        if(!tempPostEmployee.isPresent())
        {
            if(filter.match(PostUtils.getDefaultPost()))
            {
                return PostUtils.getDefaultPost();
            }
            throw new NotFoundException("Not found post object.");
        }
        return tempPostEmployee.get();
    }

    @Override
    public void setObject(PostEmployee object) {
        if(!objects.containsKey(object.getId()))
        {
            throw new NotFoundException("Not found post object.");
        }
        event.call(objects.get(object.getId()),object);
        objects.replace(object.getId(),object);
        saveData();
    }

    @Override
    public PostEmployee[] getObjects(Filter<PostEmployee> filter) {
        return objects.values().stream().filter(filter::match).toArray(PostEmployee[]::new);
    }

    @Override
    public void addObject(PostEmployee object) {
        objects.put(object.getId(),object);
        saveData();
    }

    @Override
    public boolean removeObject(Filter<PostEmployee> filter) {
        if(objects.values().stream().noneMatch(filter::matchStrictly))
        {
            throw new NotFoundException("Not found post object.");
        }
        for (PostEmployee post:
             objects.values().toArray(new PostEmployee[0])) {
            if(filter.matchStrictly(post) && !filter.matchStrictly(PostUtils.getDefaultPost()))
            {
                event.call(post,PostUtils.getDefaultPost());
                objects.remove(post.getId());
            }
        }
        saveData();
        return true;
    }

    private void loadData()
    {
        String posts_string;
        FullPostDto[] posts;
        try(BufferedReader fileReader = new BufferedReader(new FileReader(path_str))) {
            posts_string = fileReader.lines().collect(Collectors.joining("\n"));
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error loading data");
        }

        try {
            posts = objectMapper.readValue(posts_string, FullPostDto[].class);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error loading data");
        }
        objects = Arrays.stream(postMapper.toEntity(posts))
                .collect(
                        Collectors.toMap(PostEmployee::getId,Function.identity()));
    }

    private void saveData()
    {
        String posts_string;
        FullPostDto[] posts;
        if(!deleteFileOrDirectory(new File(path_str)))
        {
            throw new RuntimeException("Error save posts data");
        }
        if(objects.size() == 0)
        {
            return;
        }
        posts = postMapper.toDto(objects.values().toArray(new PostEmployee[0]));
        try(FileWriter fw = new FileWriter(path_str))
        {
            fw.write(objectMapper.writeValueAsString(posts));
            fw.flush();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error save posts data ",e);
        }
    }


}
