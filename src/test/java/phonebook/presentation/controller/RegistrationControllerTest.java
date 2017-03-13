package phonebook.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import phonebook.domain.User;
import phonebook.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Konstantin Kosmachevskiy
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RegistrationControllerTest {

    static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    private User user;

    @Autowired
    private UserService userService;

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.setProperty("lardi.conf", "./src/test/resources/test.properties");
    }

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setUserName("username");
        user.setPassword("password");
        user.setConfirmPassword("password");
        user.setFullName("full name");
    }

    @Test
    public void testUserExists() throws Exception {

        userService.add(user);

        mockMvc.
                perform(post("/registration").contentType("application/json")
                        .content(MAPPER.writeValueAsString(user).getBytes()))
                .andExpect(status().is(422));
    }

    @Test
    public void testRegistration() throws Exception {
        mockMvc.
                perform(post("/registration").contentType("application/json")
                        .content(MAPPER.writeValueAsString(user).getBytes()))
                .andExpect(status().is(201));
    }

    @Test
    public void registrationError() throws Exception {
        user.setConfirmPassword("");

        mockMvc.
                perform(post("/registration").contentType("application/json")
                        .content(MAPPER.writeValueAsString(user).getBytes()))
                .andExpect(status().is(422));

    }
}