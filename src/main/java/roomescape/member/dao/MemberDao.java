package roomescape.member.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.exception.RoomEscapeException;
import roomescape.member.domain.Member;

@Repository
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("MEMBER")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Member> memberRowMapper = ((resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password"))
    );

    public Member getByEmailAndPassword(final String email, final String password) {
        final String sql = "select * from member where email = ? and password = ?";
        try {
            return jdbcTemplate.queryForObject(sql, memberRowMapper, email, password);
        } catch (final EmptyResultDataAccessException exception) {
            throw new RoomEscapeException("해당하는 사용자가 존재하지 않습니다.");
        }
    }

    public Member getById(final long id) {
        final String sql = "select * from member where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, memberRowMapper, id);
        } catch (final EmptyResultDataAccessException exception) {
            throw new RoomEscapeException("해당하는 사용자가 존재하지 않습니다.");
        }
    }

    public long save(final Member member) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", member.getName())
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<Member> findAll() {
        final String sql = "select * from member";
        return jdbcTemplate.query(sql, memberRowMapper);
    }
}
