package roomescape.member.infrastructure;

import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final static RowMapper<Member> ROW_MAPPER = (resultSet, rowNum) -> Member.createWithId(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcMemberRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Member findById(Long id) {
        String sql = "SELECT * FROM member WHERE id = :id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);

        return namedParameterJdbcTemplate.queryForObject(sql, param, ROW_MAPPER);
    }

    @Override
    public Member findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = :email";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("email", email);
        
        return namedParameterJdbcTemplate.queryForObject(sql, param, ROW_MAPPER);
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM member";

        return namedParameterJdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public boolean isExistsByEmail(String email) {
        String sql = """
                SELECT EXISTS(
                    SELECT 1 FROM member
                    WHERE email = :email
                )
                """;

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("email", email);

        return namedParameterJdbcTemplate.queryForObject(sql, param, Boolean.class).booleanValue();
    }
}
