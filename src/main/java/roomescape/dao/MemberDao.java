package roomescape.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.mapper.MemberRowMapper;
import roomescape.domain.Email;
import roomescape.domain.Member;
import roomescape.domain.Password;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final MemberRowMapper rowMapper;

    public MemberDao(final JdbcTemplate jdbcTemplate,
                     final DataSource dataSource,
                     final MemberRowMapper memberRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
        this.rowMapper = memberRowMapper;
    }

    public Member create(final Member member) {
        final var params = new MapSqlParameterSource()
                .addValue("name", member.nameAsString())
                .addValue("email", member.emailAsString())
                .addValue("password", member.passwordAsString())
                .addValue("role", member.roleAsString());
        final var id = jdbcInsert.executeAndReturnKey(params).longValue();
        return Member.of(id, member);
    }

    public List<Member> getAll() {
        final var sql = "SELECT * FROM member";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Member> findById(final Long id) {
        final var sql = "SELECT * FROM member WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<Member> find(final Email email, final Password password) {
        final var sql = "SELECT * FROM member WHERE email = ? AND password = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, email.value(), password.value()));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
