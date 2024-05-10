package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;

import java.util.Optional;

@Repository
public class MemberCredentialRepository {

    private static final RowMapper<Member> USER_ROW_MAPPER = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name")
    );

    private final JdbcTemplate jdbcTemplate;

    public MemberCredentialRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT m.id, m.name " +
                "FROM member_credential AS mc " +
                "INNER JOIN member AS m " +
                "ON mc.member_id = m.id " +
                "WHERE mc.email = ? AND mc.password = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, USER_ROW_MAPPER, email, password));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
