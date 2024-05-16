package roomescape.repository.member;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;

@Repository
public class MemberRepository {

    private static final RowMapper<Member> USER_ROW_MAPPER = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            Role.of(resultSet.getString("role_name"))
    );

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> findById(Long id) {
        String sql = """
                SELECT m.id, m.name AS name, r.name AS role_name 
                FROM member AS m 
                INNER JOIN role AS r 
                ON m.role_id = r.id 
                WHERE m.id = ?
                """;
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, USER_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<Member> findAll() {
        String sql = """
                SELECT m.id, m.name, r.name AS role_name 
                FROM member AS m 
                INNER JOIN role AS r 
                ON m.role_id = r.id
                """;
        return jdbcTemplate.query(sql, USER_ROW_MAPPER);
    }
}
