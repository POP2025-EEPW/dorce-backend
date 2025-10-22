package project.dorce.usermanager;

import org.springframework.stereotype.Service;
import project.dorce.usermanager.dto.UserRegistrationRequest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String generateAuthToken(){
        return UUID.randomUUID().toString();
    }

    public User registerNewUser(UserRegistrationRequest request){
        if(userRepository.findByUsername(request.getUsername()) != null){
            throw new RuntimeException("Username already exists");
        }

        final var passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        final var encodedPassword = passwordEncoder.encode(request.getPassword());

        final var authToken = generateAuthToken();

        final var user = new User(request.getUsername(), encodedPassword, Role.DataUser, authToken);

        return userRepository.save(user);
    }
}
