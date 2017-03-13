package phonebook.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Konstantin Kosmachevskiy
 */
public class SearchUtilTest {
    @Test
    public void checkAndTrimIfPhoneNumber() throws Exception {
        Assert.assertEquals("38044", SearchUtil.checkAndTrimIfPhoneNumber("+38044").get());
        Assert.assertEquals("38044", SearchUtil.checkAndTrimIfPhoneNumber("+38 (044").get());
        Assert.assertEquals("04412335", SearchUtil.checkAndTrimIfPhoneNumber(" (044 123-35").get());
        Assert.assertEquals("04412335", SearchUtil.checkAndTrimIfPhoneNumber(" (044)—123-35").get());
        Assert.assertEquals("3804412335", SearchUtil.checkAndTrimIfPhoneNumber(" +38(044)—123-35").get());
        Assert.assertFalse(SearchUtil.checkAndTrimIfPhoneNumber(" +38(044)—m").isPresent());
        Assert.assertFalse(SearchUtil.checkAndTrimIfPhoneNumber("likeName").isPresent());
    }

    @Test
    public void testRemoveSpaces() throws Exception {
        Assert.assertEquals("trimmed", SearchUtil.removeSpaces(" trimmed  "));
    }
}