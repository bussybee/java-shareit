package ru.practicum.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.item.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_IdOrderByStart(Long bookerId);

    List<Booking> findAllByBooker_IdAndEndIsBeforeOrderByStart(Long bookerId, LocalDateTime end);

    List<Booking> findAllByBooker_IdAndEndIsAfterOrderByStart(Long bookerId, LocalDateTime end);

    List<Booking> findAllByBooker_IdAndStartIsAfterOrderByStart(Long bookerId, LocalDateTime end);

    List<Booking> findAllByBooker_IdAndStatusOrderByStart(Long bookerId, BookingStatus status);

    List<Booking> findAllByItem_Id(Long itemId);

    List<Booking> findAllByItemIn(List<Item> items);
}
