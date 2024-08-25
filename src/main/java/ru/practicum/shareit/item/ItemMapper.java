package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemGetAllResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDto toDTO(Item item);

    Item toItem(ItemDto itemDto);

    Item toItem(ItemUpdateRequestDto requestDto);

    ItemGetAllResponseDto toResponseDTO(Item item);
}
