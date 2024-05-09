package roomescape.member.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> rowMapper;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = (resultSet, rowNum) -> new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email")
        );
    }

    public List<Member> findMembers() {
        String sql = "SELECT id, name, email FROM member";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
