package ru.practicum.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserShotDto;
import ru.practicum.users.model.NewUserRequest;
import ru.practicum.users.model.User;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User NewUserRequestToUser(NewUserRequest newUserRequest);

    UserDto UserToUserDto(User user);

    UserShotDto UserToUserShotDto(User user);
}
