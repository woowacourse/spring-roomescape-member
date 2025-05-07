package roomescape.user.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.user.domain.Member;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member save(Member member) {
        String sql = """
                INSERT INTO member (email, password, name)
                VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getName());
        return member;
    }
}
