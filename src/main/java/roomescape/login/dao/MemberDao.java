package roomescape.login.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.login.domain.Member;

@Repository
public class MemberDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MemberDao(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Optional<Member> findByEmail(final String email) {
        String sql = "SELECT id, name, email, password from member where email = :email";
        Map<String, Object> parameter = Map.of("email", email);

        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(sql, parameter,
                    (resultSet, rowNum) -> createMember(resultSet)));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Boolean isPasswordMatch(final String email, final String password) {
        String sql = "SELECT password FROM member WHERE email = :email";
        Map<String, Object> parameter = Map.of("email", email);

        try {
            String memberPassword = namedParameterJdbcTemplate.queryForObject(sql, parameter, String.class);
            return password.equals(memberPassword);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private Member createMember(final ResultSet resultSet) throws SQLException {
        return new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password")
        );
    }
}
