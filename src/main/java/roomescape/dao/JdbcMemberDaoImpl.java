package roomescape.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class JdbcMemberDaoImpl implements MemberDao {

    private static final int MEMBER_INITIAL_CAPACITY = 4;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcMemberDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(final Member member) {
        Map<String, Object> parameters = new HashMap<>(MEMBER_INITIAL_CAPACITY);
        parameters.put("name", member.getName());
        parameters.put("email", member.getEmail());
        parameters.put("password", member.getPassword());
        parameters.put("role", member.getRole());

        Number number = insertActor.executeAndReturnKey(parameters);
        return getGeneratedId(number);
    }

    private long getGeneratedId(final Number number) {
        return number.longValue();
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        String query = "select * from member where email = ?";
        try {
            Member member = jdbcTemplate.queryForObject(query,
                    (rs, rowNum) -> Member.from(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"))
                    , email);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findById(final Long id) {
        String query = "select * from member where id = ?";
        try {
            Member member = jdbcTemplate.queryForObject(query,
                    (rs, rowNum) -> Member.from(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"))
                    , id);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
