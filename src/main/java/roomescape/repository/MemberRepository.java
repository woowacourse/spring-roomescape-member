package roomescape.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.controller.response.UserResponse;
import roomescape.domain.User;

@Repository
public class MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("user_table")
                .usingGeneratedKeyColumns("id");
    }

    public User save(User user) {
        Long userId = jdbcInsert.executeAndReturnKey(Map.of(
                        "name", user.getName(),
                        "email", user.getEmail(),
                        "password", user.getPassword()))
                .longValue();

        return new User(
                userId,
                user.getName(),
                user.getEmail(),
                user.getPassword());
    }

    public boolean checkExistMember(String email, String password) {
        String sql = """
                SELECT 
                CASE WHEN EXISTS (
                        SELECT 1
                        FROM user_table
                        WHERE email = ? AND password = ?
                    )
                    THEN TRUE
                    ELSE FALSE
                END
                """;
        boolean b = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, email, password));
        return b;
    }

    public UserResponse findByEmail(String email) {
        String sql = """
                SELECT
                    id, name, email, password
                FROM
                    user_table
                WHERE
                    email = ?
                """;
        UserResponse userResponse = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new UserResponse(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password")
        ), email);
        return userResponse;
    }

    public Optional<UserResponse> findById(Long memberId) {
        String sql = """
                SELECT
                    id, name, email, password
                FROM
                    user_table
                WHERE
                    id = ?
                """;
        UserResponse userResponse = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new UserResponse(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password")
        ), memberId);
        return Optional.ofNullable(userResponse);
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM user_table";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password")
        ));
    }
}
