package org.backend.spring.controllers.postController;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.backend.spring.actions.filters.Filter;
import org.backend.spring.controllers.dto.filter.EmployeeFilterDto;
import org.backend.spring.controllers.dto.filter.PostFilterDto;
import org.backend.spring.controllers.dto.post.PostDto;
import org.backend.spring.controllers.dto.post.PostNoIdDto;
import org.backend.spring.controllers.mappers.PostMapper;
import org.backend.spring.models.PostEmployee;
import org.backend.spring.services.DataStorage;
import org.backend.spring.utils.FilterUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
@AllArgsConstructor
@RequestMapping("/post")
public class PostRestController {

    DataStorage<PostEmployee> storage;
    PostMapper postMapper;
    ObjectMapper objectMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get one post", tags = "Post")
    public PostDto getPost(@PathVariable UUID id) {
        Filter<PostEmployee> postFilter = new Filter<>();
        postFilter.addOperation((post) -> FilterUtils.isStrictlyMatch(id.toString(),post.toString()));
        return postMapper.toDto(storage.get(postFilter));
    }

    @GetMapping("/list")
    @Operation(summary = "Get more posts with filter", tags = "Post")
    public PostDto[] getPosts(PostFilterDto filter) {
        Filter<PostEmployee> postFilter = FilterUtils.parsePostFilter(filter);
        return postMapper.toDto(storage.getArray(postFilter));
    }

    @PostMapping("/{id}/set")
    @Operation(summary = "Replace post", tags = "Post")
    public PostDto setPost(@PathVariable UUID id, @RequestBody PostNoIdDto postDto) {
        PostEmployee postEmployee = postMapper.toEntity(postDto,id);
        storage.set(postEmployee);
        return postMapper.toDto(postEmployee);
    }

    @PostMapping("/add")
    @ResponseBody
    @Operation(summary = "Add new post", tags = "Post")
    public PostDto addPost(@RequestBody PostNoIdDto postNoIdDto) {
        PostEmployee postEmployee = postMapper.toEntity(postNoIdDto, UUID.randomUUID());
        storage.add(postEmployee);
        return postMapper.toDto(postEmployee);
    }

    @PostMapping("/remove")
    @Operation(summary = "Remove post with filter", tags = "Post")
    public void removePost(@RequestBody PostFilterDto postFilterDto) {
        Filter<PostEmployee> postFilter = FilterUtils.parsePostFilter(postFilterDto);
        storage.remove(postFilter);
    }
}
