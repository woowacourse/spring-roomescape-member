package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> rowMapper =
            (resultSet, rowNum) -> new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password")
            );

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member findByEmail(final String email) {
        String sql = "select id, name, email, password from member where email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, email);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("회원 가입이 되지 않은 사용자입니다");
        }
    }

    public Member findByEmailAndPassword(final String email, final String password) {
        String sql = "select id, name, email, password from member where email = ? and password = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, email, password);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("회원 가입이 되지 않은 사용자입니다");
        }
    }
}
