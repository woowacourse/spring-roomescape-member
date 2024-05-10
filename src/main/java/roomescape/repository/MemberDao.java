package roomescape.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Password;
import roomescape.domain.dto.LoginRequest;
import roomescape.domain.dto.SignupRequest;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberDao {
    private static final RowMapper<Member> memberRowMapper =
            (resultSet, rowNum) -> new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("email"),
                    resultSet.getString("name")
            ) {
            };

    private static final RowMapper<Password> passwordRowMapper =
            (resultSet, rowNum) -> new Password(
                    resultSet.getString("password"),
                    resultSet.getString("salt")
            ) {
            };

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Member> findAll() {
        String sql = "select id, email, name from member";
        return jdbcTemplate.query(sql, memberRowMapper);
    }

    public Optional<Member> findByEmail(final String email) {
        String sql = "select id, email, name from member where email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, memberRowMapper, email));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(final Long memberId) {
        String sql = "select id, email, name from member where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, memberRowMapper, memberId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Long create(final SignupRequest signupRequest, final Password password) {
        String sql = "insert into member(email, password, salt, name) values(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, signupRequest.email());
            ps.setString(2, password.getHashValue());
            ps.setString(3, password.getSalt());
            ps.setString(4, signupRequest.name());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public boolean isExist(final SignupRequest signupRequest) {
        String sql = "select count(email) from member where email = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, signupRequest.email()) != 0;
    }

    public boolean isLoginFail(final LoginRequest loginRequest, Password password) {
        String sql = "select count(*) from member where email = ? and password = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, loginRequest.email(), password.getHashValue()) == 0;
    }

    public Optional<Password> findPasswordByEmail(final String email) {
        String sql = "select password, salt from member where email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, passwordRowMapper, email));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
