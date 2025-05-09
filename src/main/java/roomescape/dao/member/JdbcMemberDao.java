package roomescape.dao.member;

import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Role;

@Repository
public class JdbcMemberDao implements MemberDao{

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> memberRowMapper = (resultSet, rowNum) -> {
        Role role = new Role(
                resultSet.getLong("role_id"),
                resultSet.getString("role_name")
        );
        return new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                role
                );
    };

    public JdbcMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String sql = """
                SELECT m.id, m.name, m.email, m.password, r.id as role_id, r.name as role_name
                FROM member m
                INNER JOIN role r
                    ON m.role_id = r.id
                WHERE m.email = ? AND m.password = ?
                """;
        return jdbcTemplate.query(sql, memberRowMapper, email, password).stream().findFirst();
    }
}
