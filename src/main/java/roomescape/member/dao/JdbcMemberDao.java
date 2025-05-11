package roomescape.member.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;

@Repository
public class JdbcMemberDao implements MemberDao {

    private static final RowMapper<Member> MEMBER_ROW_MAPPER =
            (resultSet, rowNumber) ->
                    Member.of(
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
    public List<Member> findAll() {
        final String query = "SELECT * FROM member";
        return jdbcTemplate.query(query, MEMBER_ROW_MAPPER);
    }

    @Override
    public Optional<Member> findById(final long id) {
        final String query = "SELECT * FROM member WHERE id = ?";
        final List<Member> members = jdbcTemplate.query(query, MEMBER_ROW_MAPPER, id);
        return members.stream()
                .findFirst();
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        final String query = "SELECT * FROM member WHERE email = ?";
        final List<Member> members = jdbcTemplate.query(query, MEMBER_ROW_MAPPER, email);
        return members.stream()
                .findFirst();
    }
}
