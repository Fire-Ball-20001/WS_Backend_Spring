package org.backend.spring.controllers;

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
import org.backend.spring.actions.filters.PostFilter;
import org.backend.spring.dto.FilterDto;
import org.backend.spring.dto.FullPostDto;
import org.backend.spring.dto.PartPostDto;
import org.backend.spring.mappers.FilterMapper;
import org.backend.spring.mappers.MapperBase;
import org.backend.spring.mappers.PostMapper;
import org.backend.spring.models.PostEmployee;
import org.backend.spring.services.DataStorage;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController()
public class PostRestController {

    DataStorage<PostEmployee> storage;
    PostMapper postMapper;
    ObjectMapper objectMapper;
    FilterMapper filterMapper;

    public PostRestController(DataStorage<PostEmployee> storage, MapperBase base,ObjectMapper objectMapper) {
        this.storage = storage;
        this.objectMapper = objectMapper;
        postMapper = base.getPostMapper();
        filterMapper = base.getFilterMapper();
    }

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
                                    schema = @Schema(implementation = FullPostDto.class)
                            )
                    )
            )
    })
    public FullPostDto getPost(FilterDto filter)
    {
        PostFilter postFilter = filterMapper.toPostEntity(filter);
        return postMapper.toDto(storage.getObject(postFilter));
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
    public FullPostDto[] getPosts(FilterDto filter)
    {
        PostFilter postFilter = filterMapper.toPostEntity(filter);
        return postMapper.toDto(storage.getObjects(postFilter));
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
    public ObjectNode setPost(@RequestBody FullPostDto postDto)
    {
        PostEmployee postEmployee = postMapper.toEntity(postDto);
        storage.setObject(postEmployee);
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
    public ObjectNode addPost(@RequestBody PartPostDto partPostDto)
    {
        PostEmployee postEmployee = postMapper.toEntity(partPostDto);
        storage.addObject(postEmployee);

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
        return objectMapper.createObjectNode().put("status",storage.removeObject(filter));
    }
}
