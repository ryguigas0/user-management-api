package dev.guiga.proj1.user_management;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.nimbusds.jose.shaded.gson.Gson;

import dev.guiga.proj1.user_management.exceptions.DuplicateUsernameException;
import dev.guiga.proj1.user_management.exceptions.MaxLoginsException;
import dev.guiga.proj1.user_management.exceptions.UserBlockedException;
import dev.guiga.proj1.user_management.exceptions.UsernameInvalidPassword;
import dev.guiga.proj1.user_management.exceptions.UsernameNotFound;
import dev.guiga.proj1.user_management.transfer.FailLoginTO;
import dev.guiga.proj1.user_management.transfer.UserChangePasswordInTO;
import dev.guiga.proj1.user_management.transfer.UserInTO;
import dev.guiga.proj1.user_management.transfer.UserOutTO;
import dev.guiga.proj1.user_management.transfer.UserParser;
import dev.guiga.proj1.user_management.transfer.UserTokenOutTO;
import jakarta.validation.Valid;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private FailLoginTopicService failLoginTopic;

    @Value("${keycloak.client-id}")
    private String keycloakClientId;

    @Value("${keycloak.url}")
    private String keycloakUrl;

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

        List<UserModel> temp = repo.findByBlocked(blocked);

        temp.forEach(userModel -> output.add(UserParser.from(userModel)));

        return output;
    }

    public UserTokenOutTO loginUser(@Valid UserInTO userIn) {
        UserModel userFound = getUser(userIn.username());

        if (userFound.isBlocked())
            throw new UserBlockedException();

        if (!userFound.getPassword().equals(userIn.password())) {
            failLogin(userFound);
            throw new UsernameInvalidPassword(userIn.username());
        }

        String token = getToken(userFound);

        if (token == null) {
            failLogin(userFound);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account not authorized to generate token!");
        }

        int totalLogins = userFound.getTotalLogins() + 1;

        if (totalLogins > 10) {
            throw new MaxLoginsException();
        }

        userFound.setTotalLogins(totalLogins);

        repo.save(userFound);

        return new UserTokenOutTO(token);
    }

    private void failLogin(UserModel user) {
        int totalFailed = user.getTotalFailedLogins() + 1;

        user.setTotalFailedLogins(totalFailed);

        if (totalFailed > 5) {
            user.setBlocked(true);
        }

        failLoginTopic.send(new FailLoginTO(user.getUsername(), totalFailed));

        repo.save(user);
    }

    private String getToken(UserModel user) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
            requestBody.add("username", user.getUsername());
            requestBody.add("password", user.getPassword());
            requestBody.add("client_id", keycloakClientId);
            requestBody.add("grant_type", "password");

            HttpEntity<?> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    keycloakUrl + "/protocol/openid-connect/token",
                    request,
                    String.class);

            Gson gson = new Gson();
            Map<String, String> jsonMap = gson.fromJson(response.getBody(), Map.class);

            return jsonMap.get("access_token");
        } catch (HttpClientErrorException e) {
            return null;
        }

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

    public UserOutTO unblockUser(String username) {
        UserModel user = getUser(username);

        user.setBlocked(false);

        return UserParser.from(user);
    }
}
