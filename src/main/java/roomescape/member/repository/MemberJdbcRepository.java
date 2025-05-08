package roomescape.member.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;

@Repository
public class MemberJdbcRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member add(Member member) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", member.getName());
        params.put("email", member.getEmail());
        params.put("password", member.getPassword());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return Member.createWithId(id, member.getName(), member.getEmail(), member.getPassword());
    }

    @Override
    public Optional<Member> findIdByEmailAndPassword(String email, String password) {
        String sql = "select * from member where email = ? and password = ?";
        List<Member> members = jdbcTemplate.query(
                sql,
                (resultSet, rowNum) ->
                        Member.createWithId(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("email"),
                                resultSet.getString("password")),
                email, password
        );
        return members.stream().findFirst();
    }
}
