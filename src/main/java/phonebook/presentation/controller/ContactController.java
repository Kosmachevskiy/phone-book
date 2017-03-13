package phonebook.presentation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import phonebook.domain.Contact;
import phonebook.domain.User;
import phonebook.domain.validator.ContactValidator;
import phonebook.presentation.errors.InvalidRequestException;
import phonebook.service.ContactService;

import java.util.List;

/**
 * @author Konstantin Kosmachevskiy
 */
@RestController
@RequestMapping("/api")
public class ContactController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;
    @Autowired
    private ContactValidator contactValidator;

    @RequestMapping(path = {"/contacts"}, method = RequestMethod.GET)
    public List<Contact> getAll(@SessionAttribute User user) {
        LOGGER.debug("User {} reading all contacts", user.getUserName());
        return contactService.getAllByUser(user);
    }

    @RequestMapping(path = {"/contacts/find"}, method = RequestMethod.GET)
    public List<Contact> getAllByPattern(@SessionAttribute User user, @RequestParam String pattern) {
        LOGGER.debug("User {} searching all contacts by '{}'", user.getUserName(), pattern);
        return contactService.findByNamesAndPhones(user, pattern);
    }

    @RequestMapping(path = {"/contacts"}, method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void update(@SessionAttribute User user, @RequestBody Contact contact, BindingResult result) {
        LOGGER.debug("User {} updating contact {}", user.getUserName(), contact);

        contactValidator.validate(contact, result);
        if (result.hasErrors())
            throw new InvalidRequestException("Contact data invalid. ", result);

        contactService.update(user, contact);
    }

    @RequestMapping(path = {"/contacts"}, method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@SessionAttribute User user, @RequestBody Contact contact, BindingResult result) {
        LOGGER.debug("User {} adding contact {}", user.getUserName(), contact);

        contactValidator.validate(contact, result);
        if (result.hasErrors())
            throw new InvalidRequestException("Contact data invalid. ", result);

        contactService.addToUser(user, contact);
    }

    @RequestMapping(path = {"/contacts"}, method = RequestMethod.DELETE)
    public void delete(@SessionAttribute User user, @RequestBody Contact contact) {
        LOGGER.debug("User {} removing contact {}", user.getUserName(), contact);
        contactService.removeFromUser(user, contact);
    }

    @RequestMapping(path = {"/contacts/{id}"}, method = RequestMethod.DELETE)
    public void delete(@SessionAttribute User user, @PathVariable long id) {
        LOGGER.debug("User {} removing contact with ID  = {}", user.getUserName(), id);
        Contact contact = new Contact();
        contact.setId(id);
        contactService.removeFromUser(user, contact);
    }
}
