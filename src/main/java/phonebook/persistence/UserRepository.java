package phonebook.persistence;

import phonebook.domain.User;

import java.util.Optional;

/**
 * @author Konstantin Kosmachevskiy
 */
public interface UserRepository {

    Optional<User> getByUserName(String userName);

    void add(User user);
}
