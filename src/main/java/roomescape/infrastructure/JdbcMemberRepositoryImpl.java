package roomescape.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.domain.vo.MemberEmail;
import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.MemberPassword;
import roomescape.domain.vo.MemberRole;

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
            Map.entry("name", member.getName().getValue()),
            Map.entry("email", member.getEmail().getValue()),
            Map.entry("password", member.getPassword().getValue()),
            Map.entry("role", member.getRole().getValue())
        );

        long id = simpleJdbcInsert
            .executeAndReturnKey(saveSource)
            .longValue();

        return new Member(id, member.getName(), member.getEmail(), member.getPassword(), member.getRole());
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, getMemberRowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isExistsByEmail(String email) {
        String sql = "SELECT EXISTS(SELECT id FROM member WHERE email = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, email);
    }

    @Override
    public boolean isExistsByEmailAndPassword(String email, String password) {
        String sql = "SELECT EXISTS(SELECT id FROM member WHERE email = ? AND password = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, email, password);
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

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, getMemberRowMapper(), email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM member";

        return jdbcTemplate.query(sql, getMemberRowMapper());
    }


    private RowMapper<Member> getMemberRowMapper() {
        return (rs, rowNum) -> new Member(
            rs.getLong("id"),
            new MemberName(rs.getString("name")),
            new MemberEmail(rs.getString("email")),
            new MemberPassword(rs.getString("password")),
            new MemberRole(rs.getString("role")));
    }
}
