package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.domain.mapper.MemberMapper;

@Component
public class JdbcMemberDao implements MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Member findByEmail(String email) {
        String sql = "select * from member where email = ?";
        return jdbcTemplate.queryForObject(
                sql,
                new MemberMapper(),
                email
        );
    }
}
