package roomescape.member.repository;

import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;

@Repository
public class JdbcMemberDao implements MemberDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Member> memberMapper = (resulSet, rowNum) ->
        new Member(
                resulSet.getLong("id"),
                resulSet.getString("name"),
                resulSet.getString("email"),
                resulSet.getString("password")
        );

    public JdbcMemberDao(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        final String sql = "SELECT * FROM member WHERE email = :email AND password = :password";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("password", password);
        Member member = jdbcTemplate.queryForObject(sql, parameters, memberMapper);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findById(long id) {
        final String sql = "SELECT * FROM member WHERE id = :id";
        final SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        final Member member = jdbcTemplate.queryForObject(sql, parameters, memberMapper);
        return Optional.ofNullable(member);
    }
}
