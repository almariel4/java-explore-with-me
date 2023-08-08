package ru.practicum.evmservice.users.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.evmservice.exceptions.ApiError;
import ru.practicum.evmservice.exceptions.IncorrectRequestEcxeption;
import ru.practicum.evmservice.exceptions.NotFoundException;
import ru.practicum.evmservice.users.dto.UserDto;
import ru.practicum.evmservice.users.mapper.UserMapper;
import ru.practicum.evmservice.users.model.NewUserRequest;
import ru.practicum.evmservice.users.model.User;
import ru.practicum.evmservice.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, Pageable pageable) {
        List<UserDto> userDtos;
        if (ids != null) {
            userDtos = userRepository.getAllByIdIn(ids, pageable).stream()
                    .map(UserMapper.INSTANCE::userToUserDto)
                    .collect(Collectors.toList());
        } else {
            userDtos = userRepository.findAll(pageable).stream()
                    .map(UserMapper.INSTANCE::userToUserDto)
                    .collect(Collectors.toList());
        }
        return userDtos;
    }

    @Override
    public UserDto addUser(NewUserRequest newUserRequest) {
        if (userRepository.getUserByName(newUserRequest.getName()) != null) {
            throw new IncorrectRequestEcxeption(new ApiError(HttpStatus.CONFLICT.toString(),
                    "Пользователь с таким именем уже существует",
                    "Пользователь с таким именем уже существует", LocalDateTime.now().toString()));
        }
        User user = userRepository.save(UserMapper.INSTANCE.newUserRequestToUser(newUserRequest));
        return UserMapper.INSTANCE.userToUserDto(user);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        userRepository.deleteById(userId);
    }
}
