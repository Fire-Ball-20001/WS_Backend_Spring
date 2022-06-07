package org.backend.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.backend.spring.actions.filters.PostFilter;
import org.backend.spring.dto.FilterDto;
import org.backend.spring.dto.FullPostDto;
import org.backend.spring.dto.PartPostDto;
import org.backend.spring.mappers.FilterMapper;
import org.backend.spring.mappers.MapperBase;
import org.backend.spring.mappers.PostMapper;
import org.backend.spring.models.PostEmployee;
import org.backend.spring.services.DataStorage;
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
    public FullPostDto getPost(FilterDto filter)
    {
        PostFilter postFilter = filterMapper.toPostEntity(filter);
        return postMapper.toDto(storage.getObject(postFilter));
    }
    @GetMapping("/posts")
    public FullPostDto[] getPosts(FilterDto filter)
    {
        PostFilter postFilter = filterMapper.toPostEntity(filter);
        return postMapper.toDto(storage.getObjects(postFilter));
    }

    @PostMapping("/post/set")
    public ObjectNode setPost(@RequestBody FullPostDto postDto)
    {
        PostEmployee postEmployee = postMapper.toEntity(postDto);
        storage.setObject(postEmployee);
        return objectMapper.createObjectNode().put("id",postEmployee.getId().toString());
    }

    @PostMapping("/post/add")
    @ResponseBody
    public ObjectNode addPost(@RequestBody PartPostDto partPostDto)
    {
        PostEmployee postEmployee = postMapper.toEntity(partPostDto);
        storage.addObject(postEmployee);

        return objectMapper.createObjectNode().put("id",postEmployee.getId().toString());
    }

    @DeleteMapping("/post/remove")
    public ObjectNode removePost(@RequestBody FilterDto filterDto)
    {
        PostFilter filter = filterMapper.toPostEntity(filterDto);
        return objectMapper.createObjectNode().put("status",storage.removeObject(filter));
    }
}
