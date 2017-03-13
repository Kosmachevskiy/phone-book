package phonebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import phonebook.domain.User;
import phonebook.persistence.UserRepository;

import java.util.Optional;

/**
 * Extends persistence functional
 *
 * @author Konstantin Kosmachevskiy
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getByUserName(String userName) {
        return userRepository.getByUserName(userName);
    }

    @Override
    public void add(User user) {
        user.setConfirmPassword(""); // Just reset //
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.add(user);
    }
}
