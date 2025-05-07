package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import roomescape.entity.Member;
import roomescape.mapper.MemberMapper;

@Component
public class JdbcMemberDao implements MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean existsByEmailAndPassword(String email, String password) {
        String sql = "select exists (select 1 from member where email = ? and password = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                email,
                password
        ));
    }

    @Override
    public Member findByEmailAndPassword(String email, String password) {
        String sql = "select * from member where email = ? and password = ?";
        return jdbcTemplate.queryForObject(
                sql,
                new MemberMapper(),
                email,
                password
        );
    }
}
