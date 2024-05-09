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
import roomescape.member.domain.Role;
import roomescape.member.domain.repository.MemberRepository;

@Repository
public class MemberDao implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Member> rowMapper() {
        return (resultSet, rowNum) -> {
            final Long memberId = resultSet.getLong("id");
            final String name = resultSet.getString("name");
            final String email = resultSet.getString("email");
            final String password = resultSet.getString("password");
            final Role role = Role.valueOf(resultSet.getString("role"));

            return new Member(memberId, name, email, password, role);
        };
    }

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
                .addValue("password", member.getPassword())
                .addValue("role", member.getRole());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Member(id, member.getName(), member.getEmail(), member.getPassword(), member.getRole());
    }

    @Override
    public boolean existBy(String email, String password) {
        String sql = "SELECT COUNT(*) FROM member WHERE email = ? AND password = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email, password);
        return count != null && count > 0;
    }

    @Override
    public Member findByEmail(String email) {
        String sql = "SELECT id, name, email, password, role FROM member WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper(), email);
    }

    @Override
    public Member findById(long memberId) {
        String sql = "SELECT id, name, email, password, role FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper(), memberId);
    }
}
