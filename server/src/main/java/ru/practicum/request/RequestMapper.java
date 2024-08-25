package ru.practicum.request;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.ResponseDto;
import ru.practicum.request.dto.ResponseDtoWithItems;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequestMapper {

    ItemRequest toEntity(RequestDto requestDto);

    ResponseDto toDto(ItemRequest itemRequest);

    ResponseDtoWithItems toDtoWithItems(ItemRequest itemRequest);
}