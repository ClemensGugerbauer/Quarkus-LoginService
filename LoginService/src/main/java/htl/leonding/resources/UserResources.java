package htl.leonding.resources;

import htl.leonding.UserService;
import htl.leonding.model.User;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
public class UserResources {
    @Inject
    UserService userService;

    @POST
    @Path("/register")
    public void register(User user) {
        userService.createUser(user);
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean login(@QueryParam("username") String username, @QueryParam("password") String password) {
        return userService.verifyPassword(username, password);
    }

    @POST
    @Path("/request-reset")
    @Consumes(MediaType.APPLICATION_JSON)
    public String requestReset(@QueryParam("username") String username) {
        return userService.generateResetToken(username);
    }

    @POST
    @Path("/reset-password")
    @Consumes(MediaType.APPLICATION_JSON)
    public void resetPassword(@QueryParam("resetToken") String resetToken, @QueryParam("newPassword") String newPassword) {
        userService.resetPasswordWithToken(resetToken, newPassword);
    }
}
