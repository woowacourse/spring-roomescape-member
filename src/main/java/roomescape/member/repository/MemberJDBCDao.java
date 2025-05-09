package roomescape.member.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.member.entity.Member;
import roomescape.member.entity.Role;

import java.util.Objects;

@Repository
public class MemberJDBCDao implements MemberRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public MemberJDBCDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public Member save(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into users (email, password, name, role) values (:email, :password, :name, :role)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword())
                .addValue("name", member.getName())
                .addValue("role", member.getRole().toString());

        namedJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Member(id, member.getEmail(), member.getPassword(), member.getName(), member.getRole());
    }

    @Override
    public Member findByEmail(String email) {
        String sql = "select * from users where email = :email";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", email);

        return namedJdbcTemplate.queryForObject(sql, params, getUserRowMapper());
    }

    private RowMapper<Member> getUserRowMapper() {
        return (resultSet, rowNum) -> new Member(
                resultSet.getLong("id"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                Role.valueOf(resultSet.getString("role"))
        );
    }
}
