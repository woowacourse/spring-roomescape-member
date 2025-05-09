package roomescape.repository;

import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.model.Member;
import roomescape.model.MemberName;
import roomescape.model.Role;

@Repository
public class JdbcMemberRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        String sql = "select * from member where email =?";
        return jdbcTemplate.query(sql, (rs, rn) -> {
            MemberName name = new MemberName(rs.getString("name"));
            return new Member(rs.getLong("id"), Role.valueOf(rs.getString("role")), name, rs.getString("email"),
                    rs.getString("password"));
        }, email).stream().findFirst();
    }

    @Override
    public Optional<Member> findById(final Long id) {
        String sql = "select * from member where id =?";
        return jdbcTemplate.query(sql, (rs, rn) -> {
            MemberName name = new MemberName(rs.getString("name"));
            return new Member(rs.getLong("id"), Role.valueOf(rs.getString("role")), name, rs.getString("email"),
                    rs.getString("password"));
        }, id).stream().findFirst();
    }
}
