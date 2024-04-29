package dev.guiga.proj1.user_management;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import dev.guiga.proj1.user_management.exceptions.DuplicateUsernameException;
import dev.guiga.proj1.user_management.exceptions.MaxLoginsException;
import dev.guiga.proj1.user_management.exceptions.UsernameInvalidPassword;
import dev.guiga.proj1.user_management.exceptions.UsernameNotFound;
import dev.guiga.proj1.user_management.transfer.UserChangePasswordInTO;
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
            UserModel savedUser = repo.save(UserParser.from(userIn));

            return UserParser.from(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateUsernameException(userIn.username());
        }
    }

    public List<UserOutTO> listUsers(boolean blocked) {
        List<UserOutTO> output = new ArrayList<UserOutTO>();

        repo.findAllByBlocked(blocked).forEach(userModel -> output.add(UserParser.from(userModel)));

        return output;
    }

    public UserOutTO loginUser(@Valid UserInTO userIn) {
        UserModel userFound = getUser(userIn.username());

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

    public UserOutTO changeUserPassword(UserChangePasswordInTO userIn) {
        UserModel userFound = getUser(userIn.username());

        if (!userFound.getPassword().equals(userIn.oldPassword())) {
            throw new UsernameInvalidPassword(userIn.username());
        }

        userFound.setPassword(userIn.newPassword());
        userFound.setTotalLogins(0);
        repo.save(userFound);

        return UserParser.from(userFound);
    }

    private UserModel getUser(String username) {
        UserModel userFound = repo.findByUsername(username);

        if (userFound == null) {
            throw new UsernameNotFound(username);
        }

        return userFound;
    }

    @Value("${keycloak.client-id}")
    private String keycloakClientId;

    @Value("${keycloak.url}")
    private String keycloakUrl;

    public UserOutTO unblockUser(String username) {
        // RestTemplate restTemplate = new RestTemplate();

        // HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        // requestBody.add("username", username);
        // requestBody.add("password", user.getPassword());
        // requestBody.add("client_id", keycloakClientId);
        // requestBody.add("grant_type", "password");

        // ResponseEntity<String> response = restTemplate.postForEntity(keycloakUrl +
        // "/protocol/openid-connect/token",
        // new HttpEntity<>(requestBody, headers), String.class);

        // Gson gson = new Gson();
        // JsonObject json = gson.fromJson(response.getBody(), JsonObject.class);

        // System.out.println(json);

        UserModel user = getUser(username);

        user.setBlocked(false);

        return UserParser.from(user);
    }
}
