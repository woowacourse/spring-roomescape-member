package roomescape.persistence;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Name;

@Repository
public class MemberRepository {

    private static final RowMapper<Member> MEMBER_ROW_MAPPER =
            (resultSet, rowNum) -> new Member(
                    resultSet.getLong("id"),
                    new Name(resultSet.getString("name")),
                    resultSet.getString("email"),
                    resultSet.getString("password")
            );

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT id, name, email, password FROM member WHERE email = ? AND password = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, email, password));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
