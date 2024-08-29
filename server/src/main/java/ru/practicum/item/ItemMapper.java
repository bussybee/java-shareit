package ru.practicum.item;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemDtoForRequest;
import ru.practicum.item.dto.ItemGetAllResponseDto;
import ru.practicum.item.dto.ItemUpdateRequestDto;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper {

    ItemDto toDTO(Item item);

    Item toItem(ItemDto itemDto);

    Item toItem(ItemUpdateRequestDto requestDto);

    ItemGetAllResponseDto toResponseDTO(Item item);

    ItemDtoForRequest toResponse(Item item);
}
