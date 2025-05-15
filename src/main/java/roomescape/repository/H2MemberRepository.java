package roomescape.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class H2MemberRepository implements MemberRepository {

    private static final RowMapper<Member> mapper;

    static {
        mapper = (resultSet, resultNumber) -> new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("role")
        );
    }

    private final JdbcTemplate template;
    private SimpleJdbcInsert insertMember;

    public H2MemberRepository(final JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public long add(final Member member) {
        insertMember = initializeSimpleJdbcInsert();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());
        parameters.put("email", member.getEmail());
        parameters.put("password", member.getPassword());
        parameters.put("role", member.getRole());

        return insertMember.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM member";
        return template.query(sql, mapper);
    }

    @Override
    public Optional<Member> findMemberByEmailAndPassword(String email, String password) {
        String sql =
                """ 
                        SELECT * 
                        FROM member
                        WHERE email = ?
                        AND password = ?
                        """;
        try {
            Member member = template.queryForObject(sql, mapper, email, password);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findMemberById(final Long id) {
        String sql = """ 
                SELECT * 
                FROM member
                WHERE id = ?
                """;
        try {
            Member member = template.queryForObject(sql, mapper, id);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private SimpleJdbcInsert initializeSimpleJdbcInsert() {
        if (insertMember == null) {
            this.insertMember = new SimpleJdbcInsert(template)
                    .withTableName("member")
                    .usingGeneratedKeyColumns("id");
        }
        return insertMember;
    }
}
