package phonebook.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import phonebook.domain.Contact;
import phonebook.domain.User;
import phonebook.service.ContactService;
import phonebook.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Random;

/**
 * @author Konstantin Kosmachevskiy
 */
@Configuration
public class DemoConfig {

    @Autowired
    private ContactService contactService;
    @Autowired
    private UserService userRepository;

    @PostConstruct
    public void demoData() {
        if (userRepository.getByUserName("demo").isPresent())
            return;

        User user = new User();
        user.setUserName("demo");
        user.setPassword("demo");
        user.setFullName("Full Name");

        userRepository.add(user);
        user = userRepository.getByUserName("demo").get();

        for (int i = 0; i < 20; i++)
            contactService.addToUser(user, FakeContactProvider.getContact());
    }

    static class FakeContactProvider {
        private static final String[] FIRST_NAMES = {
                "Таня", "Надя", "Юля", "Настя", "Катя", "Люда", "Виолетта", "Травиатта", "Афродита"};
        private static final String[] LAST_NAMES =
                {"Иванова", "Петрова", "Филатова", "Абрамова", "Федорова", "Коваль", "Каваленко", "Ковалёва"};
        private static final String[] MIDDLE_NAMES =
                {"Владимировна", "Cергеевна", "Алексадровна", "Евгениевна", "Николаевна", "Cтаниславовна",
                        "Григориевна", "Михаловна"};
        private static final String[] EMAIL_SERVERS = {"@gmail.com", "@home.org", "@i.ua", "@mail.ru"};
        private static final String[] PHONE_CODE = {"+38093", "+38066", "+38050", "+38067", "+38097"};
        private static final String[] STREETS = {"New St.", "Old St.", "Fake St.", "Good St."};
        private static Random random = new Random(System.currentTimeMillis());

        private static String getFirstName() {
            return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        }

        private static String getLastName() {
            return LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        }

        private static String getMiddleName() {
            return MIDDLE_NAMES[random.nextInt(MIDDLE_NAMES.length)];
        }

        private static String getPhone() {
            return PHONE_CODE[random.nextInt(PHONE_CODE.length)] + (random.nextInt(8999999) + 1000000);
        }

        private static String cyr2lat(String in) {
            StringBuilder builder = new StringBuilder();
            in = in.toUpperCase();

            for (int i = 0; i < in.length(); i++) {
                builder.append(latChar(in.charAt(i)));
            }

            return builder.toString().toLowerCase();
        }

        private static String latChar(char c) {
            switch (c) {
                case 'А':
                    return "A";
                case 'Б':
                    return "B";
                case 'В':
                    return "V";
                case 'Г':
                    return "G";
                case 'Д':
                    return "D";
                case 'Е':
                    return "E";
                case 'Ё':
                    return "JE";
                case 'Ж':
                    return "ZH";
                case 'З':
                    return "Z";
                case 'И':
                    return "I";
                case 'Й':
                    return "Y";
                case 'К':
                    return "K";
                case 'Л':
                    return "L";
                case 'М':
                    return "M";
                case 'Н':
                    return "N";
                case 'О':
                    return "O";
                case 'П':
                    return "P";
                case 'Р':
                    return "R";
                case 'С':
                    return "S";
                case 'Т':
                    return "T";
                case 'У':
                    return "U";
                case 'Ф':
                    return "F";
                case 'Х':
                    return "KH";
                case 'Ц':
                    return "C";
                case 'Ч':
                    return "CH";
                case 'Ш':
                    return "SH";
                case 'Щ':
                    return "JSH";
                case 'Ъ':
                    return "HH";
                case 'Ы':
                    return "IH";
                case 'Ь':
                    return "JH";
                case 'Э':
                    return "EH";
                case 'Ю':
                    return "JU";
                case 'Я':
                    return "JA";
                default:
                    return String.valueOf(c);
            }
        }

        private static String getAddress() {
            return random.nextInt(100) + " " + STREETS[random.nextInt(STREETS.length)];
        }

        static Contact getContact() {
            Contact contact = new Contact();

            contact.setMiddleName(getMiddleName());
            contact.setFirstName(getFirstName());
            contact.setLastName(getLastName());

            contact.setMobilePhoneNumber(getPhone());

            if (random.nextBoolean())
                contact.setHomePhoneNumber(getPhone());

            if (random.nextBoolean())
                contact.setAddress(getAddress());

            if (random.nextBoolean())
                contact.setEmail(getEmail(contact.getFirstName(), contact.getLastName()));

            return contact;
        }

        private static String getEmail(String firstName, String lastName) {
            return cyr2lat(lastName) + "." + cyr2lat(firstName) +
                    EMAIL_SERVERS[random.nextInt(EMAIL_SERVERS.length)];
        }
    }

}
