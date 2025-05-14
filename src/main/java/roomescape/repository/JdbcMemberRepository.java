package roomescape.repository;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.MemberRoleType;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = (resultSet, rowNum) -> new Member(
            resultSet.getLong("member_id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            MemberRoleType.from(resultSet.getString("role"))
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcMemberRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("member_id");
        ;
    }

    @Override
    public long insert(final Member member) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", member.getName())
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword())
                .addValue("role", member.getRole().getName());
        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
        return newId.longValue();
    }

    @Override
    public List<Member> findAllByRole(final MemberRoleType role) {
        String sql = "SELECT * FROM member WHERE role = ?";
        return jdbcTemplate.query(sql, MEMBER_ROW_MAPPER, role.getName());
    }

    @Override
    public Optional<Member> findById(final long id) {
        String query = "SELECT * FROM member WHERE member_id = ?";
        return jdbcTemplate.query(query, MEMBER_ROW_MAPPER, id).stream()
                .findFirst();
    }

    @Override
    public Optional<Member> findByEmailAndPassword(final String email, final String password) {
        String query = """
                SELECT *
                FROM member
                WHERE email = ? AND password = ?
                """;
        return jdbcTemplate.query(query, MEMBER_ROW_MAPPER, email, password)
                .stream()
                .findFirst();
    }

    @Override
    public boolean existsByEmail(final String email) {
        String query = "SELECT EXISTS (SELECT 1 FROM member WHERE email = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, email));
    }
}
