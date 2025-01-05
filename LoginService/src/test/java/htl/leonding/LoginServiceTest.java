package htl.leonding;

import htl.leonding.model.User;
import htl.leonding.services.UserService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


@QuarkusTest
class LoginServiceTest {

    @Inject
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .username("ichhobdichlieb@gmail.com")
                .password("1234")
                .telephoneNumber("+43 660 1234567")
                .build();
    }

    @Test
    public void testRegister() {
        // TODO: Clemens
    }

    @Test
    public void testLogin() {
        // TODO: Clemens
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
