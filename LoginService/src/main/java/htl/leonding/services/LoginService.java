package htl.leonding.services;

import htl.leonding.Exceptions.CouldNotAddToDataBaseException;
import htl.leonding.Exceptions.CouldNotLogInException;
import htl.leonding.dto.LoginUserDto;
import htl.leonding.dto.RegisterUserDto;
import htl.leonding.model.User;
import htl.leonding.repository.UserRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Optional;


@ApplicationScoped
@NoArgsConstructor
public class LoginService {

    @Inject
    UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional
    public String registerNewUserInDB(RegisterUserDto user) throws CouldNotAddToDataBaseException {
        try {
            String hashedPassword = hashPassword(user.password());
            userRepository.persist(User.builder()
                    .username(user.username())
                    .password(hashedPassword)
                    .telephoneNumber(user.telephoneNumber())
                    .build());
            return Jwt.issuer("login-service").subject(user.username()).expiresAt(System.currentTimeMillis() + 3600).sign();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public void changePasswordInDB(String username, String password) {

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            String hashedPassword;
            try {
                hashedPassword = hashPassword(password);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
            user.get().setPassword(hashedPassword);
            userRepository.persist(user.get());
        }


    }

    public String loginUser(LoginUserDto loginUserDto) throws CouldNotLogInException {

        Optional<User> user = userRepository.findByUsername(loginUserDto.username());

        if(user.isEmpty()) {
            throw new CouldNotLogInException("");
        }

        try {
            if(hashPassword(loginUserDto.password()).equals(user.get().password)) {
                return Jwt.issuer("login-service").subject(loginUserDto.username()).expiresAt(System.currentTimeMillis() + 3600).sign();
            }
            else{
                return "";
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public String hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        int keyLength = 128;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 56_000, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();

        return new String(hash);
    }

    public boolean areRecoveryCodesEqual(String username, int code) {

        Optional<User> user = userRepository.findByUsername(username);

        return user.filter(value -> value.recoveryCode == code).isPresent();

    }

    public void setRecoveryNumber(String username, int recoveryNumber) {

        Optional<User> user = userRepository.findByUsername(username);

        user.ifPresent(value -> value.recoveryCode = recoveryNumber);
    }
}