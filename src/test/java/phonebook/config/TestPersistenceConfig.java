package phonebook.config;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import phonebook.persistence.json.JsonStorage;

import java.io.File;

/**
 * @author Konstantin Kosmachevskiy
 */
@Configuration
@Profile("test")
public class TestPersistenceConfig {

    @Bean(autowire = Autowire.BY_NAME, name = {"contactRepository", "userRepository"}, initMethod = "init")
    public JsonStorage storage() {
        File file = new File(".database-test.json");
        file.delete();
        file.deleteOnExit();
        return new JsonStorage(file);
    }
}
