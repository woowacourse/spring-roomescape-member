package roomescape.auth.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.auth.entity.Member;
import roomescape.auth.entity.Role;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class JdbcAuthRepository implements AuthRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAuthRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public boolean isExistEmail(String email) {
        final String sql = "SELECT COUNT(*) FROM member WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public Member save(Member member) {
        String name = member.getName();
        String email = member.getEmail();
        String password = member.getPassword();
        final String sql = "INSERT INTO member (name, email, password) VALUES (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        return Member.afterSave(id, name, email, password, Role.USER);
    }
}
