package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.dto.SignupRequest;

import java.sql.PreparedStatement;
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

    public Long create(final SignupRequest signupRequest) {
        //TODO : salt 추가하기 - 현재 동일 패스워드로 저장하면 해시값 동일함
        String sql = "insert into member(email,password,name) values(?, HASH('SHA256', STRINGTOUTF8(?), 1000), ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, signupRequest.email());
            ps.setString(2, signupRequest.password());
            ps.setString(3, signupRequest.name());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public boolean isExist(final SignupRequest signupRequest) {
        String sql = "select count(email) from member where email = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, signupRequest.email()) != 0;
    }
}
