package project.dorce.authmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import project.dorce.authmanager.dto.AuthRequest;
import project.dorce.usermanager.UserRepository;

import java.util.Map;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, Object> authUser(AuthRequest request){
        var user = userRepository.findByUsername(request.getUsername());

        final var passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        if(user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("Invalid username or password");
        }

        return Map.of(
                "token", user.getAuthToken(),
                "roles", user.getRoles()
        );
    }
}
