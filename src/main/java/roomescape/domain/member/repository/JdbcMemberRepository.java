package roomescape.domain.member.repository;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;

@Repository
public class JdbcMemberRepository implements MemberRepository {
    private static final RowMapper<Member> ROW_MAPPER = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            new MemberName(rs.getString("name")),
            rs.getString("email"),
            rs.getString("password")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(Member member) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", member.getMemberName().getValue())
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword());

        long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Member(id, member.getMemberName(), member.getEmail(), member.getPassword());
    }

    @Override
    public boolean existsByEmailAndPassword(String email, String password) {
        String query = """
                SELECT m.id FROM member AS m
                WHERE EXISTS(SELECT 1 FROM member WHERE m.email = ? AND m.password = ?)
                """;

        try {
            jdbcTemplate.queryForObject(query, Long.class, email, password);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String query = """
                SELECT * FROM member AS m
                WHERE m.email = ?
                """;

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, ROW_MAPPER, email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
