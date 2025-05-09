package roomescape.member.infrastructure;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;

@Repository
public class JdbcMemberRepository {

    private static final RowMapper<Member> ROW_MAPPER = (resultSet, rowNum) -> Member.createWithId(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcMemberRepository(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Member member) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());
        parameters.put("email", member.getEmail());
        parameters.put("password", member.getPassword());

        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    public Member findById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
    }

    public boolean existByEmail(String email) {
        String sql = "SELECT EXISTS (SELECT 1 FROM member WHERE email = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, email);
    }
}
