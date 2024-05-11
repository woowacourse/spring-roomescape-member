package roomescape.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.Role;

@Repository
public class JdbcMemberDao implements MemberDao {

    private static final RowMapper<Member> MEMBER_ROW_MAPPER =
            (resultSet, rowNum) -> new Member(
                    resultSet.getLong("id"),
                    new MemberName(resultSet.getString("name")),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    Role.valueOf(resultSet.getString("role"))
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Member> readAll() {
        String sql = "SELECT id, name, email, password, role FROM member";
        return jdbcTemplate.query(sql, MEMBER_ROW_MAPPER);
    }

    @Override
    public Optional<Member> readById(Long id) {
        String sql = "SELECT id, name, email, password, role FROM member WHERE id = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, id);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> readByEmailAndPassword(String email, String password) {
        String sql = "SELECT id, name, email, password, role FROM member WHERE email = ? AND password = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, email, password);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
