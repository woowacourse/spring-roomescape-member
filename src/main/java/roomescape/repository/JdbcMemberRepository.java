package roomescape.repository;

import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;

@Service
public class JdbcMemberRepository implements MemberRepository {

    private static final RowMapper<Member> memeberRowMapper = (resultSet, rowNum) ->
            new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcMemberRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("member")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "email", "password");
    }

    @Override
    public Member save(final Member member) {
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", member.getName())
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword());

        final Long newId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Member(newId, member.getName(), member.getEmail(), member.getPassword());
    }

    @Override
    public boolean existsByEmail(final String email) {
        final String existsSql = """
                    select exists(
                        select 1
                        from member as m
                        where m.email = ?
                    )
                """;
        final Boolean exists = jdbcTemplate.queryForObject(existsSql, Boolean.class, email);
        return Boolean.TRUE.equals(exists);
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        final String findByEmailSql = """
                    select *
                    from member as m
                    where m.email = ?
                """;
        return jdbcTemplate.query(findByEmailSql, memeberRowMapper, email)
                .stream()
                .findFirst();
    }
}
