package dev.guiga.proj1.user_management;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import dev.guiga.proj1.user_management.exceptions.DuplicateUsernameException;
import dev.guiga.proj1.user_management.transfer.UserInTO;
import dev.guiga.proj1.user_management.transfer.UserOutTO;
import dev.guiga.proj1.user_management.transfer.UserParser;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    public UserOutTO createUser(UserInTO userIn) {
        try {
            User savedUser = repo.save(UserParser.from(userIn));

            return UserParser.from(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateUsernameException(userIn.username());
        }
    }

    public List<UserOutTO> listUsers() {

        List<UserOutTO> output = new ArrayList<UserOutTO>();

        repo.findAll().forEach(userModel -> output.add(UserParser.from(userModel)));

        return output;
    }
}
