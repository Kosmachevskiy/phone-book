package phonebook.persistence.mysql;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import phonebook.persistence.ContactRepository;
import phonebook.persistence.UserRepository;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Konstantin Kosmachevskiy
 */
@Configuration
@PropertySource("classpath:database-test.properties")
@Profile("test-mysql")
public class MySqlTestConfig {

    @Value("${database.url}")
    private String url;
    @Value("${database.username}")
    private String name;
    @Value("${database.password}")
    private String password;

    @Bean
    public static PropertySourcesPlaceholderConfigurer
    propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(name);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");

        Properties p = new Properties();
        p.setProperty("useUnicode", "true");
        p.setProperty("characterEncoding", "utf8");

        dataSource.setConnectionProperties(p);

        return dataSource;
    }

    @Bean
    public UserRepository userRepository() {
        MySqlUserRepository repository = new MySqlUserRepository();
        repository.setDataSource(dataSource());
        return repository;
    }

    @Bean
    public ContactRepository contactsRepository() {
        MySqlContactRepository repository = new MySqlContactRepository();
        repository.setDataSource(dataSource());
        return repository;
    }
}
