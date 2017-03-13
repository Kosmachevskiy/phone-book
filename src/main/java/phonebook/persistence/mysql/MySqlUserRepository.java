package phonebook.persistence.mysql;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import phonebook.domain.User;
import phonebook.persistence.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @author Konstantin Kosmachevskiy
 */

public class MySqlUserRepository extends NamedParameterJdbcDaoSupport implements UserRepository {

    private static final UserMapper MAPPER = new UserMapper();

    @Override
    public Optional<User> getByUserName(String userName) {
        String sql = "SELECT * FROM users WHERE username = :userName";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("userName", userName);

        List<User> query = getNamedParameterJdbcTemplate().query(sql, parameterSource, MAPPER);
        if (query.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(query.get(0));
    }

    @Override
    public void add(User user) {
        String sql = "INSERT INTO users (username, password, full_name) VALUES (:userName, :password, :fullName);";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("userName", user.getUserName());
        parameterSource.addValue("password", user.getPassword());
        parameterSource.addValue("fullName", user.getFullName());

        getNamedParameterJdbcTemplate().execute(sql, parameterSource, PreparedStatement::execute);
    }

    private static class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setPassword(rs.getString("password"));
            user.setUserName(rs.getString("username"));
            user.setFullName(rs.getString("full_name"));
            return user;
        }
    }
}
