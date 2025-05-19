package roomescape.infrastructure.jdbctemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.Member;
import roomescape.business.MemberRole;
import roomescape.persistence.MemberRepository;

@Repository
public class H2MemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public H2MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Member> memberRowMapper = (rs, rowNum) -> (
            new Member(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    MemberRole.valueOf(rs.getString("role"))
            )
    );

    @Override
    public List<Member> findAll() {
        String query = """
                SELECT id, name, email, password, role
                FROM member
                """;
        return jdbcTemplate.query(query, memberRowMapper);
    }

    @Override
    public Optional<Member> findById(Long id) {
        String query = """
                SELECT id, name, email, password, role
                FROM member
                WHERE id = ?
                """;
        return jdbcTemplate.query(query, memberRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String query = """
                SELECT id, name, email, password, role
                FROM member
                WHERE email = ?
                """;
        return jdbcTemplate.query(query, memberRowMapper, email)
                .stream()
                .findFirst();
    }

    @Override
    public boolean existsByEmail(String email) {
        String query = """
                SELECT EXISTS (
                    SELECT 1
                    FROM member
                    WHERE email = ?
                )
                """;
        Boolean result = jdbcTemplate.queryForObject(query, Boolean.class, email);
        return Boolean.TRUE.equals(result);
    }

    @Override
    public Long add(Member member) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());
        parameters.put("email", member.getEmail());
        parameters.put("password", member.getPassword());
        parameters.put("role", member.getRole().toString());
        return (Long) jdbcInsert.executeAndReturnKey(parameters);
    }
}
