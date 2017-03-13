package phonebook.domain.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import phonebook.domain.User;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Konstantin Kosmachevskiy
 */
@RunWith(Parameterized.class)
public class UserValidatorNameTest {
    @Parameterized.Parameter(0)
    public int expected;
    @Parameterized.Parameter(1)
    public String name;
    private UserValidator validator;
    private Errors errors;
    private User user;

    @Parameterized.Parameters(name = "index = {index}; userName = {1}")
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                {0, "qwe"},
                {0, "fFDdf"},
                {1, "qw"},
                {1, "1234"},
                {1, "fFDdfГ"},
                {1, "fFDdf1"},
                {1, "QWERTYUIOPKJHJHKJHKHLHDFJLKDHFJKHDKFSDFGHJKLXCVBNMf"},
                {3, ""},
                {1, null},
        });
    }

    @Before
    public void setUp() throws Exception {
        validator = new UserValidator();
        user = new User();
        errors = new BeanPropertyBindingResult(user, "user");
    }

    @Test
    public void test() throws Exception {
        user.setUserName(name);
        validator.validate(user, errors);
        Assert.assertEquals(expected, errors.getFieldErrors("userName").size());
    }
}
