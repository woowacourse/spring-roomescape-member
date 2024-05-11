package roomescape.repository.repositoryImpl;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.repository.MemberDao;

@Repository
public class JdbcMemberDao implements MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> memberRowMapper = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            Role.from(resultSet.getString("role"))
    );

    public JdbcMemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM member";
        return jdbcTemplate.query(sql, memberRowMapper);
    }

    @Override
    public Member findByEmail(final String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, memberRowMapper, email);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 사용자입니다.");
        }
    }

    @Override
    public Member findById(final Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, memberRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 사용자입니다.");
        }
    }
}
