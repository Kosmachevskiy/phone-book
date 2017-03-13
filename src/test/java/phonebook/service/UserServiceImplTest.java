package phonebook.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import phonebook.domain.User;
import phonebook.domain.validator.UserValidatorTest;
import phonebook.persistence.UserRepository;

import static org.mockito.Mockito.*;

/**
 * @author Konstantin Kosmachevskiy
 */
public class UserServiceImplTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private BCryptPasswordEncoder encoder;
    @Mock
    private UserRepository userRepository;
    private User user;

    @Before
    public void setUp() throws Exception {
        userService = new UserServiceImpl();
        MockitoAnnotations.initMocks(this);

        user = UserValidatorTest.createValidStub();
    }

    @Test
    public void getByUserName() throws Exception {
        userService.getByUserName(user.getUserName());
        verify(userRepository, atLeastOnce()).getByUserName(user.getUserName());
    }

    @Test
    public void add() throws Exception {
        String rawPass = user.getPassword();
        String encodedPas = "!@#$%^&*(";

        when(encoder.encode(rawPass)).thenReturn(encodedPas);

        userService.add(user);
        verify(encoder, atLeastOnce()).encode(rawPass);
        verify(userRepository, atLeastOnce()).add(user);
    }

}