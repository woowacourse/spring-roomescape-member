package roomescape.repository;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Email;
import roomescape.domain.Member;
import roomescape.domain.Password;
import roomescape.domain.Role;
import roomescape.domain.UserName;

@Repository
public class MemberJdbcRepository implements MemberRepository {

    private static final RowMapper<Member> memberRowMapper = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            new UserName(resultSet.getString("name")),
            new Email(resultSet.getString("email")),
            new Password(resultSet.getString("password")),
            Role.valueOf(resultSet.getString("role"))
    );

    private final JdbcTemplate jdbcTemplate;

    public MemberJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Member> findAll() {
        String sql = "SELECT id, name, email, password, role FROM member";
        return jdbcTemplate.query(sql, memberRowMapper);
    }

    public Member findByMemberId(Long id) {
        String sql = "SELECT id, name, email, password, role FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper, id);
    }

    public Member findByEmail(String email) {
        String sql = "SELECT id, name, email, password, role FROM member WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper, email);
    }
}
