package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.dto.SignupRequest;

import java.util.List;

@Repository
public class MemberDao {
    private static final RowMapper<Member> rowMapper =
            (resultSet, rowNum) -> new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("email"),
                    resultSet.getString("name")
            ) {
            };

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Member> findAll() {
        String sql = "select id, email, name from member";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void create(final SignupRequest signupRequest) {
        String sql = "insert into member(email,password,name) values(?, HASH('SHA256', STRINGTOUTF8(?), 1000), ?)";
        jdbcTemplate.update(sql, signupRequest.email(), signupRequest.password(), signupRequest.name());
    }

    public boolean isExist(final SignupRequest signupRequest) {
        String sql = "select count(email) from member where email = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, signupRequest.email()) != 0;
    }
}
