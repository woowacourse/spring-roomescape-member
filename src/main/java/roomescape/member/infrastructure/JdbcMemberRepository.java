package roomescape.member.infrastructure;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;
import roomescape.member.domain.Role;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final static RowMapper<Member> ROW_MAPPER = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            Role.valueOf(resultSet.getString("role"))
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
    public Member findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM member WHERE email = :email AND password = :password";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("password", password);

        return namedParameterJdbcTemplate.queryForObject(sql, param, ROW_MAPPER);
    }
}
