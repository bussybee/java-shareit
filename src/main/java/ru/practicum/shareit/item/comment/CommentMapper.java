package ru.practicum.shareit.item.comment;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto toDTO(Comment comment);

    Comment toComment(CommentDto commentDto);
}
