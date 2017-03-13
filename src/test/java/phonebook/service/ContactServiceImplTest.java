package phonebook.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import phonebook.domain.Contact;
import phonebook.domain.User;
import phonebook.domain.validator.ContactValidatorTest;
import phonebook.domain.validator.UserValidatorTest;
import phonebook.persistence.ContactRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @author Konstantin Kosmachevskiy
 */
public class ContactServiceImplTest {

    @Mock
    private ContactRepository contactRepository;
    @InjectMocks
    private ContactService contactService;
    private Contact contact;
    private User user;

    @Before
    public void setUp() throws Exception {
        contactService = new ContactServiceImpl();
        MockitoAnnotations.initMocks(this);

        user = UserValidatorTest.createValidStub();
        contact = ContactValidatorTest.createValidStub();
    }

    @Test
    public void getAllByUser() throws Exception {
        contactService.getAllByUser(user);
        Mockito.verify(contactRepository, Mockito.atLeastOnce()).getAllByUser(user);
    }

    @Test
    public void addToUser() throws Exception {
        contactService.addToUser(user, contact);
        Mockito.verify(contactRepository, Mockito.atLeastOnce()).addToUser(user, contact);
    }

    @Test
    public void removeFromUser() throws Exception {
        contactService.removeFromUser(user, contact);
        Mockito.verify(contactRepository, Mockito.atLeastOnce()).removeFromUser(user, contact);
    }

    @Test
    public void update() throws Exception {
        contactService.update(user, contact);
        Mockito.verify(contactRepository, Mockito.atLeastOnce()).update(user, contact);
    }

    @Test
    public void findByNamesAndPhones() throws Exception {
        when(contactRepository.findByNamesAndPhones(user, "pattern")).thenReturn(Arrays.asList(contact, contact));

        List<Contact> contacts = contactService.findByNamesAndPhones(user, "pattern");

        Mockito.verify(contactRepository, Mockito.atLeastOnce()).findByNamesAndPhones(user, "pattern");
        Assert.assertEquals(Arrays.asList(contact, contact), contacts);
    }

}