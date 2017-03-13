package phonebook.util;

import java.util.Optional;

/**
 * @author Konstantin Kosmachevskiy
 */
public class SearchUtil {
    private static final String ONLY_DIGITS = "^\\d+$";
    private static final String PHONE_TEMPLATE_COMMON_CHARS = "[\\—\\-\\(\\)\\ \\+]";

    /**
     * Checks search pattern to phone number likeness and removes chars from it.
     *
     * @param pattern Search pattern
     * @return {@link Optional} of {@link String}.
     * String present in case of pattern like number and was trimmed.
     */
    public static Optional<String> checkAndTrimIfPhoneNumber(String pattern) {
        pattern = pattern.replaceAll(PHONE_TEMPLATE_COMMON_CHARS, "");

        if (pattern.matches(ONLY_DIGITS))
            return Optional.of(pattern);
        else
            return Optional.empty();
    }

    public static String removeSpaces(String s) {
        return s.replace(" ", "");
    }
}
