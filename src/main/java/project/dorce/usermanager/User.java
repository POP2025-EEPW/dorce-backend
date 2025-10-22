package project.dorce.usermanager;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String authToken;

    @Column(nullable = false)
    private List<Role> roles;

    public User(){

    }

    public User(String username, String password, List<Role> roles, String authToken) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.authToken = authToken;
    }
}
