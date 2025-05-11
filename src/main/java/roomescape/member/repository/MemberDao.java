package roomescape.member.repository;

import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.member.domain.Member;

@Repository
public class MemberDao {

    private static final String TABLE_NAME = "member";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Member> memberRowMapper = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );

    public MemberDao(final NamedParameterJdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    public Member save(final Member member) {
        String name = member.getName();
        String email = member.getEmail();
        String password = member.getPassword();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("email", email)
                .addValue("password", password);
        long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Member(id, name, email, password);
    }

    public Optional<Member> findByEmailAndPassword(final String email, final String password) {
        String sql = """
                SELECT id, name, email, password
                FROM member
                WHERE email = :email AND password = :password
                """;
        Map<String, String> params = Map.of("email", email, "password", password);

        try {
            Member member = jdbcTemplate.queryForObject(sql, params, memberRowMapper);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(final Long id) {
        String sql = """
                SELECT id, name, email, password
                FROM member
                WHERE id = :id
                """;
        Map<String, Long> params = Map.of("id", id);

        try {
            Member member = jdbcTemplate.queryForObject(sql, params, memberRowMapper);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
