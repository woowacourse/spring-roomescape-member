package roomescape.dao;

import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class JdbcMemberDaoImpl implements MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcMemberDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
            .withTableName("member")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public long save(Member member) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("name", member.getName());
        parameters.put("email", member.getEmail());
        parameters.put("password", member.getPassword());
        Number newId = insertActor.executeAndReturnKey(parameters);
        return newId.longValue();
    }
}
