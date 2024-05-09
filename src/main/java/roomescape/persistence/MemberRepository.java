package roomescape.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.domain.Role;

@Repository
public class MemberRepository {

    private static final RowMapper<Member> MEMBER_ROW_MAPPER =
            (resultSet, rowNum) ->
                    new Member(
                            resultSet.getLong("id"),
                            new Name(resultSet.getString("name")),
                            resultSet.getString("email"),
                            Role.valueOf(resultSet.getString("role"))
                    );

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT id, name, email, password, role FROM member WHERE email = ? AND password = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, email, password));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, name, email, role FROM member WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Member> findAll() {
        String sql = "SELECT id, name, email, role FROM member";
        return jdbcTemplate.query(sql, MEMBER_ROW_MAPPER);
    }
}
