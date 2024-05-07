package roomescape.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;

@Repository
public class JdbcMemberDao implements MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Member create(Member member) {
        String sql = """
                INSERT
                INTO member
                (name, email, password)
                VALUES
                (?, ?, ?);
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, member.getName().getValue());
                    ps.setString(2, member.getEmail().getValue());
                    ps.setString(3, member.getPassword().getValue());
                    return ps;
                },
                keyHolder
        );
        long id = keyHolder.getKey().longValue();
        return new Member(id, member.getName(), member.getEmail(), member.getPassword());
    }

    @Override
    public boolean existByEmail(MemberEmail memberEmail) {
        String sql = """
                SELECT
                CASE 
                    WHEN EXISTS (SELECT 1 FROM member WHERE email = ?)
                    THEN TRUE
                    ELSE FALSE
                END;
                """;
        return jdbcTemplate.queryForObject(sql, boolean.class, memberEmail.getValue());
    }
}
