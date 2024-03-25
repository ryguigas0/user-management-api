package dev.guiga.proj1.user_management;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import dev.guiga.proj1.user_management.exceptions.DuplicateUsernameException;
import dev.guiga.proj1.user_management.exceptions.MaxLoginsException;
import dev.guiga.proj1.user_management.exceptions.UsernameInvalidPassword;
import dev.guiga.proj1.user_management.exceptions.UsernameNotFound;
import dev.guiga.proj1.user_management.transfer.UserInTO;
import dev.guiga.proj1.user_management.transfer.UserOutTO;
import dev.guiga.proj1.user_management.transfer.UserParser;
import jakarta.validation.Valid;

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

    public UserOutTO loginUser(@Valid UserInTO userIn) {
        User userFound = repo.findByUsername(userIn.username());

        if (userFound == null) {
            throw new UsernameNotFound(userIn.username());
        }
        

        if (!userFound.getPassword().equals(userIn.password())) {
            int totalFailed = userFound.getTotalFailedLogins() + 1;

            userFound.setTotalFailedLogins(totalFailed);

            if (totalFailed > 5) {
                userFound.setBlocked(true);
            }

            repo.save(userFound);

            throw new UsernameInvalidPassword(userIn.username());
        }

        int totalLogins = userFound.getTotalLogins() + 1;

        if (totalLogins > 10) {
            throw new MaxLoginsException();
        }

        userFound.setTotalLogins(totalLogins);

        repo.save(userFound);

        return UserParser.from(userFound);
    }
}
