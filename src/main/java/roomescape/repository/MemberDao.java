package roomescape.repository;

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

    private final RowMapper<Member> rowMapper = (resultSet, __) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            Role.from(resultSet.getString("role"))
    );

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> findByEmail(String email) {
        String query = "SELECT * FROM MEMBER WHERE email = ?";
        try {
            Member member = jdbcTemplate.queryForObject(query, rowMapper, email);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(long id) {
        String query = "SELECT * FROM MEMBER WHERE id = ?";
        try {
            Member member = jdbcTemplate.queryForObject(query, rowMapper, id);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Member> findAll() {
        String query = "SELECT * FROM MEMBER";
        return jdbcTemplate.query(query, rowMapper);
    }
}
