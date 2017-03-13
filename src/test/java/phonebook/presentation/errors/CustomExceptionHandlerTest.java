package phonebook.presentation.errors;

import lombok.Data;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Konstantin Kosmachevskiy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan
@SpringBootTest
@ActiveProfiles("test")
public class CustomExceptionHandlerTest {

    public static final String PROP_NAME = "error.code";
    public static final String PROP_VALUE = "This is an error!";

    @Autowired
    ErrorsMapper mapper;
    @Autowired
    ResourceBundleMessageSource messageSource;

    BindingResult result;

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.setProperty("lardi.conf", "./src/test/resources/test.properties");
    }

    @Before
    public void setUp() throws Exception {
        messageSource.setBasename("test-messages");

        Object target = new SomeObject();
        result = new BeanPropertyBindingResult(target, "object");
        new SomeObjectValidator().validate(target, result);
    }

    @Test
    public void testAccessToProperties() throws Exception {
        String message = messageSource.getMessage(PROP_NAME, null, Locale.getDefault());
        Assert.assertEquals(PROP_VALUE, message);


    }

    @Test
    public void testConvert() throws Exception {
        Errors convert = mapper.map(result.getFieldErrors());
        Assert.assertEquals(2, convert.keySet().size());

        Assert.assertTrue("", convert.keySet().contains("field_1"));
        List<String> messages = convert.get("field_1");
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals("This is an error!", messages.get(0));

        Assert.assertTrue("", convert.keySet().contains("field_2"));
        messages = convert.get("field_2");
        Assert.assertEquals(2, messages.size());
        Assert.assertEquals("This is an error!", messages.get(0));
        Assert.assertEquals("This is an epic error!", messages.get(1));
    }

    @Data
    private class SomeObject {
        private String field_1;
        private String field_2;
    }

    private class SomeObjectValidator implements Validator {

        @Override
        public boolean supports(Class<?> clazz) {
            return Objects.class.equals(clazz);
        }

        @Override
        public void validate(Object target, org.springframework.validation.Errors errors) {
            errors.rejectValue("field_1", "error.code");
            errors.rejectValue("field_2", "error.code");
            errors.rejectValue("field_2", "epic.error.code");
        }
    }

}