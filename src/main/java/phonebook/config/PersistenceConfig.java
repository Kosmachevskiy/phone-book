package phonebook.config;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import phonebook.persistence.ContactRepository;
import phonebook.persistence.UserRepository;
import phonebook.persistence.json.JsonStorage;
import phonebook.persistence.mysql.MySqlContactRepository;
import phonebook.persistence.mysql.MySqlUserRepository;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Konstantin Kosmachevskiy
 */

@Configuration
@PropertySource("file:${lardi.conf}")
public class PersistenceConfig {

    private static final String JDBC_PREFIX = "jdbc:mysql://";
    private static final String JDBC_DRIVE_NAME = "com.mysql.cj.jdbc.Driver";

    @Value("${storage.mysql.url}")
    private String databaseUrl;

    @Value("${storage.mysql.username}")
    private String username;

    @Value("${storage.mysql.password}")
    private String password;

    @Value("${storage.json.path}")
    private String jsonStoragePath;

    @Value("${storage.mysql.url.postfix}")
    private String urlPostfix;

    @Bean(value = "dataSource", autowire = Autowire.BY_NAME)
    @Profile("mysql")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        String url;

        if (!databaseUrl.startsWith("jdbc:mysql://"))
            url = JDBC_PREFIX + databaseUrl + urlPostfix;
        else
            url = databaseUrl + urlPostfix;

        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(JDBC_DRIVE_NAME);

        Properties p = new Properties();
        p.setProperty("useUnicode", "true");
        p.setProperty("characterEncoding", "utf8");

        dataSource.setConnectionProperties(p);

        return dataSource;
    }

    @Bean
    @Profile("mysql")
    public UserRepository userRepository() {
        MySqlUserRepository mySqlUserRepository = new MySqlUserRepository();
        mySqlUserRepository.setDataSource(dataSource());
        return mySqlUserRepository;
    }

    @Bean
    @Profile("mysql")
    public ContactRepository contactsRepository() {
        MySqlContactRepository repository = new MySqlContactRepository();
        repository.setDataSource(dataSource());
        return repository;
    }

    @Bean(autowire = Autowire.BY_NAME, name = {"contactRepository", "userRepository"}, initMethod = "init")
    @Profile("json")
    public JsonStorage storage() {
        return new JsonStorage(jsonStoragePath);
    }

}
