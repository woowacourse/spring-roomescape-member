package roomescape.repository;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Email;
import roomescape.domain.Member;
import roomescape.domain.Password;
import roomescape.domain.UserName;
import roomescape.exception.NotExistingEntryException;

@Repository
public class MemberJdbcRepository implements MemberRepository {

    private static final RowMapper<Member> memberRowMapper = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            new UserName(resultSet.getString("name")),
            new Email(resultSet.getString("email")),
            new Password(resultSet.getString("password"))
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberJdbcRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public List<Member> findAll() {
        String sql = "SELECT id, name, email, password FROM member";
        return jdbcTemplate.query(sql, memberRowMapper);
    }

    public Member findById(Long id) {
        String sql = "SELECT id, name, email, password FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper, id);
    }

    public Member findByEmail(String email) {
        String sql = "SELECT id, name, email, password FROM member WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper, email);
    }

    public Member findByEmailAndPassword(String email, String password) {
        String sql = "SELECT id, name, email, password FROM member WHERE email = ? AND password = ?";
        try {
            return jdbcTemplate.queryForObject(sql, memberRowMapper, email, password);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistingEntryException(email + "은 존재하지 않는 사용자입니다.");
        }
    }
}
