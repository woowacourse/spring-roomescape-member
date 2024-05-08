package roomescape.infrastructure;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.MemberEmail;
import roomescape.domain.MemberPassword;
import roomescape.domain.MemberRepository;

@Repository
public class JdbcMemberRepositoryImpl implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcMemberRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(Objects.requireNonNull(jdbcTemplate.getDataSource()))
            .withTableName("member")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(Member member) {
        Map<String, Object> saveSource = Map.ofEntries(
            Map.entry("name", member.getName()),
            Map.entry("email", member.getEmail().getValue()),
            Map.entry("password", member.getPassword().getValue())
        );

        long id = simpleJdbcInsert
            .executeAndReturnKey(saveSource)
            .longValue();

        return new Member(id, member.getName(), member.getEmail(), member.getPassword());
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM member WHERE email = ? AND password = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, getMemberRowMapper(), email, password));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Member> getMemberRowMapper() {
        return (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("name"),
            new MemberEmail(rs.getString("email")),
            new MemberPassword(rs.getString("password")));
    }
}
