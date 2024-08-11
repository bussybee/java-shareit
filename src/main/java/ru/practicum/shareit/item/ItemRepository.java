package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner_Id(Long id);

    @Query(value = "select * from items as i " +
            "where i.name ilike concat('%', ?1, '%') or " +
            "i.description ilike concat('%', ?1, '%')", nativeQuery = true)
    List<Item> searchByText(String text);
}
