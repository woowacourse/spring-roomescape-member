package roomescape.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.LoginMember;
import roomescape.domain.member.Member;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM member WHERE email = ? and password = ?";
        List<Member> result = jdbcTemplate.query(sql, (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password")
        ), email, password);

        return result.stream().findAny();
    }

    @Override
    public Optional<LoginMember> findById(Long id) {
        String sql = "SELECT id, name, email FROM member WHERE id = ?";
        List<LoginMember> result = jdbcTemplate.query(sql, (rs, rowNum) -> new LoginMember(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("name")
        ), id);
        return result.stream().findAny();
    }
}
