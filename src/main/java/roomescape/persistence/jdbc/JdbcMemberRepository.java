package roomescape.persistence.jdbc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.domain.Member;
import roomescape.persistence.MemberRepository;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(Member member) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());
        parameters.put("email", member.getEmail());
        parameters.put("password", member.getPassword());
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public Optional<Member> findById(Long id) {
        String query = """
                SELECT id, name, email, password
                FROM member
                WHERE id = ?""";
        return jdbcTemplate.query(
                        query,
                        (rs, rowNum) -> new Member(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password")
                        ),
                        id
                )
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String query = """
                SELECT id, name, email, password
                FROM member
                WHERE email = ?""";
        return jdbcTemplate.query(
                        query,
                        (rs, rowNum) -> new Member(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password")
                        ),
                        email
                )
                .stream()
                .findFirst();
    }
}
