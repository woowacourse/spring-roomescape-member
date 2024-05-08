package roomescape.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Name;
import roomescape.domain.Member;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class MemberJdbcDao implements MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Member> rowMapper = (resultSet, rowNumber) -> new Member(
        resultSet.getLong("id"),
        new Name(resultSet.getString("name")),
        resultSet.getString("email"),
        resultSet.getString("password"));

    public MemberJdbcDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(final Member member) {
        final Name name = member.getName();
        final String email = member.getEmail();
        final String password = member.getPassword();
        final Long id = jdbcInsert.executeAndReturnKey(
                Map.of("name", name,
                        "email", email,
                        "password", password))
                .longValue();
        return new Member(Objects.requireNonNull(id), name, email, password);
    }

    @Override
    public Optional<Member> findById(final Long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
