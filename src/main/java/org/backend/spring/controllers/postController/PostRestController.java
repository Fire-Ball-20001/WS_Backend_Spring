package org.backend.spring.controllers.postController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.backend.spring.actions.filters.Filter;
import org.backend.spring.dto.FilterDto;
import org.backend.spring.dto.post.PostDto;
import org.backend.spring.dto.post.PostNoIdDto;
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
    @Operation(summary = "Get one post, id can be partial.", tags = "Post")
    @Parameters(value = {
            @Parameter(
                    name = "filter",
                    hidden = true
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Find one post",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = PostDto.class)
                            )
                    )
            )
    })
    public PostDto getPost(@PathVariable String id, FilterDto filter) {
        filter.setId(id);
        Filter<PostEmployee> postFilter = FilterUtils.parsePostFilter(filter);
        return postMapper.toDto(storage.get(postFilter));
    }

    @GetMapping("/list")
    @Operation(summary = "Get more posts with filter", tags = "Post")
    @Parameters(value = {
            @Parameter(
                    name = "filter",
                    hidden = true
            ),
            @Parameter(
                    name = "postId",
                    allowEmptyValue = true,
                    in = ParameterIn.QUERY,
                    content = {
                            @Content(
                                    schema = @Schema(type = "string")
                            )
                    }
            ),
            @Parameter(
                    name = "postName",
                    allowEmptyValue = true,
                    in = ParameterIn.QUERY,
                    content = {
                            @Content(
                                    schema = @Schema(type = "string")
                            )
                    }
            ),
            @Parameter(
                    name = "isStrictly",
                    allowEmptyValue = true,
                    in = ParameterIn.QUERY,
                    content = {
                            @Content(
                                    schema = @Schema(
                                            type = "boolean",
                                            defaultValue = "false"
                                    )
                            )
                    }
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Find more posts"
            )
    })
    public PostDto[] getPosts(FilterDto filter) {
        Filter<PostEmployee> postFilter = FilterUtils.parsePostFilter(filter);
        return postMapper.toDto(storage.getArray(postFilter));
    }

    @PostMapping("/{id}/set")
    @Operation(summary = "Replace post", tags = "Post")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(example = "{\n\"name\":\"Example\"}")
            )
    })
    @Parameters(value = {
            @Parameter(
                    name = "postDto",
                    hidden = true
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Replace post",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\n\"id\":\"00000000-0000-0000-0000-000000000000\"\n}")
                    )
            )
    })
    public ObjectNode setPost(@PathVariable UUID id, @RequestBody PostNoIdDto postDto) {
        PostEmployee postEmployee = postMapper.toEntity(postDto,id);
        storage.set(postEmployee);
        return objectMapper.createObjectNode().put("id", postEmployee.getId().toString());
    }

    @PostMapping("/add")
    @ResponseBody
    @Operation(summary = "Add new post", tags = "Post")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(example = "{\n\"name\":\"Example\"}")
            )
    })
    @Parameters(value = {
            @Parameter(
                    name = "partPostDto",
                    hidden = true
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Add new post",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\n\"id\":\"00000000-0000-0000-0000-000000000000\"\n}")
                    )
            )
    })
    public ObjectNode addPost(@RequestBody PostNoIdDto postNoIdDto) {
        PostEmployee postEmployee = postMapper.toEntity(postNoIdDto, UUID.randomUUID());
        storage.add(postEmployee);

        return objectMapper.createObjectNode().put("id", postEmployee.getId().toString());
    }

    @PostMapping("/remove")
    @Operation(summary = "Remove post with filter", tags = "Post")

    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(example = "{\n\"postId\":\"00000000-0000-0000-0000-000000000000\",\n\"postName\":\"Example\"}")
            )
    })
    @Parameters(value = {
            @Parameter(
                    name = "filterDto",
                    hidden = true
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Remove post",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\n\"status\":\"true\"\n}")
                    )
            )
    })
    public ObjectNode removePost(@RequestBody FilterDto filterDto) {
        Filter<PostEmployee> postFilter = FilterUtils.parsePostFilter(filterDto);
        boolean status = storage.remove(postFilter);
        return objectMapper.createObjectNode().put("status", status);
    }
}
