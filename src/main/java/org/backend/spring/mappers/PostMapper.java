package org.backend.spring.mappers;

import org.backend.spring.dto.post.PostDto;
import org.backend.spring.dto.post.PostNoIdDto;
import org.backend.spring.models.PostEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PostMapper {
    PostEmployee toEntity(PostDto dto);
    @Mapping(target = "id",expression = "java(java.util.UUID.randomUUID())")
    PostEmployee toEntity(PostNoIdDto dto);
    PostDto toDto(PostEmployee post);
    PostEmployee[] toEntity(PostDto[] dto);
    PostDto[] toDto(PostEmployee[] post);
}
