package roomescape.dao;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insert(String name, String email, String password) {
        String insertSql = "INSERT INTO member(name, email, password) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    insertSql,
                    new String[]{"id"});
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Optional<Member> findByEmail(String email) {
        String findByEmailSql = "SELECT id, name, role, email, password FROM member WHERE email = ?";
        List<Member> members = jdbcTemplate.query(findByEmailSql,
                (resultSet, numRow) -> new Member(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("role"),
                        resultSet.getString("email"),
                        resultSet.getString("password"))
                , email);
        return Optional.ofNullable(DataAccessUtils.singleResult(members));
    }

    public Optional<Member> findById(Long id) {
        String findByEmailSql = "SELECT id, name, role, email, password FROM member WHERE id = ?";
        List<Member> members = jdbcTemplate.query(findByEmailSql,
                (resultSet, numRow) -> new Member(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("role"),
                        resultSet.getString("email"),
                        resultSet.getString("password"))
                , id);
        return Optional.ofNullable(DataAccessUtils.singleResult(members));
    }

    public List<Member> findAll() {
        String findAllSql = "SELECT id, name, role, email, password FROM member";
        return jdbcTemplate.query(findAllSql,
                (resultSet, numRow) -> new Member(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("role"),
                        resultSet.getString("email"),
                        resultSet.getString("password")));
    }
}
