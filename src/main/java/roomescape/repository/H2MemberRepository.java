package roomescape.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.domain.Role;

@Repository
public class H2MemberRepository implements MemberRepository {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String ROLE = "role";

    private final MemberRowMapper rowMapper;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public H2MemberRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.rowMapper = new MemberRowMapper();
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("user_table")
                .usingGeneratedKeyColumns(ID);
    }

    public Member save(Member member) {
        Long memberId = jdbcInsert.executeAndReturnKey(Map.of(
                        NAME, member.getName(),
                        EMAIL, member.getEmail(),
                        PASSWORD, member.getPassword(),
                        ROLE, member.getRole().name()
        )).longValue();

        return new Member(
                memberId,
                member.getName(),
                member.getEmail(),
                member.getPassword(),
                member.getRole()
        );
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM user_table WHERE email = ? AND password = ?";
        try {
            Member savedMember = jdbcTemplate.queryForObject(sql, rowMapper, email, password);
            return Optional.ofNullable(savedMember);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(Long memberId) {
        String sql = "SELECT * FROM user_table WHERE id = ?";
        try {
            Member savedMember = jdbcTemplate.queryForObject(sql, rowMapper, memberId);
            return Optional.ofNullable(savedMember);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Member> findAll() {
        String sql = "SELECT * FROM user_table";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public boolean existsByEmail(Member member) {
        String sql = "SELECT EXISTS (SELECT 1 FROM user_table WHERE email = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, member.getEmail()));
    }

    private static class MemberRowMapper implements RowMapper<Member> {
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(
                    rs.getLong(ID),
                    rs.getString(NAME),
                    rs.getString(EMAIL),
                    rs.getString(PASSWORD),
                    Role.valueOf(rs.getString(ROLE))
            );
        }
    }
}
