package phonebook.persistence.json;

import org.junit.Test;
import phonebook.domain.Contact;
import phonebook.domain.User;
import phonebook.persistence.ContactRepository;
import phonebook.persistence.UserRepository;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Konstantin Kosmachevskiy
 */
public class JsonStorageTest {
    private UserRepository userRepository;
    private ContactRepository contactRepository;
    private JsonStorage jsonStorage;

    {
        File file = new File("./database-test.json");
        file.delete();
        file.deleteOnExit();
        jsonStorage = new JsonStorage(file);
        jsonStorage.init();

        userRepository = jsonStorage;
        contactRepository = jsonStorage;
    }

    @Test
    public void testJsonStorage() throws Exception {

        // Add User and get by name //
        userRepository.add(new User("username_1"));
        Optional<User> userOptional = userRepository.getByUserName("username_1");
        assertTrue(userOptional.isPresent());
        assertEquals(1, userOptional.get().getId());

        userRepository.add(new User("username_2"));
        userOptional = userRepository.getByUserName("username_2");
        assertTrue(userOptional.isPresent());
        assertEquals(2, userOptional.get().getId());


        // Add contacts and get //
        User user = userRepository.getByUserName("username_1").get();
        contactRepository.addToUser(user, new Contact("Александр", "Александров"));
        contactRepository.addToUser(user, new Contact("Евген", "Евгенов"));

        List<Contact> contacts = contactRepository.getAllByUser(user);

        assertEquals(2, contacts.size());
        assertEquals("Александр", contacts.get(0).getFirstName());
        assertEquals("Евген", contacts.get(1).getFirstName());


        // Remove contact //
        contactRepository.removeFromUser(user, contacts.get(1));

        contacts = contactRepository.getAllByUser(user);
        assertEquals(1, contacts.size());
        assertEquals("Александр", contacts.get(0).getFirstName());


        // Update contact //
        Contact contact = contacts.get(0);
        contact.setMiddleName("Александрович");

        contactRepository.update(user, contact);

        contacts = contactRepository.getAllByUser(user);
        assertEquals("Александрович", contact.getMiddleName());


        // Find by pattern //
        contactRepository.removeFromUser(user, contact);
        addTestContacts(user, userRepository, contactRepository);

        contacts = contactRepository.findByNamesAndPhones(user, "НетТакого");
        assertEquals(0, contacts.size());

        contacts = contactRepository.findByNamesAndPhones(user, "Иванов");
        assertEquals("Should be match by middle name.", 2, contacts.size());

        contacts = contactRepository.findByNamesAndPhones(user, "алек");
        assertEquals("Should be match by last name and first name.", 2, contacts.size());

        contacts = contactRepository.findByNamesAndPhones(user, "етр");
        assertEquals("Should be match by first name and middle name.", 2, contacts.size());

        contacts = contactRepository.findByNamesAndPhones(user, "77");
        assertEquals(1, contacts.size());
        assertEquals("Петров", contacts.get(0).getLastName());

        contacts = contactRepository.findByNamesAndPhones(user, "+3");
        assertEquals(2, contacts.size());

        contacts = contactRepository.findByNamesAndPhones(user, "12345");
        assertEquals(2, contacts.size());
    }

    private void addTestContacts(User user, UserRepository userRepository, ContactRepository contactRepository) {
        Contact aleksandrov = new Contact();
        aleksandrov.setLastName("Александров");
        aleksandrov.setFirstName("Петр");
        aleksandrov.setMiddleName("Иванович");
        aleksandrov.setMobilePhoneNumber("+380123456789");
        aleksandrov.setHomePhoneNumber("+30987654321");

        Contact petrov = new Contact();
        petrov.setLastName("Петров");
        petrov.setFirstName("Александ");
        petrov.setMiddleName("Иванович");
        petrov.setMobilePhoneNumber("+380123456777");
        petrov.setHomePhoneNumber("+380123456789");

        Contact blank = new Contact();
        blank.setMiddleName("Something");
        blank.setLastName("Something");
        blank.setFirstName("Something");
        blank.setMobilePhoneNumber("Something");

        contactRepository.addToUser(user, aleksandrov);
        contactRepository.addToUser(user, petrov);
        contactRepository.addToUser(user, blank);

        List<Contact> contacts = contactRepository.getAllByUser(user);

        assertEquals(3, contacts.size());
    }

    @Test
    public void saveAndLoadTest() throws Exception {
        File file = new File("./database-test-temp.json");

        // Create storage //
        JsonStorage jsonStorage = new JsonStorage(file);
        jsonStorage.init();

        // Add data //
        jsonStorage.add(new User("user"));
        User user = jsonStorage.getByUserName("user").get();

        addTestContacts(user, jsonStorage, jsonStorage);

        // Backup data //
        HashMap<String, SerializableUser> expectedUsers = jsonStorage.getUsers();
        long expectedNextId = jsonStorage.getNextId();

        // Create new storage for same file //
        jsonStorage = new JsonStorage(file);
        jsonStorage.init();

        // Compare //
        assertEquals(expectedUsers, jsonStorage.getUsers());
        assertEquals(expectedNextId, jsonStorage.getNextId());
        file.deleteOnExit();
    }
}