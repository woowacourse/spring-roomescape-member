package roomescape.repository;

import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.model.Member;
import roomescape.model.MemberName;

@Repository
public class JdbcMemberRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        String sql = "select * from member ";
        return jdbcTemplate.query(sql, (rs, rn) -> {
            MemberName name = new MemberName(rs.getString("name"));
            return new Member(rs.getLong("id"), rs.getString("role"), name, rs.getString("email"),
                    rs.getString("password"));
        }).stream().findFirst();
    }
}
