package roomescape.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Role;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    private final static RowMapper<Member> rowMapper = (resultSet, rowNum) -> new Member(
        resultSet.getLong("id"),
        resultSet.getString("name"),
        resultSet.getString("email"),
        resultSet.getString("password"),
        Role.valueOf(resultSet.getString("role"))
    );

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> findByEmail(String email) {
        String query = "SELECT * FROM member where email = ?";
        try {
            Member member = jdbcTemplate.queryForObject(query, rowMapper, email);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(Long memberId) {
        String query = "SELECT * FROM member where id = ?";
        try {
            Member member = jdbcTemplate.queryForObject(query, rowMapper, memberId);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Member> getAll() {
        String query = "SELECT * FROM member";
        return jdbcTemplate.query(query, rowMapper);
    }
}
