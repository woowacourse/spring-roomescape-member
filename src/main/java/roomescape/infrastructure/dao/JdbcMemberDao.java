package roomescape.infrastructure.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.model.Member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcMemberDao implements MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private static final RowMapper<Member> ROW_MAPPER = (resultSet, rowNum) ->
            new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role")
            );

    public JdbcMemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(Member member) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());
        parameters.put("email", member.getEmail());
        parameters.put("password", member.getPassword());
        parameters.put("role", member.getRole().getRole());

        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public boolean existByEmail(final String email) {
        String query = "SELECT EXISTS (SELECT 1 FROM member WHERE email = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, email);
    }

    @Override
    public Member findByEmail(String email) {
        String query = "SELECT * FROM member WHERE email = ?";
        return jdbcTemplate.queryForObject(
                query,
                ROW_MAPPER,
                email);
    }

    @Override
    public Member findById(final Long memberId) {
        String query = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(
                query,
                ROW_MAPPER,
                memberId);
    }

    @Override
    public List<Member> findAll() {
        String query = "SELECT * FROM member";
        return jdbcTemplate.query(query, ROW_MAPPER);
    }
}
