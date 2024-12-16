package htl.leonding;

import htl.leonding.model.User;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;


@QuarkusTest
class LoginServiceTest {

    @Inject
    private UserService userService;

    @Test
    public void testUserCreation() {
        userService.createUser(User.builder()
                .username("ichhobdichlieb@gmail.com")
                .password("1234")
                .telephoneNumber("+43 660 1234567")
                .build());
    }

    // TODO: Clemens
    // Endpoints TESTEN
}

