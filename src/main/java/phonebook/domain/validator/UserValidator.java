package phonebook.domain.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import phonebook.domain.User;

/**
 * @author Konstantin Kosmachevskiy
 */
@Component
public class UserValidator implements Validator {
    private static final String LOGIN_PATTERN = "^[a-zA-Z]+$";

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        validateUserName(user, errors);
        validatePassword(user, errors);
        validateIsPasswordsMatch(user, errors);
        validateFullName(user, errors);
    }

    void validateUserName(User user, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "required");

        if (user.getUserName() != null && user.getUserName().length() < 3)
            errors.rejectValue("userName", "userName.toShort");

        if (user.getUserName() != null && !user.getUserName().matches(LOGIN_PATTERN))
            errors.rejectValue("userName", "userName.incorrect");

        if (user.getUserName() != null && user.getUserName().length() > 50)
            errors.rejectValue("userName", "tooLong");

    }

    void validatePassword(User user, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required");

        if (user.getPassword() != null && user.getPassword().length() < 5)
            errors.rejectValue("password", "password.tooShort");
    }

    void validateIsPasswordsMatch(User user, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "required");

        if (user.getConfirmPassword() != null && !user.getPassword().equals(user.getConfirmPassword()))
            errors.rejectValue("confirmPassword", "confirmPassword.doNotMatch");
    }

    void validateFullName(User user, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullName", "required");

        if (user.getFullName() != null && user.getFullName().length() < 5)
            errors.rejectValue("fullName", "fullName.tooShort");

        if (user.getFullName() != null && user.getFullName().length() > 50)
            errors.rejectValue("fullName", "tooLong");
    }

}
