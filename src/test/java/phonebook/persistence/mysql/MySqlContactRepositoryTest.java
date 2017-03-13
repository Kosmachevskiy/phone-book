package phonebook.persistence.mysql;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import phonebook.domain.Contact;
import phonebook.domain.User;
import phonebook.persistence.ContactRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * @author Konstantin Kosmachevskiy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MySqlTestConfig.class})
@ActiveProfiles("test-mysql")
@Ignore
public class MySqlContactRepositoryTest {

    @Value("${database.url}")
    private String url;
    @Value("${database.username}")
    private String name;
    @Value("${database.password}")
    private String password;

    @Autowired
    private ContactRepository repository;

    @Before
    public void setUp() throws Exception {
        ScriptUtils.executeSqlScript(getConnection(), new ClassPathResource("reset-database.sql"));
        ScriptUtils.executeSqlScript(getConnection(), new ClassPathResource("test-data.sql"));
    }

    @Test
    public void testGetAllByUser() throws Exception {
        User user = new User();
        List<Contact> contacts;

        // Test is we get right set //
        user.setId(1L);
        contacts = repository.getAllByUser(user);
        Assert.assertEquals(2, contacts.size());


        user.setId(2L);
        contacts = repository.getAllByUser(user);
        Assert.assertEquals(3, contacts.size());

        // Test order and content //
        Assert.assertEquals("lastName_2_1", contacts.get(0).getLastName());
        Assert.assertEquals("lastName_2_2", contacts.get(1).getLastName());
        Assert.assertEquals("lastName_2_3", contacts.get(2).getLastName());
    }

    @Test
    public void testAddToUser() throws Exception {
        User user = new User();
        user.setId(1L);

        Contact expected = buildExpectedContact();

        int entriesCount = getEntriesCountForUserWithID(1);
        Assert.assertEquals("Should be 2 contacts by default", 2, entriesCount);

        repository.addToUser(user, expected); // Calling //

        entriesCount = getEntriesCountForUserWithID(1);
        Assert.assertEquals("Should be 2 contacts by default", 3, entriesCount);


        ResultSet resultSet = getConnection().createStatement()
                .executeQuery("SELECT * FROM contacts WHERE user_id = 1 AND first_name= 'firstName'");

        assertContact(user, expected, resultSet);

        resultSet.close();
    }

    private void assertContact(User user, Contact expected, ResultSet resultSet) throws SQLException {
        Assert.assertTrue(resultSet.next());
        Assert.assertEquals(expected.getLastName(), resultSet.getString("last_name"));
        Assert.assertEquals(expected.getMiddleName(), resultSet.getString("middle_name"));
        Assert.assertEquals(expected.getFirstName(), resultSet.getString("first_name"));
        Assert.assertEquals(expected.getAddress(), resultSet.getString("address"));
        Assert.assertEquals(expected.getHomePhoneNumber(), resultSet.getString("home_phone_number"));
        Assert.assertEquals(expected.getMobilePhoneNumber(), resultSet.getString("mobile_phone_number"));
        Assert.assertEquals(expected.getEmail(), resultSet.getString("email"));
        Assert.assertEquals(user.getId(), resultSet.getLong("user_id"));
    }

    private Contact buildExpectedContact() {
        Contact expected = new Contact();
        expected.setFirstName("firstName");
        expected.setLastName("lastName");
        expected.setMiddleName("middleName");
        expected.setMobilePhoneNumber("+380123456789");
        expected.setHomePhoneNumber("+380123456789");
        expected.setEmail("+380987654321");
        expected.setAddress("Address str.");
        return expected;
    }

    private int getEntriesCountForUserWithID(int u) throws SQLException, ClassNotFoundException {
        ResultSet set = getConnection().createStatement()
                .executeQuery("SELECT COUNT(*) FROM contacts WHERE user_id  = " + u);
        set.next();
        int entriesCount = set.getInt(1);
        set.close();
        return entriesCount;
    }

    @Test
    public void removeFromUser() throws Exception {
        User user = new User();
        user.setId(1l);

        Contact contact = new Contact();
        contact.setId(1l);

        repository.removeFromUser(user, contact);

        Assert.assertEquals(1, getEntriesCountForUserWithID(1));

        ResultSet resultSet = getConnection().createStatement()
                .executeQuery("SELECT * FROM contacts WHERE id = 1");

        Assert.assertFalse(resultSet.next());
        resultSet.close();
    }

    @Test
    public void update() throws Exception {
        User user = new User();
        user.setId(1l);

        Contact newAndExpected = buildExpectedContact();
        newAndExpected.setId(1l);

        repository.update(user, newAndExpected);

        ResultSet resultSet = getConnection().createStatement()
                .executeQuery("SELECT * FROM contacts WHERE id = 1 AND user_id = 1");

        assertContact(user, newAndExpected, resultSet);

        resultSet.close();
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Properties p = new Properties();
        p.setProperty("useUnicode", "true");
        p.setProperty("characterEncoding", "utf8");
        p.setProperty("user", name);
        p.setProperty("password", password);
        return DriverManager.getConnection(url, p);
    }


    @Test
    public void testFindByPattern() throws Exception {
        User user = new User();
        user.setId(2l);


        // Search bt all names //
        Assert.assertEquals(3, repository.findByNamesAndPhones(user, "ви").size());

        List<Contact> contacts = repository.findByNamesAndPhones(user, "вит");
        Assert.assertEquals(3, contacts.size());
        Assert.assertEquals("lastName_2_1", contacts.get(0).getLastName());
        Assert.assertEquals("lastName_2_2", contacts.get(1).getLastName());
        Assert.assertEquals("lastName_2_3", contacts.get(2).getLastName());

        contacts = repository.findByNamesAndPhones(user, "Виктор");
        Assert.assertEquals(2, contacts.size());
        Assert.assertEquals("lastName_2_1", contacts.get(0).getLastName());
        Assert.assertEquals("lastName_2_3", contacts.get(1).getLastName());

        Assert.assertEquals(1, repository.findByNamesAndPhones(user, "Робертович").size());
        Assert.assertEquals(1, repository.findByNamesAndPhones(user, "ame_2_1").size());


        // Search by phones //
        contacts = repository.findByNamesAndPhones(user, "678");
        Assert.assertEquals(2, contacts.size());
        Assert.assertEquals("lastName_2_1", contacts.get(0).getLastName());
        Assert.assertEquals("lastName_2_3", contacts.get(1).getLastName());

        contacts = repository.findByNamesAndPhones(user, "654");
        Assert.assertEquals(2, contacts.size());
        Assert.assertEquals("lastName_2_2", contacts.get(0).getLastName());
        Assert.assertEquals("lastName_2_3", contacts.get(1).getLastName());

        contacts = repository.findByNamesAndPhones(user, "777");
        Assert.assertEquals(1, contacts.size());
        Assert.assertEquals("lastName_2_2", contacts.get(0).getLastName());
    }
}