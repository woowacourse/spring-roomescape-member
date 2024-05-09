package roomescape.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.exception.InvalidInputException;

@Repository
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> rowMapper = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            new Name(rs.getString("name")),
            rs.getString("email"),
            rs.getString("password")
    );

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Member> findAll() {
        return jdbcTemplate.query("select * from member", rowMapper);
    }

    public Member find(Member member) {
        List<Member> members = jdbcTemplate.query(
                "SELECT * FROM member WHERE email = ? AND password = ?",
                rowMapper, member.getEmail(), member.getPassword());
        if (members.isEmpty()) {
            throw new InvalidInputException("이메일 혹은 비밀번호가 일치하지 않습니다.");
        }
        return members.get(0);
    }

    public Member findMemberById(Long id) {
        List<Member> members = jdbcTemplate.query(
                "SELECT * FROM member WHERE id = ?", rowMapper, id);
        if (members.isEmpty()) {
            throw new InvalidInputException("해당 계정이 존재하지 않습니다.");
        }
        return members.get(0);
    }
}
