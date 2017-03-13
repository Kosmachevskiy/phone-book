package phonebook.domain;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Konstantin Kosmachevskiy
 */
public class ContactTest {
    @Test
    public void equals() throws Exception {
        Contact c1 = new Contact();
        Contact c2 = new Contact();

        c1.setId(1);

        Assert.assertNotEquals(c1, c2);

        c2.setId(1);

        Assert.assertEquals(c1, c2);
    }

}