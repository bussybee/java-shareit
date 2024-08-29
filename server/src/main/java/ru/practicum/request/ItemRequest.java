package ru.practicum.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.*;
import ru.practicum.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String description;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    User requester;
    @Column(name = "created_time")
    LocalDateTime created;
}
