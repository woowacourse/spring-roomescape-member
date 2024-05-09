package roomescape.dao.member;

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
    private final RowMapper<Member> userRowMapper;

    public MemberDao(final DataSource dataSource, final RowMapper<Member> userRowMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.userRowMapper = userRowMapper;
    }

    public Optional<Member> findByEmail(final String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, userRowMapper, email));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    // TODO: 여기서 USER 를 매개변수로 바로 반환하는 게 맞나? SimpleJdbcInsert 사용하면 좋을 듯
    public Member save(final Member member) {
        String sql = "INSERT INTO user VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, member.getNameValue(), member.getEmail(), member.getPassword());
        return member;
    }
}
