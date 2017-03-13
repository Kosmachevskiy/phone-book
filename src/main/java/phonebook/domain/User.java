package phonebook.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Konstantin Kosmachevskiy
 */
@Data
@NoArgsConstructor
public class User {
    private long id;
    private String userName;
    private String password;
    private String confirmPassword;
    private String fullName;

    public User(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        User user = (User) o;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }


}
