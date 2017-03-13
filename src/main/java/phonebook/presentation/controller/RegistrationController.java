package phonebook.presentation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import phonebook.domain.User;
import phonebook.domain.validator.UserValidator;
import phonebook.presentation.errors.InvalidRequestException;
import phonebook.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Konstantin Kosmachevskiy
 */
@Controller
public class RegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;

    void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    public void registration(@RequestBody User user,
                             BindingResult bindingResult,
                             UserValidator validator,
                             HttpServletResponse response,
                             HttpSession httpSession) {

        LOGGER.debug("Create user request with name {} ", user.getUserName());

        // Validation //
        validator.validate(user, bindingResult);

        if (userService.getByUserName(user.getUserName()).isPresent())
            bindingResult.rejectValue("userName", "userName.exists");

        if (bindingResult.hasErrors())
            throw new InvalidRequestException("User data invalid", bindingResult);

        // Handling //
        String password = user.getPassword(); // Backup not encrypted  password //
        userService.add(user); // Save new user //
        login(user.getUserName(), password, httpSession); // And authenticate //
    }

    private void login(String name, String pass, HttpSession httpSession) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(name);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, pass, userDetails.getAuthorities());

        authenticationManager.authenticate(authenticationToken);

        if (authenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            httpSession.setAttribute("user", userService.getByUserName(name).get());
            LOGGER.debug("User '{}' logged in", name);
        }
    }

}
