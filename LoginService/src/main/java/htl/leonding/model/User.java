package htl.leonding.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue
    public Long id;

    @Email
    @NotBlank
    public String username;

    @NotBlank
    public String password;

    @Pattern(regexp = "^\\+(?:43|0043)?[ ()\\/-]*0[6-9]\\d{6,7}$", message = "Sogamoi bitte a österreichische Telefonnummer ein")
    public String telephoneNumber;

    public String resetToken;
}
