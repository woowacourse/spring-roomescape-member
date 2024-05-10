package roomescape.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.mapper.MemberMapper;
import roomescape.domain.user.Member;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final MemberMapper rowMapper;

    public MemberDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource, final MemberMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
        this.rowMapper = rowMapper;
    }

    public Member create(final Member member) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", member.getName())
                .addValue("password", member.getPassword())
                .addValue("email", member.getEmail());

        final long id = jdbcInsert.executeAndReturnKey(params)
                .longValue();
        return Member.from(id, member.getName(), member.getEmail(), member.getPassword());
    }

    public Optional<Member> findByEmail(final String email) {
        final String sql = "SELECT id, name, email, password FROM member WHERE email = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, email));
        } catch (final EmptyResultDataAccessException | NullPointerException exception) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(final long id) {
        final String sql = "SELECT id, name, email, password FROM member WHERE id=?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException | NullPointerException exception) {
            return Optional.empty();
        }
    }
    public List<Member> getAllMembers() {
        final String sql = "SELECT id, name, email, password FROM member";
        return jdbcTemplate.query(sql, rowMapper);
    }


}
