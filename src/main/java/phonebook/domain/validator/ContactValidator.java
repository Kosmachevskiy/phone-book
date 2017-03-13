package phonebook.domain.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import phonebook.domain.Contact;

/**
 * @author Konstantin Kosmachevskiy
 */
@Component
public class ContactValidator implements Validator {

    private static final String EMAIL_TEMPLATE = "^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$";
    private static final String PHONE_TEMPLATE_ONLY_DIGITS = "^\\d+$";
    private static final String PHONE_TEMPLATE_COMMON_CHARS = "[\\—\\-\\(\\)\\ \\+]";
    private static final String UA_CODE = "380";
    private static final String UA_INNER_CODE_1 = "80";
    private static final String UA_INNER_CODE_2 = "0";
    private static final int FULL_NUMBER_LENGTH = 12;

    public static String trimAndValidatePhoneNumber(String number) throws PhoneNumberFormatException {

        number = number.replaceAll(PHONE_TEMPLATE_COMMON_CHARS, "");

        if (!number.matches(PHONE_TEMPLATE_ONLY_DIGITS))
            throw new PhoneNumberFormatException();

        if (number.startsWith(UA_CODE) && number.length() == FULL_NUMBER_LENGTH)
            return "+" + number;


        if (number.startsWith(UA_INNER_CODE_1) && number.length() == (FULL_NUMBER_LENGTH - 1))
            return "+3" + number;


        if (number.startsWith(UA_INNER_CODE_2) && number.length() == (FULL_NUMBER_LENGTH - 2))
            return "+38" + number;

        throw new PhoneNumberFormatException();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Contact.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Contact contact = (Contact) o;

        validateFirstName(contact, errors);
        validateMiddleName(contact, errors);
        validateLastName(contact, errors);
        validateMobilePhoneNumber(contact, errors);
        validateHomePhoneNumber(contact, errors);
        validateEmail(contact, errors);
        validateAddress(contact, errors);
    }

    private void validateAddress(Contact contact, Errors errors) {
        if (contact.getAddress() != null && contact.getAddress().length() > 50)
            errors.rejectValue("address", "tooLong");
    }

    private void validateHomePhoneNumber(Contact contact, Errors errors) {
        String homePhoneNumber = contact.getHomePhoneNumber();

        if (homePhoneNumber != null && !homePhoneNumber.isEmpty()) {
            try {
                contact.setHomePhoneNumber(trimAndValidatePhoneNumber(homePhoneNumber));
            } catch (PhoneNumberFormatException e) {
                errors.rejectValue("homePhoneNumber", "contact.homePhoneNumber.invalid");
            }
        }
    }

    private void validateFirstName(Contact contact, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "required");
        if (contact.getFirstName() != null && contact.getFirstName().length() < 4)
            errors.rejectValue("firstName", "contact.firstName.tooShort");

        if (contact.getFirstName() != null && contact.getFirstName().length() > 50)
            errors.rejectValue("firstName", "tooLong");

    }

    private void validateMiddleName(Contact contact, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "middleName", "required");
        if (contact.getMiddleName() != null && contact.getMiddleName().length() < 4)
            errors.rejectValue("middleName", "contact.middleName.tooShort");

        if (contact.getMiddleName() != null && contact.getMiddleName().length() > 50)
            errors.rejectValue("middleName", "tooLong");

    }

    private void validateLastName(Contact contact, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "required");

        if (contact.getLastName() != null && contact.getLastName().length() < 4)
            errors.rejectValue("lastName", "contact.lastName.tooShort");

        if (contact.getLastName() != null && contact.getLastName().length() > 50)
            errors.rejectValue("lastName", "tooLong");
    }

    private void validateMobilePhoneNumber(Contact contact, Errors errors) {

        String mobilePhoneNumber = contact.getMobilePhoneNumber();

        if (mobilePhoneNumber == null || mobilePhoneNumber.isEmpty()) {
            errors.rejectValue("mobilePhoneNumber", "required");
            return;
        }

        try {
            contact.setMobilePhoneNumber(trimAndValidatePhoneNumber(mobilePhoneNumber));
        } catch (PhoneNumberFormatException e) {
            errors.rejectValue("mobilePhoneNumber", "contact.mobilePhoneNumber.invalid");
        }
    }

    private void validateEmail(Contact contact, Errors errors) {
        String email = contact.getEmail();

        if (email == null || email.isEmpty())
            return;

        if (contact.getEmail().length() > 50)
            errors.rejectValue("email", "tooLong");

        if (!email.matches(EMAIL_TEMPLATE))
            errors.rejectValue("email", "contact.email.invalid");
    }


    public static class PhoneNumberFormatException extends RuntimeException {

    }


}
