package htl.leonding.repository;

import htl.leonding.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class UserRepository implements IUserRepository, PanacheRepositoryBase<User, Long>{
    public Optional<User> findByUsername(String username) {
        return find("username", username).firstResultOptional();
    }
}
