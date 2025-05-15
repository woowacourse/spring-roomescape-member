package roomescape.auth.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.Member;
import roomescape.entity.Role;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class JdbcAuthRepository implements AuthRepository {

    private static RowMapper<Member> ROWMAPPER() {
        return (resultSet, rowNum) ->
                Member.afterSave(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role"))
                );
    }

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
        String role = String.valueOf(member.getRole());
        final String sql = "INSERT INTO member (name, email, password,role) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, role);
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        return Member.afterSave(id, name, email, password, Role.valueOf(role));
    }

    public Member findByEmail(final String email) {
        try {
            final String sql = "SELECT * FROM member WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, ROWMAPPER(), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Member findMemberById(final long id) {
        try {
            final String sql = "SELECT * FROM member WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, ROWMAPPER(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }
}
