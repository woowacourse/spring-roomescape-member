package roomescape.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import roomescape.domain.Member;
import roomescape.domain.Role;

@Repository
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> rowMapper;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = (resultSet, rowNum) -> new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role"))
        );
    }

    public List<Member> readMemberOnlyMember() {
        String sql = "SELECT id, name, email, password, role FROM member WHERE role = ?";
        return jdbcTemplate.query(sql, rowMapper, "MEMBER");
    }

    public Optional<Member> readMemberById(Long id) {
        String sql = "SELECT id, name, email, password, role FROM member WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<Member> readMemberByEmailAndPassword(String email, String password) {
        String sql = "SELECT id, name, email, password, role FROM member WHERE email = ? AND password = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, email, password));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public boolean existsMemberByEmail(String email) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM member
                    WHERE email = ?
                )
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, email);
    }

    public Member createMember(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO member(name, email, password) VALUES (?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, member.getName());
            preparedStatement.setString(2, member.getEmail());
            preparedStatement.setString(3, member.getPassword());
            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return member.createWithId(id);
    }
}
