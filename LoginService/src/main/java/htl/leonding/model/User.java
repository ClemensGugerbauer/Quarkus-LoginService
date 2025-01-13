package htl.leonding.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    public String telephoneNumber;

    public byte[] salt;

    public int recoveryCode;
}
