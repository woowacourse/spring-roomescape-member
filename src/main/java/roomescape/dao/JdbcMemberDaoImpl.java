package roomescape.dao;

import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class JdbcMemberDaoImpl implements MemberDao {

    private static final int MEMBER_INITIAL_CAPACITY = 3;

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

        Number number = insertActor.executeAndReturnKey(parameters);
        return getGeneratedId(number);
    }

    private long getGeneratedId(final Number number) {
        return number.longValue();
    }
}
