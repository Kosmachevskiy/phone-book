package phonebook.persistence.mysql;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import phonebook.domain.Contact;
import phonebook.domain.User;
import phonebook.persistence.ContactRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Konstantin Kosmachevskiy
 */
public class MySqlContactRepository extends NamedParameterJdbcDaoSupport implements ContactRepository {

    public static final RowMapper<Contact> MAPPER = new ContactMapper();

    @Override
    public List<Contact> getAllByUser(User user) {
        String sql = "SELECT * FROM contacts WHERE user_id = :userId ORDER BY last_name;";

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("userId", user.getId());

        return getNamedParameterJdbcTemplate().query(sql, source, MAPPER);
    }

    @Override
    public void addToUser(User user, Contact contact) {
        String sql = "INSERT INTO contacts " +
                "(last_name, first_name, middle_name, mobile_phone_number, email, home_phone_number, address, user_id) " +
                "VALUES " +
                "(:lastName, :firstName, :middleName, :mobilePhoneNumber, :email, :homePhoneNumber, :address, :userId);";

        MapSqlParameterSource source = buildMapSqlParameterSourceFor(user, contact);

        getNamedParameterJdbcTemplate().execute(sql, source, PreparedStatement::execute);
    }

    private MapSqlParameterSource buildMapSqlParameterSourceFor(User user, Contact contact) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("lastName", contact.getLastName());
        source.addValue("middleName", contact.getMiddleName());
        source.addValue("firstName", contact.getFirstName());
        source.addValue("mobilePhoneNumber", contact.getMobilePhoneNumber());
        source.addValue("homePhoneNumber", contact.getHomePhoneNumber());
        source.addValue("email", contact.getEmail());
        source.addValue("address", contact.getAddress());
        source.addValue("userId", user.getId());
        return source;
    }

    @Override
    public void removeFromUser(User user, Contact contact) {
        String sql = "DELETE FROM contacts WHERE id = :id;";

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", contact.getId());

        getNamedParameterJdbcTemplate().execute(sql, source, PreparedStatement::execute);
    }

    @Override
    public void update(User user, Contact contact) {
        String sql = "UPDATE contacts SET " +
                "first_name = :firstName," +
                "last_name = :lastName," +
                "middle_name = :middleName," +
                "mobile_phone_number = :mobilePhoneNumber," +
                "home_phone_number = :homePhoneNumber," +
                "email = :email," +
                "address = :address " +
                "WHERE user_id = :userId and id = :id;";

        MapSqlParameterSource source = buildMapSqlParameterSourceFor(user, contact);
        source.addValue("id", contact.getId());

        getNamedParameterJdbcTemplate().execute(sql, source, PreparedStatement::execute);
    }

    @Override
    public List<Contact> findByNamesAndPhones(User user, String pattern) {
        String sql = "SELECT * FROM contacts " +
                "WHERE (first_name LIKE :pattern " +
                "OR last_name LIKE :pattern " +
                "OR middle_name LIKE :pattern " +
                "OR mobile_phone_number LIKE :pattern " +
                "OR home_phone_number LIKE :pattern) " +
                "AND user_id = :id " +
                "ORDER BY last_name;";

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("pattern", "%" + pattern + "%");
        source.addValue("id", user.getId());

        return getNamedParameterJdbcTemplate().query(sql, source, MAPPER);
    }

    private static class ContactMapper implements RowMapper<Contact> {

        @Override
        public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
            Contact contact = new Contact();

            contact.setId(rs.getLong("id"));
            contact.setFirstName(rs.getString("first_name"));
            contact.setLastName(rs.getString("last_name"));
            contact.setMiddleName(rs.getString("middle_name"));
            contact.setMobilePhoneNumber(rs.getString("mobile_phone_number"));
            contact.setHomePhoneNumber(rs.getString("home_phone_number"));
            contact.setEmail(rs.getString("email"));
            contact.setAddress(rs.getString("address"));

            return contact;
        }
    }
}
