package roomescape.member.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.auth.domain.Role;
import roomescape.member.domain.Member;
import roomescape.member.entity.MemberEntity;

@Repository
public class JDBCMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JDBCMemberRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query(
                "SELECT id, name, email, password, role FROM member",
                (resultSet, rowNum) -> {
                    MemberEntity entity = new MemberEntity(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            Role.valueOf(resultSet.getString("role"))
                    );
                    return entity.toMember();
                }
        );
    }

    @Override
    public Member save(final Member member) {
        long generatedId = simpleJdbcInsert.executeAndReturnKey(
                Map.of("name", member.getName(),
                        "email", member.getEmail(),
                        "password", member.getPassword(),
                        "role", member.getRole()
                )).longValue();

        return Member.of(generatedId, member.getName(), member.getEmail(), member.getPassword(), member.getRole());
    }

    @Override
    public boolean deleteById(final long id) {
        return jdbcTemplate.update("DELETE FROM member WHERE id = ?", id) != 0;
    }

    @Override
    public Optional<Member> findById(final long id) {
        try {
            MemberEntity memberEntity = jdbcTemplate.queryForObject(
                    "SELECT id, name, email, password, role FROM member WHERE id = ?",
                    (resultSet, rowNum) -> new MemberEntity(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            Role.valueOf(resultSet.getString("role"))
                    ), id
            );
            return Optional.ofNullable(memberEntity)
                    .map(MemberEntity::toMember);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmailAndPassword(final String email, final String password) {
        MemberEntity memberEntity = jdbcTemplate.queryForObject(
                "SELECT id, name, email, password, role FROM member WHERE email = ? AND password = ?",
                (resultSet, rowNum) -> new MemberEntity(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role"))
                ),
                email, password
        );
        return Optional.ofNullable(memberEntity)
                .map(MemberEntity::toMember);
    }
}
