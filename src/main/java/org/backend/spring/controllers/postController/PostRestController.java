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
import org.backend.spring.actions.filters.PostFilter;
import org.backend.spring.dto.FilterDto;
import org.backend.spring.dto.post.PostDto;
import org.backend.spring.dto.post.PostNoIdDto;
import org.backend.spring.mappers.FilterMapper;
import org.backend.spring.mappers.PostMapper;
import org.backend.spring.models.PostEmployee;
import org.backend.spring.services.DataStorage;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController()
@AllArgsConstructor
public class PostRestController {

    DataStorage<PostEmployee> storage;
    PostMapper postMapper;
    ObjectMapper objectMapper;
    FilterMapper filterMapper;

    @GetMapping("/post")
    @Operation(summary = "Get one post with filter",tags = "Post")
    @Parameters(value = {
            @Parameter(
                    name = "filter",
                    hidden = true
            ),
            @Parameter(
                    name="postId",
                    allowEmptyValue = true,
                    in = ParameterIn.QUERY,
                    content = {
                            @Content(
                                    schema = @Schema(type = "string")
                            )
                    }
            ),
            @Parameter(
                    name="postName",
                    allowEmptyValue = true,
                    in = ParameterIn.QUERY,
                    content = {
                            @Content(
                                    schema = @Schema(type = "string")
                            )
                    }
            ),
            @Parameter(
                    name="isStrictly",
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
                    description = "Find one post",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = PostDto.class)
                            )
                    )
            )
    })
    public PostDto getPost(FilterDto filter)
    {
        PostFilter postFilter = filterMapper.toPostEntity(filter);
        return postMapper.toDto(storage.get(postFilter));
    }
    @GetMapping("/posts")
    @Operation(summary = "Get more posts with filter",tags = "Post")
    @Parameters(value = {
            @Parameter(
                    name = "filter",
                    hidden = true
            ),
            @Parameter(
                    name="postId",
                    allowEmptyValue = true,
                    in = ParameterIn.QUERY,
                    content = {
                            @Content(
                                    schema = @Schema(type = "string")
                            )
                    }
            ),
            @Parameter(
                    name="postName",
                    allowEmptyValue = true,
                    in = ParameterIn.QUERY,
                    content = {
                            @Content(
                                    schema = @Schema(type = "string")
                            )
                    }
            ),
            @Parameter(
                    name="isStrictly",
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
    public PostDto[] getPosts(FilterDto filter)
    {
        PostFilter postFilter = filterMapper.toPostEntity(filter);
        return postMapper.toDto(storage.getArray(postFilter));
    }

    @PostMapping("/post/set")
    @Operation(summary = "Replace post",tags = "Post")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(example = "{\n\"id\":\"302e187a-52e4-46c7-820f-f85cf06a022f\",\n\"name\":\"Example\"}")
            )
    })
    @Parameters(value = {
            @Parameter(
                    name = "postDto",
                    hidden = true
            ),
            @Parameter(
                    name="id",
                    required = true,
                    in = ParameterIn.DEFAULT,
                    content = {
                            @Content(
                                    schema = @Schema(type = "string")
                            )
                    }
            ),
            @Parameter(
                    name="name",
                    required = true,
                    in = ParameterIn.DEFAULT,
                    content = {
                            @Content(
                                    schema = @Schema(type = "string")
                            )
                    }
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Replace post",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\n\"id\":\"302e187a-52e4-46c7-820f-f85cf06a022f\"\n}")
                    )
            )
    })
    public ObjectNode setPost(@RequestBody PostDto postDto)
    {
        PostEmployee postEmployee = postMapper.toEntity(postDto);
        storage.set(postEmployee);
        return objectMapper.createObjectNode().put("id",postEmployee.getId().toString());
    }

    @PostMapping("/post/add")
    @ResponseBody
    @Operation(summary = "Add new post",tags = "Post")
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
            ),
            @Parameter(
                    name="name",
                    required = true,
                    in = ParameterIn.DEFAULT,
                    content = {
                            @Content(
                                    schema = @Schema(type = "string")
                            )
                    }
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Add new post",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\n\"id\":\"302e187a-52e4-46c7-820f-f85cf06a022f\"\n}")
                    )
            )
    })
    public ObjectNode addPost(@RequestBody PostNoIdDto postNoIdDto)
    {
        PostEmployee postEmployee = postMapper.toEntity(postNoIdDto);
        storage.add(postEmployee);

        return objectMapper.createObjectNode().put("id",postEmployee.getId().toString());
    }

    @DeleteMapping("/post/remove")
    @Operation(summary = "Remove post with strictly filter",tags = "Post")

    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(example = "{\n\"postId\":\"302e187a-52e4-46c7-820f-f85cf06a022f\",\n\"postName\":\"Example\"}")
            )
    })
    @Parameters(value = {
            @Parameter(
                    name = "filterDto",
                    hidden = true
            ),
            @Parameter(
                    name="postId",
                    in = ParameterIn.DEFAULT,
                    content = {
                            @Content(
                                    schema = @Schema(type = "string")
                            )
                    }
            ),
            @Parameter(
                    name="postName",
                    in = ParameterIn.DEFAULT,
                    content = {
                            @Content(
                                    schema = @Schema(type = "string")
                            )
                    }
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
    public ObjectNode removePost(@RequestBody FilterDto filterDto)
    {
        PostFilter filter = filterMapper.toPostEntity(filterDto);
        boolean status = storage.remove(filter);
        return objectMapper.createObjectNode().put("status",status);
    }
}
