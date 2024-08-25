package ru.practicum.item;

import lombok.*;
import lombok.experimental.FieldDefaults;


import jakarta.persistence.*;
import ru.practicum.request.ItemRequest;
import ru.practicum.user.User;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    Boolean available;
    @ManyToOne()
    @JoinColumn(name = "owner_id")
    User owner;
    @OneToOne
    @JoinColumn(name = "request_id")
    ItemRequest request;
}
