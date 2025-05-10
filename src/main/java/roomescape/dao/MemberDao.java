package roomescape.dao;

import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.model.Member;

@Repository
public class MemberDao {

    private final RowMapper<Member> customerRowMapper = (resultSet, rowNum) -> {
        return new Member(
                resultSet.getLong("id"),
                resultSet.getString("email"),
                resultSet.getString("name"),
                resultSet.getString("password"));
    };

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsByEmailAndPassword(Member member){
        String sql = "SELECT EXISTS(SELECT 1 FROM member WHERE email = ? AND password = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, member.getEmail(), member.getPassword()));
    }

    public Optional<Member> findById(Long customerId) {
        String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.query(sql, customerRowMapper, customerId)
                .stream().
                findFirst();
    }
}
