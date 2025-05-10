package roomescape.member.repository;

import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.entity.MemberEntity;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private static final RowMapper<MemberEntity> MEMBER_ENTITY_ROW_MAPPER = (resultSet, rowNum) -> new MemberEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            resultSet.getString("role")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcMemberRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public boolean existsByEmailAndPassword(final String email, final String password) {
        try {
            return Boolean.TRUE.equals(
                    jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM member WHERE (email, password) = (?, ?))",
                            Boolean.class,
                            email,
                            password
                    ));
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public Optional<Member> findByMember(final String email, final String password) {
        try {
            MemberEntity memberEntity = jdbcTemplate.queryForObject(
                    "SELECT id, name, email, password, role FROM member WHERE (email, password) = (?, ?)",
                    MEMBER_ENTITY_ROW_MAPPER, email, password);
            return Optional.ofNullable(memberEntity)
                    .map(MemberEntity::toMember);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findById(final Long id) {
        try {
            MemberEntity memberEntity = jdbcTemplate.queryForObject(
                    "SELECT id, name, email, password, role FROM member WHERE id = ?",
                    MEMBER_ENTITY_ROW_MAPPER, id);
            return Optional.ofNullable(memberEntity)
                    .map(MemberEntity::toMember);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Member save(final Member member) {
        Long generatedId = simpleJdbcInsert.executeAndReturnKey(
                Map.of("name", member.getName(), "email", member.getEmail(), "password",
                        member.getPassword(), "role", member.getMemberRole().name())
        ).longValue();
        return Member.of(generatedId, member.getName(), member.getEmail(), member.getPassword(),
                member.getMemberRole());
    }
}
