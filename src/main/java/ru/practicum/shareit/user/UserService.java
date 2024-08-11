package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.error.UserInvalidDataException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public UserDto create(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        isEmailExists(user);
        return userMapper.toDTO(userRepository.save(user));
    }

    public UserDto update(UserUpdateRequestDto userDto, Long id) {
        User userRep = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        User user = userMapper.toUser(userDto);
        isEmailExists(user);

        user.setId(id);
        user.setName(userDto.getName() == null ? userRep.getName() : userDto.getName());
        user.setEmail(userDto.getEmail() == null ? userRep.getEmail() : userDto.getEmail());

        return userMapper.toDTO(userRepository.save(user));
    }

    private void isEmailExists(User user) {
        Optional<User> emailExistsUser = userRepository.findByEmail(user.getEmail());

        if (emailExistsUser.isPresent()) {
            throw new UserInvalidDataException("Пользователь с такой почтой уже существует");
        }
    }

    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDto getById(Long id) {
        return userMapper.toDTO(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден")));
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
