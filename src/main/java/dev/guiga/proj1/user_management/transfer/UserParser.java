package dev.guiga.proj1.user_management.transfer;

import dev.guiga.proj1.user_management.User;

public class UserParser {
    public static User from(UserInTO userIn) {
        return User.builder()
                .username(userIn.username())
                .password(userIn.password())
                .build();
    }

    public static UserOutTO from(User user) {
        return new UserOutTO(user.getId(), user.getUsername(), user.isBlocked());
    }
}
