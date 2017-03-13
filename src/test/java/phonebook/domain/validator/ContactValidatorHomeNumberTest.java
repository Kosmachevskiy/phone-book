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
public class ContactValidatorHomeNumberTest {
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
                {0, null},
                {0, ""},
                {1, "   "},
                {1, "+38012 3!5 67 89"},
                {0, "+38012 345 67 89"},
                {0, "+38(01234)5-67-89"},
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
        contact.setHomePhoneNumber(input);
        validator.validate(contact, errors);

        Assert.assertEquals(expected, errors.getFieldErrors("homePhoneNumber").size());
    }
}