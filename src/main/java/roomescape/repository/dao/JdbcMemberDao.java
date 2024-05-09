package roomescape.repository.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.model.Member;
import roomescape.model.Role;

import java.util.Optional;

@Repository
public class JdbcMemberDao implements MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> rowMapper = (resultSet, ignore) ->
            new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    Role.asRole(resultSet.getString("role")));

    public JdbcMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, name, email, password, role FROM member WHERE email = ?";
        Member member = jdbcTemplate.queryForObject(sql, rowMapper, email);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findById(long id) {
        String sql = "SELECT id, name, email, password, role FROM member WHERE id = ?";
        Member member = jdbcTemplate.queryForObject(sql, rowMapper, id);
        return Optional.ofNullable(member);
    }
}
