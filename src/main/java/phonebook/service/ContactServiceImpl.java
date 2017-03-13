package phonebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phonebook.domain.Contact;
import phonebook.domain.User;
import phonebook.persistence.ContactRepository;
import phonebook.util.SearchUtil;

import java.util.List;
import java.util.Optional;

/**
 * @author Konstantin Kosmachevskiy
 */
@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public List<Contact> getAllByUser(User user) {
        return contactRepository.getAllByUser(user);
    }

    @Override
    public void addToUser(User user, Contact contact) {
        contactRepository.addToUser(user, contact);
    }

    @Override
    public void removeFromUser(User user, Contact contact) {
        contactRepository.removeFromUser(user, contact);
    }

    @Override
    public void update(User user, Contact contact) {
        contactRepository.update(user, contact);
    }

    @Override
    public List<Contact> findByNamesAndPhones(User user, String pattern) {
        Optional<String> s = SearchUtil.checkAndTrimIfPhoneNumber(pattern);
        if (s.isPresent())
            pattern = s.get();
        else
            pattern = SearchUtil.removeSpaces(pattern);
        return contactRepository.findByNamesAndPhones(user, pattern);
    }
}
