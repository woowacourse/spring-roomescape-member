package roomescape.member.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMemberDao implements MemberRepository {

    private final RowMapper<Member> rowMapper = (rs, rowNum) ->
            new Member(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("role")
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT id, email, password, name, role FROM member WHERE email = ? AND password = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, email, password));
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, email, password, name, role FROM member WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT id, email, password, name, role FROM member";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Long saveAndReturnId(Member member) {
        String sql = "INSERT INTO member (email, password, name, role) VALUES(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, member.getEmail());
                    preparedStatement.setString(2, member.getPassword());
                    preparedStatement.setString(3, member.getName());
                    preparedStatement.setString(4, member.getRole().name());
                    return preparedStatement;
                },
                keyHolder
        );

        return keyHolder.getKey().longValue();
    }

}
