package roomescape.dao.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.exception.custom.NotFoundException;

@Repository
public class JdbcMemberDao implements MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member findMemberByEmail(String email) {
        String sql = "SELECT id, name, email, password FROM member WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, createMemberMapper(), email);
        } catch (DataAccessException e) {
            throw new NotFoundException("member");
        }
    }

    private RowMapper<Member> createMemberMapper() {
        return (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password"));
    }
}
