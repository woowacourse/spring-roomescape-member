package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

import java.sql.PreparedStatement;

@Repository
public class MemberDAO {
    private final JdbcTemplate jdbcTemplate;

    public MemberDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member insert(final Member member) {
        final String sql = "INSERT INTO member (name, email, password) VALUES (?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        final String name = member.getName();
        final String email = member.getEmail();
        final String password = member.getPassword();

        jdbcTemplate.update(con -> {
            final PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            return preparedStatement;
        }, keyHolder);

        final long key = keyHolder.getKey().longValue();
        return new Member(key, name, email, password);
    }
}
