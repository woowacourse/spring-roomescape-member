package roomescape.dao.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;

@Repository
public class JdbcMemberDaoImpl implements MemberDao {

    private static final int MEMBER_INITIAL_CAPACITY = 4;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcMemberDaoImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(final Member member) {
        final Map<String, Object> parameters = new HashMap<>(MEMBER_INITIAL_CAPACITY);
        parameters.put("name", member.getName());
        parameters.put("email", member.getEmail());
        parameters.put("password", member.getPassword());
        parameters.put("role", member.getRole());

        final Number number = insertActor.executeAndReturnKey(parameters);
        return getGeneratedId(number);
    }

    private long getGeneratedId(final Number number) {
        return number.longValue();
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        final String query = "select * from member where email = ?";
        try {
            final Member member = jdbcTemplate.queryForObject(query, getMemberRowMapper(), email);
            return Optional.ofNullable(member);
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findById(final Long id) {
        final String query = "select * from member where id = ?";
        try {
            final Member member = jdbcTemplate.queryForObject(query, getMemberRowMapper(), id);
            return Optional.ofNullable(member);
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        final String query = "select * from member";
        return jdbcTemplate.query(query, getMemberRowMapper());
    }

    private RowMapper<Member> getMemberRowMapper() {
        return (rs, rowNum) -> Member.fromWithoutPassword(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                MemberRole.from(rs.getString("role"))
        );
    }

}
