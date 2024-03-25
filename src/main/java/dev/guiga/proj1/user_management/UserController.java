package dev.guiga.proj1.user_management;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.guiga.proj1.user_management.transfer.UserInTO;
import dev.guiga.proj1.user_management.transfer.UserOutTO;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/")
    public UserOutTO createUser(@RequestBody @Valid UserInTO userIn) {
        return service.createUser(userIn);
    }

    @PutMapping("/login")
    public UserOutTO loginUser(@RequestBody @Valid UserInTO userIn) {
        return service.loginUser(userIn);
    }

    @GetMapping("/")
    public List<UserOutTO> listUsers() {
        return service.listUsers();
    }

}
