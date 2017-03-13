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
public class ContactValidatorEmailTest {
    @Parameterized.Parameter(0)
    public int expected;
    @Parameterized.Parameter(1)
    public String input;
    private ContactValidator validator;
    private Errors errors;
    private Contact contact;

    @Parameterized.Parameters(name = "index = {index}; email = {1}")
    public static Collection primeNumbers() {
        return Arrays.asList(new Object[][]{
                {0, ""},
                {0, null},
                {0, "test@test.test"},
                {0, "123@test.test"},
                {1, "test@testtest"},
                {1, "testtest.test"},
                {2, "QWERTYUIOPKJHJHKJHKHLHDFJLKDHFJKHDKFSDFGHJKLXCVBNMf"},
                {1, "test/@test.test"},
                {1, "/test@test.test"},
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
        contact.setEmail(input);

        validator.validate(contact, errors);

        Assert.assertEquals(expected, errors.getFieldErrors("email").size());
    }
}
