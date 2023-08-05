package ru.practicum.users.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.NewUserRequest;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(int[] ids, Pageable pageable) {
        List<UserDto> userDtos;
        if (ids != null) {
            userDtos = userRepository.getAllByIdIn(ids, pageable).stream()
                    .map(UserMapper.INSTANCE::UserToUserDto)
                    .collect(Collectors.toList());
        } else {
            userDtos = userRepository.findAll(pageable).stream()
                    .map(UserMapper.INSTANCE::UserToUserDto)
                    .collect(Collectors.toList());
        }
        return userDtos;
    }

    @Override
    public UserDto addUser(NewUserRequest newUserRequest) {
        User user = userRepository.save(UserMapper.INSTANCE.NewUserRequestToUser(newUserRequest));
        return UserMapper.INSTANCE.UserToUserDto(user);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        userRepository.deleteById(userId);
    }
}
