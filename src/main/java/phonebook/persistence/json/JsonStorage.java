package phonebook.persistence.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phonebook.domain.Contact;
import phonebook.domain.User;
import phonebook.persistence.ContactRepository;
import phonebook.persistence.UserRepository;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Konstantin Kosmachevskiy
 */
public class JsonStorage implements ContactRepository, UserRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonStorage.class);
    private static final String FILE_NAME = "database.json";
    private ObjectMapper mapper = new ObjectMapper();
    private File file;
    // Serializable data //
    private long nextId;
    private HashMap<String, SerializableUser> users;
    // Serializable data //

    public JsonStorage(String path) {
        this.file = new File(path + "/" + FILE_NAME);
    }

    public JsonStorage(File file) {
        this.file = file;
    }

    public JsonStorage() {
    }

    private long nextId() {
        return ++nextId;
    }

    public long getNextId() {
        return nextId;
    }

    public void setNextId(long nextId) {
        this.nextId = nextId;
    }

    public HashMap<String, SerializableUser> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, SerializableUser> users) {
        this.users = users;
    }

    void init() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        // Init fields //
        try {
            JsonStorage jsonStorage = mapper.readValue(file, JsonStorage.class);
            nextId = jsonStorage.getNextId();
            users = jsonStorage.getUsers();

            LOGGER.debug("Data from file {} loaded", file.getCanonicalPath());
        } catch (IOException e) {
            nextId = 0;
            users = new HashMap<>();

            LOGGER.debug("Error reading from file. " + e.getMessage());
        }
    }

    private void update() {
        LOGGER.debug("JsonStorage updated.");
        try {
            mapper.writeValue(file, this);
        } catch (IOException e) {
            LOGGER.error("Error file writing. " + e.getMessage());
        }
    }

    @Override
    public List<Contact> getAllByUser(User user) {
        SerializableUser serializableUser = users.get(user.getUserName());
        Collection<Contact> values = serializableUser.getContacts().values();
        if (!values.isEmpty())
            return new ArrayList<>(values);
        return new ArrayList();
    }

    @Override
    public void addToUser(User user, Contact contact) {
        if (users.containsKey(user.getUserName())) {
            SerializableUser serializableUser = users.get(user.getUserName());

            // Prepare contact //
            contact.setId(nextId());
            if (contact.getHomePhoneNumber() == null)
                contact.setHomePhoneNumber("");

            serializableUser.putContact(contact);
            update();
        }
    }

    @Override
    public void removeFromUser(User user, Contact contact) {
        SerializableUser serializableUser = users.get(user.getUserName());
        serializableUser.getContacts().remove(contact.getId(), contact);
        update();
    }

    @Override
    public void update(User user, Contact contact) {
        SerializableUser su = users.get(user.getUserName());
        su.putContact(contact);
        update();
    }

    @Override
    public List<Contact> findByNamesAndPhones(User user, String pattern) {
        final String p = pattern.toLowerCase();

        List<Contact> collect = users.get(user.getUserName()).getContacts().values().parallelStream()
                .filter(contact ->
                        contact.getFirstName().toLowerCase().contains(p) ||
                                contact.getLastName().toLowerCase().contains(p) ||
                                contact.getMiddleName().toLowerCase().contains(p) ||
                                contact.getMobilePhoneNumber().toLowerCase().contains(p) ||
                                contact.getHomePhoneNumber().toLowerCase().contains(p))
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public Optional<User> getByUserName(String userName) {
        return Optional.ofNullable(users.get(userName));
    }

    @Override
    public void add(User user) {
        if (users.containsKey(user.getUserName()))
            return;

        SerializableUser serializableUser = new SerializableUser();

        serializableUser.setId(nextId());
        serializableUser.setUserName(user.getUserName());
        serializableUser.setPassword(user.getPassword());
        serializableUser.setFullName(user.getFullName());

        users.put(serializableUser.getUserName(), serializableUser);

        user.setId(serializableUser.getId()); // Copy ID  to original object //

        update();
    }

}
