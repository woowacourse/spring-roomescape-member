package roomescape.member.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.entity.MemberEntity;

@Repository
public class JDBCMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JDBCMemberRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Member> getAll() {
        return jdbcTemplate.query(
                "SELECT id, name, email, password FROM member",
                (resultSet, rowNum) -> {
                    MemberEntity entity = new MemberEntity(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    );
                    return entity.toMember();
                }
        );
    }

    @Override
    public Member put(final Member member) {
        long generatedId = simpleJdbcInsert.executeAndReturnKey(
                Map.of("name", member.getName(),
                        "email", member.getEmail(),
                        "password", member.getPassword())).longValue();

        return Member.of(generatedId, member.getName(), member.getEmail(), member.getPassword());
    }

    @Override
    public boolean deleteById(final long id) {
        return jdbcTemplate.update("DELETE FROM member WHERE id = ?", id) != 0;
    }

    @Override
    public Optional<Member> findById(final long id) {
        try {
            MemberEntity memberEntity = jdbcTemplate.queryForObject(
                    "SELECT id, name, email, password FROM member WHERE id = ?",
                    (resultSet, rowNum) -> new MemberEntity(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
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
                "SELECT id, name, email, password FROM member WHERE email = ? AND password = ?",
                (resultSet, rowNum) -> new MemberEntity(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                ),
                email, password
        );
        return Optional.ofNullable(memberEntity)
                .map(MemberEntity::toMember);
    }
}
