package roomescape.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.repository.MemberRepository;

import java.util.Map;

@Repository
public class JdbcMemberRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }


    @Override
    public Member save(Member member) {
        Map<String, Object> parameters = Map.ofEntries(
                Map.entry("name", member.getName()),
                Map.entry("email", member.getEmail()),
                Map.entry("password", member.getPassword())
        );

        Long generatedKey = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return Member.generateWithPrimaryKey(member, generatedKey);
    }
}
