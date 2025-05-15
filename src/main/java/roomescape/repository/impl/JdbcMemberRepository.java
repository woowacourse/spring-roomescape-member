package roomescape.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.mapper.MemberMapper;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.repository.MemberRepository;

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

    public List<Member> readAll() {
        final String query = """
                SELECT id, name, email, role
                FROM member
                """;

        return jdbcTemplate.query(
                query,
                new MemberMapper()
        );
    }

    public Member save(Member member) {
        Map<String, Object> parameters = Map.ofEntries(
                Map.entry("name", member.getName()),
                Map.entry("email", member.getEmail()),
                Map.entry("password", member.getPassword()),
                Map.entry("role", member.getRole())
        );

        Long generatedKey = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return Member.generateWithPrimaryKey(member, generatedKey);
    }

    // * [고민] 파라미터: String email VS Member member
    // ? 필요한 정보는 String 타입 email 뿐인데 Member 전체를 받는 것은 불필요한 데이터 전송 아닌가?
    // ? 정확하게 원하는 것은 **이메일**이 아니라, 멤버의 이메일이기에 Member 전체를 받고 내부에서 분해하는 방법이 더 좋은 것 같다고 판단했다.
    public boolean existsByEmail(Member member) {
        final String query = """
                SELECT EXISTS(
                    SELECT id
                    FROM member
                    WHERE email = ?
                )
                """;

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                query,
                Boolean.class,
                member.getEmail()
        ));
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        final String query = """
                SELECT id, name, email, password, role
                FROM member
                WHERE email = ? AND password = ?
                """;

        return jdbcTemplate.query(
                query,
                new MemberMapper(),
                email,
                password
                ).stream().findFirst();
    }

    public Optional<Member> findById(Long id) {
        final String query = """
                SELECT id, name, email, role
                FROM member
                WHERE id = ?
                """;

        return jdbcTemplate.query(
                query,
                new MemberMapper(),
                id
        ).stream().findFirst();
    }
}
