package phonebook.presentation.errors;

import org.springframework.validation.Errors;

/**
 * @author Konstantin Kosmachevskiy
 */
public class InvalidRequestException extends RuntimeException {
    private Errors errors;

    public InvalidRequestException(String message, Errors errors) {
        super(message);
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}