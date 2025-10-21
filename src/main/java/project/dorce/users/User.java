package project.dorce.users;

import jakarta.persistence.*;
import lombok.Data;

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

    public User(){

    }

    public User(String username, String password, String authToken) {
        this.username = username;
        this.password = password;
        this.authToken = authToken;
    }
}
