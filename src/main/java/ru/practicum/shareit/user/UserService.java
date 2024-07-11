package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public UserDto create(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        return userMapper.toDTO(userRepository.save(user));
    }

    public UserDto update(UserUpdateRequestDto userDto, Long id) {
        User userRep = userRepository.findById(id);
        User user = userMapper.toUser(userDto);

        user.setId(id);
        user.setName(userDto.getName() == null ? userRep.getName() : userDto.getName());
        user.setEmail(userDto.getEmail() == null ? userRep.getEmail() : userDto.getEmail());

        return userMapper.toDTO(userRepository.update(user));
    }

    public List<UserDto> getAll() {
        return userRepository.getAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDto getById(Long id) {
        return userMapper.toDTO(userRepository.findById(id));
    }

    public void delete(Long id) {
        userRepository.delete(id);
    }
}
