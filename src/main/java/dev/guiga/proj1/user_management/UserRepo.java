package dev.guiga.proj1.user_management;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User, Integer> {
    User findByUsername(String username);

    List<User> findAllByBlocked(boolean blocked);
}
