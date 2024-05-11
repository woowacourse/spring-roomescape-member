package roomescape.repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class JdbcMemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Member> memberRowMapper = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public List<Member> findAll() {
        String sql = "SELECT * FROM member";
        List<Member> reservations = jdbcTemplate.query(sql, memberRowMapper);

        return Collections.unmodifiableList(reservations);
    }

    public Member findById(long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        Member member = jdbcTemplate.queryForObject(sql, memberRowMapper, id);
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다");
        }

        return member;
    }

    public Member findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM member WHERE email = ? AND password = ?";
        Member member = jdbcTemplate.queryForObject(sql, memberRowMapper, email, password);
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다");
        }

        return member;
    }

    public Member save(Member member) {
        Map<String, Object> params = Map.of(
                "name", member.getName(),
                "email", member.getEmail(),
                "password", member.getEmail()
        );
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Member(id, member.getName(), member.getEmail(), member.getPassword());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }
}
