package phonebook.domain.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import phonebook.domain.Contact;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Konstantin Kosmachevskiy
 */
@RunWith(Parameterized.class)
public class ContactValidatorMobilePhoneTest {
    @Parameterized.Parameter(0)
    public int expected;
    @Parameterized.Parameter(1)
    public String input;
    private ContactValidator validator;
    private Errors errors;
    private Contact contact;

    @Parameterized.Parameters(name = "index = {index}; input = {1}")
    public static Collection primeNumbers() {
        return Arrays.asList(new Object[][]{
                {1, null},
                {1, ""},
                {1, "   "},
                {0, "+38012 345 67 89"},
        });
    }

    @Before
    public void setUp() throws Exception {
        validator = new ContactValidator();
        contact = new Contact();
        errors = new BeanPropertyBindingResult(contact, "contact");
    }

    @Test
    public void test() throws Exception {
        contact.setMobilePhoneNumber(input);
        validator.validate(contact, errors);

        Assert.assertEquals(expected, errors.getFieldErrors("mobilePhoneNumber").size());
    }
}
