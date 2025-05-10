package roomescape.repository.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;

@Repository
public class H2MemberRepository implements MemberRepository {

    private static final RowMapper<Member> mapper;
    private final JdbcTemplate template;
    private final SimpleJdbcInsert insertActor;

    static {
        mapper = (resultSet, resultNumber) -> new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                MemberRole.valueOf(resultSet.getString("role"))
        );
    }

    public H2MemberRepository(DataSource dataSource, JdbcTemplate template) {
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
        this.template = template;
    }

    @Override
    public List<Member> findAllByRole(MemberRole role) {
        String sql = """
                SELECT *
                FROM member
                WHERE member.role LIKE ?
                ORDER BY member.id ASC
                """;
        return template.query(sql, mapper, role.toString());
    }

    @Override
    public Optional<Member> findById(long id) {
        String sql = """
                SELECT *
                FROM member
                WHERE member.id = ?
                ORDER BY member.id ASC
                """;
        try {
            Member member = template.queryForObject(sql, mapper, id);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = """
                SELECT * FROM member
                WHERE member.email = ?
                ORDER BY member.id ASC
                """;
        try {
            Member member = template.queryForObject(sql, mapper, email);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public long add(Member member) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());
        parameters.put("email", member.getEmail());
        parameters.put("password", member.getPassword());
        parameters.put("role", member.getRole());
        return insertActor.executeAndReturnKey(parameters).longValue();
    }
}
