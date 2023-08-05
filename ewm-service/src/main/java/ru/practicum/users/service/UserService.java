package ru.practicum.users.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.NewUserRequest;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(int[] ids, Pageable pageable);

    UserDto addUser(NewUserRequest userDto);

    void deleteUser(long userId);

}
