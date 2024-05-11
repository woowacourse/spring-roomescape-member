package roomescape.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class JdbcMemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> memberRowMapper = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member findById(long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        Member member = jdbcTemplate.queryForObject(sql, memberRowMapper, id);
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다");
        }

        return member;
    }

    // TODO: 예외 처리 통일하기
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM member WHERE email = ? AND password = ?";
        List<Member> members = jdbcTemplate.query(sql, memberRowMapper, email, password);
        if (members.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(members.get(0));
    }
}
