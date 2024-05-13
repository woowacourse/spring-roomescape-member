package roomescape.member.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {
    private static final RowMapper<Member> ROW_MAPPER = (resultSet, rowNum) ->
            new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    Role.valueOf(resultSet.getString("role")));

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<List<Member>> findAll() {
        String sql = "SELECT * FROM member";
        try {
            List<Member> members = jdbcTemplate.query(sql, ROW_MAPPER);
            return Optional.of(members);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, ROW_MAPPER, email);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
