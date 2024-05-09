package roomescape.domain.member.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.domain.ReservationMember;
import roomescape.global.auth.AuthUser;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<AuthUser> authRowMapper = (resultSet, rowNum) ->
            new AuthUser(
                    resultSet.getLong("id"),
                    resultSet.getString("name")
            );

    private final RowMapper<ReservationMember> memberRowMapper = (resultSet, rowNum) ->
            new ReservationMember(
                    resultSet.getLong("id"),
                    resultSet.getString("name")
            );

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<AuthUser> findIdByEmailAndPassword(String email, String password) {
        String sql = "SELECT id, name FROM member WHERE email = ? AND password = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, authRowMapper, email, password));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<ReservationMember> findAll() {
        String sql = "SELECT id, name FROM member";
        return jdbcTemplate.query(sql, memberRowMapper);
    }
}
