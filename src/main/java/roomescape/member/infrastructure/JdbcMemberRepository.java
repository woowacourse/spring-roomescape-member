package roomescape.member.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.repository.MemberRepository;

@Repository
public class JdbcMemberRepository implements MemberRepository {
    private final static RowMapper<Member> MEMBER_ROW_MAPPER =
            (rs, rowNum) -> new Member(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name")
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Member member) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("members")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = Map.of(
                "email", member.getEmail(),
                "password", member.getPassword(),
                "name", member.getName()
        );

        Number key = simpleJdbcInsert.executeAndReturnKey(params);
        return key.longValue();
    }

    @Override
    public Optional<Member> findById(Long id) {
        String selectOneSql = "SELECT id, email, password, name FROM members WHERE id=?";
        try {
            Member member = jdbcTemplate.queryForObject(selectOneSql, MEMBER_ROW_MAPPER,
                    id);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String selectOneSql = "SELECT id, email, password, name FROM members WHERE email=?";
        try {
            Member member = jdbcTemplate.queryForObject(selectOneSql, MEMBER_ROW_MAPPER,
                    email);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        String selectAll = "SELECT id, email, password, name FROM members";
        return jdbcTemplate.query(selectAll, MEMBER_ROW_MAPPER);
    }
}
