package htl.leonding;

import htl.leonding.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.jboss.logging.Logger;
import htl.leonding.repository.UserRepository;

import java.util.UUID;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    private static final Logger LOGGER = Logger.getLogger(UserService.class);

    @Transactional
    public void createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ValidationException("User with this email already exists.");
        }
        userRepository.persist(user);
        LOGGER.infof("Email sent to %s", user.getUsername());
        LOGGER.infof("SMS sent to %s", user.getTelephoneNumber());
    }

    public boolean verifyPassword(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    @Transactional
    public String generateResetToken(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ValidationException("User not found."));
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        userRepository.persist(user);
        LOGGER.infof("Reset token generated for user: %s", username);
        return resetToken;
    }

    @Transactional
    public void resetPasswordWithToken(String resetToken, String newPassword) {
        User user = userRepository.find("resetToken", resetToken)
                .firstResultOptional()
                .orElseThrow(() -> new ValidationException("Invalid reset token."));
        user.setPassword(newPassword);
        user.setResetToken(null); // Token muss nach dem benutzn nimma funktionieren
        userRepository.persist(user);
        LOGGER.infof("Password reset with token for user: %s", user.getUsername());
    }
}
