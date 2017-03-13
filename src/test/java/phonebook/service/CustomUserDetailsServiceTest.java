package phonebook.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import phonebook.domain.User;

import java.util.Optional;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * @author Konstantin Kosmachevskiy
 */
public class CustomUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserDetailsService userDetailsService = new CustomUserDetailsService();
    private User user;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        user = new User();

        user.setId(1);
        user.setUserName("input");
        user.setPassword("pass");
    }

    @Test
    public void testLoadUserByUsername() throws Exception {
        when(userService.getByUserName("input")).thenReturn(Optional.ofNullable(user));

        UserDetails details = userDetailsService.loadUserByUsername("input");

        Mockito.verify(userService, Mockito.times(1)).getByUserName("input");
        Assert.assertEquals(user.getUserName(), details.getUsername());
        Assert.assertEquals(user.getPassword(), details.getPassword());
        Assert.assertArrayEquals(AuthorityUtils.createAuthorityList("USER").toArray(),
                details.getAuthorities().toArray());
    }

    @Test
    public void exceptionTest() throws Exception {

        when(userService.getByUserName("wrongName")).thenReturn(Optional.ofNullable(null));

        try {
            userDetailsService.loadUserByUsername("wrongName");
            fail();
        } catch (UsernameNotFoundException e) {
            Assert.assertEquals("Username \"wrongName\" not found.", e.getMessage());
        }

    }
}