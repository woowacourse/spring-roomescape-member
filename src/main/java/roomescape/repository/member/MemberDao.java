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

    // TODO: 여기서 USER 를 매개변수로 바로 반환하는 게 맞나? SimpleJdbcInsert 사용하면 좋을 듯
    public Member save(final Member member) {
        String sql = "INSERT INTO member(member_name, email, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, member.getNameValue(), member.getEmail(), member.getPassword());
        return member;
    }

    public List<Member> getAll() {
        return jdbcTemplate.query("SELECT * FROM member", memberRowMapper);
    }

    public Optional<Member> findById(final long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, memberRowMapper, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<Member> findByEmail(final String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, memberRowMapper, email));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
