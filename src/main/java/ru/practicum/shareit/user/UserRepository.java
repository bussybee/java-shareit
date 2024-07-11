package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.error.UserInvalidDataException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRepository {
    final Map<Long, User> users = new HashMap<>();
    Long idGenerator = 1L;

    public User save(User user) {
        isEmailExists(user.getId(), user.getEmail());

        user.setId(idGenerator++);
        user.setName(user.getName());
        user.setEmail(user.getEmail());

        users.put(user.getId(), user);

        return user;
    }

    public User update(User user) {
        isEmailExists(user.getId(), user.getEmail());
        users.put(user.getId(), user);
        return user;
    }

    public User findById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с id=%s не найден", id));
        }
        return user;
    }

    private void isEmailExists(Long id, String email) {
        boolean emailExists = users.values().stream()
                .filter(user -> !user.getId().equals(id))
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));

        if (emailExists) {
            throw new UserInvalidDataException("Пользователь с такой почтой уже существует");
        }
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public void delete(Long id) {
        users.remove(id);
    }
}
