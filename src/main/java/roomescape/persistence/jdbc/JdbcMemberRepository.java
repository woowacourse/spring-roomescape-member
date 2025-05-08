package roomescape.persistence.jdbc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.domain.member.Member;
import roomescape.persistence.MemberRepository;
import roomescape.persistence.entity.MemberEntity;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(Member member) {
        MemberEntity memberEntity = MemberEntity.fromDomain(member);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", memberEntity.getName());
        parameters.put("email", memberEntity.getEmail());
        parameters.put("password", memberEntity.getPassword());
        parameters.put("role", memberEntity.getRole());
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public Optional<Member> findById(Long id) {
        String query = """
                SELECT id, name, email, password, role
                FROM member
                WHERE id = ?""";
        return jdbcTemplate.query(
                        query,
                        (rs, rowNum) -> new MemberEntity(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getString("role")
                        ),
                        id
                )
                .stream()
                .findFirst()
                .map(MemberEntity::toDomain);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String query = """
                SELECT id, name, email, password, role
                FROM member
                WHERE email = ?""";
        return jdbcTemplate.query(
                        query,
                        (rs, rowNum) -> new MemberEntity(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getString("role")
                        ),
                        email
                )
                .stream()
                .findFirst()
                .map(MemberEntity::toDomain);
    }
}
