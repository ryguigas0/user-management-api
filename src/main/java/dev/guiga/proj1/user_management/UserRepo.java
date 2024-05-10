package dev.guiga.proj1.user_management;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<UserModel, Integer> {
    UserModel findByUsername(String username);

    List<UserModel> findByBlocked(boolean blocked);
}
