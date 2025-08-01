package com.dgramada.registrationexample.user;

import com.dgramada.registrationexample.user.dto.UserCreateDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody @Valid UserCreateDTO dto) {
        userService.save(dto);
    }

}
