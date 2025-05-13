package roomescape.persistence.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.domain.member.Member;
import roomescape.business.domain.member.MemberCredential;
import roomescape.business.domain.member.SignUpMember;
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
    public Long save(SignUpMember signUpMember) {
        MemberEntity memberEntity = new MemberEntity(
                null,
                signUpMember.getName(),
                signUpMember.getEmail(),
                signUpMember.getPassword(),
                signUpMember.getRole().value()
        );
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
    public Optional<MemberCredential> findCredentialByEmail(String email) {
        String query = """
                SELECT id, email, password
                FROM member
                WHERE email = ?""";
        return jdbcTemplate.query(
                        query,
                        (rs, rowNum) -> new MemberCredential(
                                rs.getLong("id"),
                                rs.getString("email"),
                                rs.getString("password")
                        ),
                        email
                )
                .stream()
                .findFirst();
    }

    @Override
    public List<Member> findAll() {
        String query = """
                SELECT id, name, email, password, role
                FROM member""";
        return jdbcTemplate.query(
                        query,
                        (rs, rowNum) -> new MemberEntity(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getString("role")
                        )
                )
                .stream()
                .map(MemberEntity::toDomain)
                .toList();
    }

    @Override
    public boolean existsByEmail(String email) {
        String query = """
                SELECT EXISTS (
                    SELECT 1
                    FROM member
                    WHERE email = ?
                )""";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, email));
    }
}
