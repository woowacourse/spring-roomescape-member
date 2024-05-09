package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.model.Member;
import roomescape.model.Role;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    private static final RowMapper<Member> ROW_MAPPER = (selectedMember, rowNum) ->
            new Member(
                selectedMember.getLong("id"),
                selectedMember.getString("name"),
                Role.from(selectedMember.getString("role")),
                selectedMember.getString("email"),
                selectedMember.getString("password"));

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Member> findAll() {
        final String selectQuery = """
            SELECT
                id,
                name,
                email,
                password,
                role
            FROM member
        """;
        return jdbcTemplate.query(selectQuery, ROW_MAPPER)
                .stream()
                .toList();
    }

    public Optional<Member> findByEmail(final String email) {
        final String selectQuery = """
            SELECT
                id,
                name,
                email,
                password,
                role
            FROM member
            WHERE email = ?
            LIMIT 1
        """;
        try {
            final Member member = jdbcTemplate.queryForObject(selectQuery, ROW_MAPPER, email);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(final Long memberId) {
        final String selectQuery = """
            SELECT
                id,
                name,
                email,
                password,
                role
            FROM member
            WHERE id = ?
            LIMIT 1
        """;
        try {
            final Member member = jdbcTemplate.queryForObject(selectQuery, ROW_MAPPER, memberId);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
