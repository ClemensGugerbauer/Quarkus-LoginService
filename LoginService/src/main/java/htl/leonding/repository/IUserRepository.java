package htl.leonding.repository;

import htl.leonding.model.User;

import java.util.Optional;

public interface IUserRepository {
    Optional<User> findByUsername(String username);
}
