package roomescape.auth.infrastructure;

import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.auth.domain.Member;
import roomescape.auth.domain.MemberRepository;

@Repository
public class JdbcMemberRepository implements MemberRepository {
    private final static RowMapper<Member> MEMBER_ROW_MAPPER =
            (rs, rowNum) -> new Member(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name")
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Member member) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("members");

        Map<String, Object> parameters = Map.of(
                "email", member.getEmail(),
                "password", member.getPassword(),
                "name", member.getName()
        );

        simpleJdbcInsert.execute(parameters);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String selectOneSql = "SELECT email, password, name FROM members WHERE email=?";
        try {
            Member member = jdbcTemplate.queryForObject(selectOneSql, MEMBER_ROW_MAPPER,
                    email);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
