package phonebook.domain.validator;

import org.junit.Assert;
import org.junit.Test;
import phonebook.domain.User;

/**
 * @author Konstantin Kosmachevskiy
 */
public class UserValidatorTest {

    public static User createValidStub() {
        User user = new User();
        user.setFullName("Full Name");
        user.setPassword("password");
        user.setConfirmPassword("password");
        user.setUserName("userName");
        return user;
    }

    @Test
    public void testSupports() throws Exception {
        Assert.assertTrue(new UserValidator().supports(User.class));
    }
}