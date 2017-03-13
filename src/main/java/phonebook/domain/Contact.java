package phonebook.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Konstantin Kosmachevskiy
 */
@Data
@NoArgsConstructor
public class Contact {
    private long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String mobilePhoneNumber;
    private String homePhoneNumber;
    private String address;
    private String email;

    public Contact(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        return id == contact.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
