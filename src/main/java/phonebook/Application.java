package phonebook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

@SpringBootApplication
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Throwable {

        Optional<String> profile = getProfile();

        if (profile.isPresent()) {
            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile.get());
            SpringApplication.run(Application.class, args);
        } else
            System.exit(1);
    }

    private static Optional<String> getProfile() {
        String pathToFile = "";

        try {
            pathToFile = System.getProperty("lardi.conf");

            Properties prop = new Properties();
            InputStream input = new FileInputStream(pathToFile);
            prop.load(input);

            return Optional.of(prop.getProperty("storage"));
        } catch (IOException | NullPointerException e) {
            LOGGER.error("Error reading properties file {} " + pathToFile);
            return Optional.empty();
        }
    }
}
