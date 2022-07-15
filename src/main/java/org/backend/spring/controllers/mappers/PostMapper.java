package org.backend.spring.controllers.mappers;

import org.backend.spring.dto.post.PostDto;
import org.backend.spring.dto.post.PostNoIdDto;
import org.backend.spring.models.PostEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper
public interface PostMapper {
    PostEmployee toEntity(PostDto dto);

    @Mapping(target = "id", source = "id")
    PostEmployee toEntity(PostNoIdDto dto, UUID id);

    PostDto toDto(PostEmployee post);

    PostEmployee[] toEntity(PostDto[] dto);

    PostDto[] toDto(PostEmployee[] post);
}
