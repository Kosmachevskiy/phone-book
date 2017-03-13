package phonebook.presentation.errors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Locale;

/**
 * @author Konstantin Kosmachevskiy
 */
@Component
public class ErrorsMapper {

    @Autowired
    private ResourceBundleMessageSource messageSource;

    public Errors map(List<FieldError> fieldErrors) {
        Errors errors = new Errors();
        for (FieldError fieldError : fieldErrors)
            errors.addFieldError(fieldError.getField(),
                    messageSource.getMessage(fieldError.getCode(), null, Locale.getDefault()));
        return errors;
    }
}
