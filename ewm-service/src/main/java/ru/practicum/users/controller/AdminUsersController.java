package ru.practicum.users.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.NewUserRequest;
import ru.practicum.users.service.UserService;
import ru.practicum.utils.PageRequestUtil;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@AllArgsConstructor
public class AdminUsersController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) int[] ids,
                                  @RequestParam(value = "from", defaultValue = "0") int from,
                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequestUtil.createPageRequest(from, size);
        return userService.getUsers(ids, pageable);
    }

    @PostMapping
    public UserDto addUser(NewUserRequest newUserRequest) {
        return userService.addUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }
}
