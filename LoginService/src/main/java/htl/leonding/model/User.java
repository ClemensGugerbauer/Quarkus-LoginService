package htl.leonding.model;
import jakarta.persistence.*;
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
    public String username;
    public String password;
    public String telephoneNumber;
    public String resetToken;
}
