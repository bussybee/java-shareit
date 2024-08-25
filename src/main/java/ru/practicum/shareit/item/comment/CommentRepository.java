package ru.practicum.shareit.item.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.Item;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthor_IdAndItem_Id(Long authorId, Long itemId);

    List<Comment> findByItemIn(List<Item> items);
}
