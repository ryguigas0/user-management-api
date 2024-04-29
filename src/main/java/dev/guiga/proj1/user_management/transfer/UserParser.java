package dev.guiga.proj1.user_management.transfer;

import dev.guiga.proj1.user_management.UserModel;

public class UserParser {
    public static UserModel from(UserInTO userIn) {
        return UserModel.builder()
                .username(userIn.username())
                .password(userIn.password())
                .build();
    }

    public static UserOutTO from(UserModel user) {
        return new UserOutTO(user.getId(), user.getUsername(), user.isBlocked());
    }
}
