package phonebook.presentation.errors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents {@link  org.springframework.validation.FieldError} as {@link java.util.Map}
 *
 * @author Konstantin Kosmachevskiy
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Errors extends HashMap<String, List<String>> {

    public void addFieldError(String field, String message) {
        if (containsKey(field)) {
            get(field).add(message);
        } else {
            ArrayList<String> messages = new ArrayList<>();
            messages.add(message);
            put(field, messages);
        }
    }
}