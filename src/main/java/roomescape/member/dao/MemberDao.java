package roomescape.member.dao;

import java.sql.ResultSet;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.reservation.domain.Theme;

@Repository
public class MemberDao implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Member> rowMapper = (ResultSet resultSet, int rowNum) -> {
        return new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password")
        );
    };

    public MemberDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(Member member) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", member.getName())
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Member(id, member.getName(), member.getEmail(), member.getPassword());
    }

    @Override
    public boolean existBy(String email, String password) {
        String sql = "SELECT COUNT(*) FROM member WHERE email = ? AND password = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email, password);
        return count != null && count > 0;
    }

    @Override
    public Member findByEmail(String email) {
        String sql = "SELECT id, name, email, password FROM member WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, email);
    }
}
