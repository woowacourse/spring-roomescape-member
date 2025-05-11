package roomescape.member.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;

@Repository
public class JdbcMemberDao implements MemberDao {

    private static final RowMapper<Member> MEMBER_ROW_MAPPER =
            (resultSet, rowNumber) ->
                    Member.of(
                            resultSet.getLong("id"),
                            resultSet.getString("role"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcMemberDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public long insert(final Member member) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", member.getId())
                .addValue("role", member.getRole().toString())
                .addValue("name", member.getName())
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword());
        final Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
        return newId.longValue();
    }

    @Override
    public boolean existsByEmail(final String email) {
        final String query = "SELECT EXISTS (SELECT 1 FROM member WHERE email = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, email));
    }

    @Override
    public List<Member> findAll() {
        final String query = "SELECT * FROM member";
        return jdbcTemplate.query(query, MEMBER_ROW_MAPPER);
    }

    @Override
    public Optional<Member> findById(final long id) {
        final String query = "SELECT * FROM member WHERE id = ?";
        final List<Member> members = jdbcTemplate.query(query, MEMBER_ROW_MAPPER, id);
        return members.stream()
                .findFirst();
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        final String query = "SELECT * FROM member WHERE email = ?";
        final List<Member> members = jdbcTemplate.query(query, MEMBER_ROW_MAPPER, email);
        return members.stream()
                .findFirst();
    }
}
