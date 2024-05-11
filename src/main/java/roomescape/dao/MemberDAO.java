package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

    public Boolean existMember(final String email, final String password) {
        final String sql = """
                SELECT CASE WHEN EXISTS (
                    SELECT 1
                    FROM member
                    WHERE email = ? AND password = ?
                ) THEN TRUE ELSE FALSE END;
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, email, password);
    }

    public Member findByEmail(final String email) {
        final String sql = "SELECT * FROM member WHERE email = ?;";
        return jdbcTemplate.queryForObject(sql, memberRowMapper(), email);
    }

    public Member findById(final Long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper(), id);
    }

    private RowMapper<Member> memberRowMapper() {
        return (resultSet, rowNum) -> new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password")
        );
    }
}
