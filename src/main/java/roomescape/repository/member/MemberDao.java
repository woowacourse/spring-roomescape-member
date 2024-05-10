package roomescape.repository.member;

import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class MemberDao implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private static final RowMapper<Member> rowMapper = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );

    public MemberDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "email", "password");
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT EXISTS(SELECT id FROM MEMBER WHERE EMAIL = ?)";
        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, email);
        return Boolean.TRUE.equals(result);
    }

    @Override
    public Member save(Member member) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", member.getName())
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword());

        long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Member(id, member);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT ID, NAME, EMAIL, PASSWORD FROM MEMBER WHERE EMAIL = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, rowMapper, email);
            return Optional.ofNullable(member);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
