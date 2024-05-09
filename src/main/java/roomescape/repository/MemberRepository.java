package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Member> memberRowMapper = (resultSet, rowNum) -> new Member(
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );


    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsByEmailAndPassword(String email, String password) {
        String sql = "SELECT exists(" +
                "SELECT 1 " +
                "FROM member " +
                "WHERE email = ? " +
                "AND password = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, email, password);
    }
}
