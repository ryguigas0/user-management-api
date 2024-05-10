package dev.guiga.proj1.user_management;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.guiga.proj1.user_management.transfer.UserChangePasswordInTO;
import dev.guiga.proj1.user_management.transfer.UserInTO;
import dev.guiga.proj1.user_management.transfer.UserOutTO;
import dev.guiga.proj1.user_management.transfer.UserTokenOutTO;
import jakarta.validation.Valid;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public UserTokenOutTO loginUser(@RequestBody @Valid UserInTO userIn) {
        return service.loginUser(userIn);
    }

    @PutMapping("/change-password")
    public UserOutTO changeUserPassword(@RequestBody @Valid UserChangePasswordInTO userIn) {
        return service.changeUserPassword(userIn);
    }

    @GetMapping("/blocked")
    public List<UserOutTO> listUsers() {
        return service.listUsers(true);
    }

    @PutMapping("/unblock/{username}")
    public UserOutTO unblockUser(@PathVariable String username) {
        return service.unblockUser(username);
    }

}
