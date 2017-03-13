package phonebook.domain.validator;

import org.junit.Assert;
import org.junit.Test;
import phonebook.domain.Contact;

import static org.junit.Assert.fail;

/**
 * @author Konstantin Kosmachevskiy
 */
public class ContactValidatorTest {

    public static Contact createValidStub() {
        Contact contact = new Contact();
        contact.setFirstName("FirstName");
        contact.setLastName("LastName");
        contact.setMiddleName("MiddleName");
        contact.setMobilePhoneNumber("+380123456789");
        contact.setHomePhoneNumber("+380123456789");
        contact.setEmail("test@test.test");
        return contact;
    }

    @Test
    public void testSupports() throws Exception {
        Assert.assertTrue(new ContactValidator().supports(Contact.class));
    }

    @Test
    public void trimAndValidatePhoneNumber() throws Exception {
        String expected = "+380123456789";

        Assert.assertEquals(expected, ContactValidator.trimAndValidatePhoneNumber("+38(012)3456789"));
        Assert.assertEquals(expected, ContactValidator.trimAndValidatePhoneNumber("+38(012)345 67 89"));
        Assert.assertEquals(expected, ContactValidator.trimAndValidatePhoneNumber("+38(012)345-67—89"));
        Assert.assertEquals(expected, ContactValidator.trimAndValidatePhoneNumber("+38 012)345-67—89"));
        Assert.assertEquals(expected, ContactValidator.trimAndValidatePhoneNumber("38 012)345-67—89"));
        Assert.assertEquals(expected, ContactValidator.trimAndValidatePhoneNumber("8 012)345-67—89"));
        Assert.assertEquals(expected, ContactValidator.trimAndValidatePhoneNumber(" 012)345-67—89"));

        try {
            ContactValidator.trimAndValidatePhoneNumber("38012345678");
            fail("Too short");
        } catch (ContactValidator.PhoneNumberFormatException e) {
            Assert.assertEquals(ContactValidator.PhoneNumberFormatException.class, e.getClass());
        }

        try {
            ContactValidator.trimAndValidatePhoneNumber("+3801234567890");
            fail("Too long number.");
        } catch (ContactValidator.PhoneNumberFormatException e) {
            Assert.assertEquals(ContactValidator.PhoneNumberFormatException.class, e.getClass());
        }

        try {
            ContactValidator.trimAndValidatePhoneNumber("+38012345678!0");
            fail("It should be only digits.");
        } catch (ContactValidator.PhoneNumberFormatException e) {
            Assert.assertEquals(ContactValidator.PhoneNumberFormatException.class, e.getClass());
        }
    }

}