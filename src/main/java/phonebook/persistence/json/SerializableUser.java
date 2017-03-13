package phonebook.persistence.json;

import lombok.Data;
import phonebook.domain.Contact;
import phonebook.domain.User;

import java.util.HashMap;

/**
 * @author Konstantin Kosmachevskiy
 */
@Data
class SerializableUser extends User {
    private HashMap<Long, Contact> contacts = new HashMap<>();

    void putContact(Contact contact) {
        contacts.put(contact.getId(), contact);
    }

    @Override
    public String toString() {
        return "User{ username=" + getUserName() +
                ", contacts=" + contacts +
                '}';
    }
}
