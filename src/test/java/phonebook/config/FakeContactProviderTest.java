package phonebook.config;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import phonebook.domain.Contact;
import phonebook.domain.validator.ContactValidator;

/**
 * @author Konstantin Kosmachevskiy
 */
public class FakeContactProviderTest {
    @Test
    public void getContact() throws Exception {
        Contact contact = DemoConfig.FakeContactProvider.getContact();

        ContactValidator validator = new ContactValidator();
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(contact, "contact");

        validator.validate(contact, result);
        if (result != null)
            Assert.assertFalse(result.hasErrors());
    }
}