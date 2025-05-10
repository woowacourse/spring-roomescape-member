package roomescape.dao.meber;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class JdbcMemberDao implements MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> memberMapper = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("email"),
            resultSet.getString("name"),
            resultSet.getString("password")
    );

    public JdbcMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Member create(final Member member) {
        final SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");

        final Map<String, Object> parameters = new HashMap<>(Map.of(
                "name", member.getName(),
                "email", member.getEmail(),
                "password", member.getPassword()));
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return new Member(key.longValue(), member.getName(), member.getEmail(), member.getPassword());
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        final String sql = """
                SELECT *
                FROM member
                WHERE email = ?
                """;
        return jdbcTemplate.query(sql, memberMapper, email).stream()
                .findFirst();
    }
}
