package htl.leonding;

import htl.leonding.dto.LoginUserDto;
import htl.leonding.dto.RegisterUserDto;
import htl.leonding.model.User;
import htl.leonding.repository.UserRepository;
import htl.leonding.services.LoginService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@QuarkusTest
class LoginServiceTest {

    @Inject
    LoginService loginService;

    @Inject
    UserRepository userRepository;


    @Test
    public void testRegister() throws Exception {
        RegisterUserDto registerUserDto = new RegisterUserDto("testuser@example.com", "password123", "+123456789");

        String token = loginService.registerNewUserInDB(registerUserDto);

        assertNotNull(token);

        User user = userRepository.findByUsername("testuser@example.com").orElse(null);
        assertNotNull(user);
    }

    @Test
    public void testLogin() throws Exception {
        RegisterUserDto registerUserDto = new RegisterUserDto("testuser@example.com", "password123", "+123456789");
        loginService.registerNewUserInDB(registerUserDto);

        LoginUserDto loginUserDto = new LoginUserDto("testuser@example.com", "password123");
        String token = loginService.loginUser(loginUserDto);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    public void testRequestReset() {
        // TODO: Clemens
    }

    @Test
    public void testResetPassword() {
        // TODO: Clemens
    }
}
