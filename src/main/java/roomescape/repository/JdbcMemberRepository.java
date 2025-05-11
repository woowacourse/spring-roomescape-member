package roomescape.repository;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.model.user.Email;
import roomescape.model.user.Member;
import roomescape.model.user.Name;
import roomescape.model.user.Password;
import roomescape.model.user.Role;

@Repository
public class JdbcMemberRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> userRowMapper = (rs, rowNum) ->
            new Member(
                    rs.getLong("id"),
                    new Name(rs.getString("name")),
                    new Email(rs.getString("email")),
                    new Password(rs.getString("password")),
                    Role.of(rs.getString("role")
                    )
            );

    @Override
    public Member login(String email, String password) {
        String sql = "select * from member where email = ? and password = ?";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, email, password);
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: " + email, ex);
        }
    }

    @Override
    public Name findUserNameByUserId(Long userId) {
        String sql = "select name from member where id = ?";
        try {
            return new Name(jdbcTemplate.queryForObject(sql, String.class, userId));
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: userId :" + userId, ex);
        }
    }

    @Override
    public Name findUserNameByUserEmail(String userEmail) {
        String sql = "select name from member where email = ?";
        try {
            return new Name(jdbcTemplate.queryForObject(sql, String.class, userEmail));
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: userEmail :" + userEmail, ex);
        }
    }

    @Override
    public List<Member> findAllUsers() {
        String sql = "select * from member";
        return jdbcTemplate.query(sql, userRowMapper);
    }
}
