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
import phonebook.domain.User;
import phonebook.persistence.UserRepository;

import java.sql.*;
import java.util.Optional;

/**
 * @author Konstantin Kosmachevskiy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MySqlTestConfig.class})
@ActiveProfiles("test-mysql")
@Ignore
public class MySqlUserRepositoryTest {

    @Value("${database.url}")
    private String url;
    @Value("${database.username}")
    private String name;
    @Value("${database.password}")
    private String password;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() throws SQLException, ClassNotFoundException {
        ScriptUtils.executeSqlScript(getConnection(), new ClassPathResource("reset-database.sql"));
    }

    @Test
    public void getByUserName() throws Exception {
        ScriptUtils.executeSqlScript(getConnection(), new ClassPathResource("test-data.sql"));

        Optional<User> userOptional = userRepository.getByUserName("user2");
        Assert.assertTrue(userOptional.isPresent());

        User user = userOptional.get();
        Assert.assertEquals("user2", user.getUserName());
        Assert.assertEquals("pass2", user.getPassword());
        Assert.assertEquals("fullName2", user.getFullName());
    }

    @Test
    public void add() throws Exception {
        User user = new User();
        user.setPassword("pass");
        user.setUserName("user");
        user.setFullName("name");

        userRepository.add(user);

        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users;");

        Assert.assertTrue(resultSet.next());
        Assert.assertEquals(1L, resultSet.getLong("id"));
        Assert.assertEquals("user", resultSet.getString("username"));
        Assert.assertEquals("pass", resultSet.getString("password"));
        Assert.assertEquals("name", resultSet.getString("full_name"));

        Assert.assertFalse(resultSet.next());

        statement.close();
        connection.close();
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, name, password);
    }

}