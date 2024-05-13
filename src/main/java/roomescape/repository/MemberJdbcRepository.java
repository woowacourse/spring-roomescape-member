package roomescape.repository;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Role;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MemberJdbcRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<Member> memberRowMapper = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            Role.from(resultSet.getString("role")),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );

    public MemberJdbcRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT id, role, name, email, password FROM member";
        return jdbcTemplate.query(sql, memberRowMapper);
    }

    @Override
    public Member findByEmail(String email) {
        try {
            String sql = "SELECT id, role, name, email, password FROM member WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, memberRowMapper, email);
        } catch(IncorrectResultSizeDataAccessException e) {
            throw new IllegalArgumentException("존재하지 않는 이메일입니다.");
        }
    }

    @Override
    public Member findById(Long id) {
        try {
            String sql = "SELECT id, role, name, email, password FROM member WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, memberRowMapper, id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new IllegalArgumentException("존재하지 않는 이메일입니다.");
        }
    }
}
