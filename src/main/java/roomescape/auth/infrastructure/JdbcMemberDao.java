package roomescape.auth.infrastructure;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.auth.domain.LoginMember;

@Repository
public class JdbcMemberDao implements MemberDao {

    private static final RowMapper<LoginMember> MEMBER_ROW_MAPPER =
            (resultSet, rowNumber) ->
                    new LoginMember(
                            resultSet.getLong("id"),
                            resultSet.getString("role"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    );

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<LoginMember> findByEmail(final String email) {
        final String query = "SELECT * FROM member WHERE email = ?";
        final List<LoginMember> members = jdbcTemplate.query(query, MEMBER_ROW_MAPPER, email);
        return members.stream()
                .findFirst();
    }
}
