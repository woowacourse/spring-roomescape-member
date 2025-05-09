package roomescape.member.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberRepository;

@Repository
public class MemberJdbcRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberJdbcRepository(JdbcTemplate jdbcTemplate,
                                @Qualifier("userJdbcInsert") SimpleJdbcInsert simpleJdbcInsert) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = simpleJdbcInsert;
    }

    @Override
    public Member save(String name, String email, String password) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("email", email)
                .addValue("password", password);

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        return new Member(id, name, email, password);
    }

    @Override
    public boolean isExistUser(String email, String password) {
        String sql = "SELECT COUNT(*) FROM users where email = ? AND password = ?";
        int count  = jdbcTemplate.queryForObject(sql, Integer.class, email, password);
        return count > 0;
    }

    @Override
    public Member findUserByEmail(String payload) {
        String sql = "SELECT * FROM users WHERE email = ?";
        Member member;

        try {
            member = jdbcTemplate.queryForObject(sql, (resultSet, rowNum) ->
                    new Member(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    ), payload);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 회원입니다.");
        }
        return member;
    }
}
