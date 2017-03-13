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
public class UserValidatorConfirmPasswordTest {
    @Parameterized.Parameter(0)
    public int expected;
    @Parameterized.Parameter(1)
    public String input;
    private UserValidator validator;
    private Errors errors;
    private User user;

    @Parameterized.Parameters(name = "confirmPassword = {1}")
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                {0, "password"},
                {1, "anotherPassword"},
                {1, null},
                {2, ""},
                {2, "   "},
        });
    }

    @Before
    public void setUp() throws Exception {
        validator = new UserValidator();
        user = new User();
        user.setPassword("password");
        errors = new BeanPropertyBindingResult(user, "user");
    }

    @Test
    public void test() throws Exception {
        user.setConfirmPassword(input);
        validator.validate(user, errors);
        Assert.assertEquals(expected, errors.getFieldErrors("confirmPassword").size());
    }
}
