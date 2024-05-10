package roomescape.repository.member;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;

@Repository
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> memberRowMapper;

    public MemberDao(final DataSource dataSource, final RowMapper<Member> memberRowMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.memberRowMapper = memberRowMapper;
    }

    public Member save(final Member member) {
        String sql = "INSERT INTO member(member_name, email, password, member_role) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, member.getNameValue(), member.getEmail(), member.getPassword(), "MEMBER");
        return member;
    }

    public List<Member> getAll() {
        return jdbcTemplate.query("SELECT * FROM member", memberRowMapper);
    }

    public Optional<Member> findById(final long id) {
        String sql = "SELECT member.id AS member_id, member_name, email, password, member_role FROM member WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, memberRowMapper, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<Member> findByEmail(final String email) {
        String sql = "SELECT member.id AS member_id, member_name, email, password, member_role FROM member WHERE email = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, memberRowMapper, email));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
