package roomescape.member.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

@Repository
public class JdbcMemberDao implements MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Member> memberRowMapper = (resultSet, rowNum) -> {
        Role role = Role.getRoleById(resultSet.getLong("role_id"));
        return new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                role
        );
    };

    public JdbcMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String sql = """
                SELECT m.id, m.name, m.email, m.password, r.id as role_id, r.name as role_name
                FROM member m
                INNER JOIN role r
                    ON m.role_id = r.id
                WHERE m.email = ? AND m.password = ?
                """;
        return jdbcTemplate.query(sql, memberRowMapper, email, password).stream().findFirst();
    }

    @Override
    public Member create(Member member) {
        Map<String, Object> parameters = new HashMap<>(Map.of(
                "name", member.getName(),
                "email", member.getEmail(),
                "password", member.getPassword(),
                "role_id", member.getRole().getId()));
        Number number = simpleJdbcInsert.executeAndReturnKey(parameters);
        return new Member(number.longValue(), member.getName(), member.getEmail(), member.getPassword(),
                member.getRole());
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = """
                SELECT m.id, m.name, m.email, m.password, r.id as role_id, r.name as role_name
                FROM member m
                INNER JOIN role r
                    ON m.role_id = r.id
                WHERE m.email = ?
                """;
        return jdbcTemplate.query(sql, memberRowMapper, email).stream().findFirst();
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = """
                SELECT m.id, m.name, m.email, m.password, r.id as role_id, r.name as role_name
                FROM member m
                INNER JOIN role r
                    ON m.role_id = r.id
                WHERE m.id = ?
                """;
        return jdbcTemplate.query(sql, memberRowMapper, id).stream().findFirst();
    }

    @Override
    public List<Member> findAll() {
        String sql = """
                SELECT m.id, m.name, m.email, m.password, r.id as role_id, r.name as role_name
                FROM member m
                INNER JOIN role r
                    ON m.role_id = r.id
                """;
        return jdbcTemplate.query(sql, memberRowMapper);
    }
}
