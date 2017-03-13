package phonebook.persistence;

import phonebook.domain.Contact;
import phonebook.domain.User;

import java.util.List;

/**
 * @author Konstantin Kosmachevskiy
 */
public interface ContactRepository {
    List<Contact> getAllByUser(User user);

    void addToUser(User user, Contact contact);

    void removeFromUser(User user, Contact contact);

    void update(User user, Contact contact);

    List<Contact> findByNamesAndPhones(User user, String pattern);
}
