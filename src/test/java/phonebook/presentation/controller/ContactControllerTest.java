package phonebook.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import phonebook.domain.Contact;
import phonebook.domain.User;
import phonebook.domain.validator.ContactValidatorTest;
import phonebook.presentation.errors.Errors;
import phonebook.service.ContactService;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Konstantin Kosmachevskiy
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ContactControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactControllerTest.class);

    private User user;
    private List<Contact> contacts;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ContactService contactService;

    @Autowired
    @InjectMocks
    private ContactController contactController;

    @BeforeClass
    public static void setUp() throws Exception {
        System.setProperty("lardi.conf", "./src/test/resources/test.properties");
    }

    @Before
    public void init() throws Exception {

        user = new User();
        user.setId(1);
        user.setPassword("pass");
        user.setUserName("input");
        user.setUserName("FullName");

        Contact contact1 = new Contact();
        Contact contact2 = new Contact();

        contact1.setId(1);
        contact2.setId(2);

        contacts = new LinkedList<>();
        contacts.add(contact1);
        contacts.add(contact2);
    }

    @Test
    @WithMockUser
    public void testGetAll() throws Exception {
        when(contactService.getAllByUser(user)).thenReturn(contacts);

        MvcResult result = mockMvc.perform(get("/api/contacts").sessionAttr("user", this.user))
                .andExpect(status().isOk()).andReturn();

        verify(contactService, times(1)).getAllByUser(this.user);

        // Check response. It should be JSON array
        String response = result.getResponse().getContentAsString();
        Contact[] contacts = MAPPER.readValue(response, Contact[].class);
        Assert.assertArrayEquals(this.contacts.toArray(), contacts);
    }

    @Test
    @WithMockUser
    public void testGetAllByPattern() throws Exception {
        when(contactService.findByNamesAndPhones(user, "654")).thenReturn(contacts);

        MvcResult result = mockMvc.perform(get("/api/contacts/find").sessionAttr("user", this.user)
                .param("pattern", "654"))
                .andExpect(status().isOk()).andReturn();

        verify(contactService, times(1)).findByNamesAndPhones(this.user, "654");

        // Check response. It should be JSON array
        String response = result.getResponse().getContentAsString();
        Contact[] contacts = MAPPER.readValue(response, Contact[].class);
        Assert.assertArrayEquals(this.contacts.toArray(), contacts);
    }

    @Test
    @WithMockUser
    public void testUpdate() throws Exception {
        Contact modifiedContact = ContactValidatorTest.createValidStub();

        mockMvc.perform(
                put("/api/contacts")
                        .sessionAttr("user", this.user)
                        .contentType("application/json")
                        .content(MAPPER.writeValueAsString(modifiedContact).getBytes())
                        .with(csrf()))
                .andExpect(status().is(202));

        verify(contactService, times(1)).update(user, modifiedContact);
    }

    @Test
    @WithMockUser
    public void testAddValidContact() throws Exception {
        Contact contact = ContactValidatorTest.createValidStub();

        mockMvc.perform(
                post("/api/contacts")
                        .sessionAttr("user", this.user)
                        .contentType("application/json")
                        .content(MAPPER.writeValueAsString(contact).getBytes())
                        .with(csrf()))
                .andExpect(status().is(201));

        verify(contactService, times(1)).addToUser(user, contact);
    }

    @Test
    @WithMockUser
    public void testAddInvalidContact() throws Exception {
        Contact contact = ContactValidatorTest.createValidStub();
        contact.setFirstName("...");
        contact.setEmail("^*(");

        MvcResult result = mockMvc.perform(
                post("/api/contacts")
                        .sessionAttr("user", this.user)
                        .contentType("application/json")
                        .content(MAPPER.writeValueAsString(contact).getBytes())
                        .with(csrf()))
                .andExpect(status().is(422)).andReturn();

        Errors errors =
                MAPPER.readValue(result.getResponse().getContentAsByteArray(), Errors.class);

        Assert.assertNotNull(errors);

        LOGGER.debug("Response: " + errors);

        verify(contactService, times(0)).addToUser(this.user, contact);
    }

    @Test
    @WithMockUser
    public void testDelete() throws Exception {
        Contact contact = new Contact();
        contact.setId(2);

        mockMvc.perform(
                delete("/api/contacts")
                        .sessionAttr("user", this.user)
                        .contentType("application/json")
                        .content(MAPPER.writeValueAsString(contact).getBytes())
                        .with(csrf()))
                .andExpect(status().is(200));

        verify(contactService, times(1)).removeFromUser(user, contact);
    }

}