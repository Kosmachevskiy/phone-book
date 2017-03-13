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
public class ContactValidatorNameTest {
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
                {2, ""},
                {1, "       "},
                {1, "Имя"},
                {1, null},
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
        contact.setFirstName(input);
        contact.setMiddleName(input);
        contact.setLastName(input);

        validator.validate(contact, errors);

        Assert.assertEquals(expected, errors.getFieldErrors("firstName").size());
        Assert.assertEquals(expected, errors.getFieldErrors("middleName").size());
        Assert.assertEquals(expected, errors.getFieldErrors("lastName").size());
    }
}
