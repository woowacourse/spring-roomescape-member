package roomescape.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.domain.Role;

@Repository
public class MemberDao {
    private static final RowMapper<Member> MAPPER = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            new Name(rs.getString("name")),
            Role.valueOf(rs.getString("role")),
            rs.getString("email"),
            rs.getString("password")
    );
    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Member> findAll() {
        return jdbcTemplate.query("select * from member", MAPPER);
    }

    public Member find(Member member) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM member WHERE email = ? AND password = ?",
                MAPPER, member.getEmail(), member.getPassword());
    }

    public Member findMemberById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM member WHERE id = ?", MAPPER, id);
    }
}
