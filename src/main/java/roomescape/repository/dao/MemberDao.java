package roomescape.repository.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import roomescape.domain.Member;

@Component
@RequiredArgsConstructor
public class MemberDao {

    private static final RowMapper<Member> DEFAULT_ROW_MAPPER = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            resultSet.getString("role")
    );

    private final JdbcTemplate jdbcTemplate;

    public Member insertAndGet(Member member) {
        String insertQuery = "INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[] {"id"});
            ps.setString(1, member.name());
            ps.setString(2, member.email());
            ps.setString(3, member.password());
            ps.setString(4, member.role().name());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();

        return member.withId(id);
    }

    public Optional<Member> selectById(Long id) {
        String selectQuery = "SELECT id, name, email, password, role FROM member WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(selectQuery, DEFAULT_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> selectByEmail(String email) {
        String selectQuery = "SELECT id, name, email, password, role FROM member WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(selectQuery, DEFAULT_ROW_MAPPER, email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Member> selectAll() {
        String selectQuery = "SELECT id, name, email, password, role FROM member";
        return jdbcTemplate.query(selectQuery, DEFAULT_ROW_MAPPER);
    }
}
