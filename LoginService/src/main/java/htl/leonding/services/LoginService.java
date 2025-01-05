package htl.leonding.services;

import htl.leonding.Exceptions.CouldNotAddToDataBaseException;
import htl.leonding.Exceptions.CouldNotLogInException;
import htl.leonding.dto.LoginUserDto;
import htl.leonding.dto.RegisterUserDto;
import htl.leonding.model.User;
import htl.leonding.repository.UserRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
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

    private UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerNewUserInDB(RegisterUserDto user) throws CouldNotAddToDataBaseException {

        try {
            String hashedPassword = hashPassword(user.password());
            userRepository.persist(User.builder()
                    .username(user.username())
                    .password(hashedPassword)
                    .telephoneNumber(user.telephoneNumber())
                    .build());
            return Jwt.issuer("https://htl-leonding.at").sign();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public void changePasswordInDB(String username, String password) {

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            String hashedPassword = null;
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
                return Jwt.issuer("https://htl-leonding.at").sign();
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

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 56_000);
        SecretKeyFactory factory;

        factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();

        return new String(hash);
    }

    public boolean areRecoveryCodesEqual(String username, int code) {

        Optional<User> user = userRepository.findByUsername(username);

        if(user.isPresent()) {
            if(user.get().recoveryCode == code) {
                return true;
            }
        }

        return false;
    }

    public void setRecoveryNumber(String username, int recoveryNumber) {

        Optional<User> user = userRepository.findByUsername(username);

        if(user.isPresent()) {
            user.get().recoveryCode = recoveryNumber;
        }
    }
}