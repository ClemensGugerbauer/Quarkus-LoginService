package htl.leonding.resources;

import htl.leonding.Exceptions.CouldNotAddToDataBaseException;
import htl.leonding.Exceptions.CouldNotLogInException;
import htl.leonding.dto.LoginUserDto;
import htl.leonding.dto.RegisterUserDto;
import htl.leonding.dto.ResetUserDto;
import htl.leonding.dto.ResetUserDtoWithCode;
import htl.leonding.services.LoginService;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;

@Path("/")
@NoArgsConstructor
public class LoginResources {

    private LoginService loginService;

    public LoginResources(LoginService loginService) {
        this.loginService = loginService;
    }

    @POST
    @Path("register")
    public Response register(RegisterUserDto loginUserDto) {

        String jwt;

        try {
            jwt = loginService.registerNewUserInDB(loginUserDto);
        } catch (CouldNotAddToDataBaseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok(jwt).build();
    }

    @POST
    @Path("login")
    public Response login(LoginUserDto loginUserDto) {
        try {
            String jwt = loginService.loginUser(loginUserDto);
            return Response.ok(jwt).build();
        } catch (CouldNotLogInException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("reset-password")
    public Response resetPassword(ResetUserDto resetUserDto) {

        SecureRandom random = new SecureRandom();
        int recoveryNumber = random.nextInt(10_000, 20_000);

        loginService.setRecoveryNumber(resetUserDto.username(), recoveryNumber);

        return Response.ok().build();
    }

    @POST
    @Path("reset-password-code")
    public Response resetPasswordCode(ResetUserDtoWithCode resetUserDto) {

        boolean result = loginService.areRecoveryCodesEqual(resetUserDto.username(), resetUserDto.RecoveryCode());
        if(result){
            loginService.changePasswordInDB(resetUserDto.username(), resetUserDto.password());
            return Response.ok().build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
