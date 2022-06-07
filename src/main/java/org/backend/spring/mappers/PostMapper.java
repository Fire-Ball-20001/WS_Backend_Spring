package org.backend.spring.mappers;

import org.backend.spring.dto.FullPostDto;
import org.backend.spring.dto.PartPostDto;
import org.backend.spring.models.PostEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PostMapper {
    PostEmployee toEntity(FullPostDto dto);
    @Mapping(target = "id",expression = "java(java.util.UUID.randomUUID())")
    PostEmployee toEntity(PartPostDto dto);
    FullPostDto toDto(PostEmployee post);
    PostEmployee[] toEntity(FullPostDto[] dto);
    FullPostDto[] toDto(PostEmployee[] post);
}
