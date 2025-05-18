package roomescape.infra;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.entity.Member;
import roomescape.domain.entity.Role;
import roomescape.domain.repository.MemberRepository;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private static final RowMapper<Member> memeberRowMapper = (resultSet, rowNum) ->
            new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    Role.valueOf(resultSet.getString("role"))
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcMemberRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("member")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "email", "password", "role");
    }

    @Override
    public Member save(final Member member) {
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", member.getName())
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword())
                .addValue("role", member.getRole().name());

        final Long newId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Member(newId, member.getName(), member.getEmail(), member.getPassword(), member.getRole());
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

    @Override
    public Optional<Member> findById(final Long id) {
        final String findByIdSql = """
                    select *
                    from member as m
                    where m.id = ?
                """;
        return jdbcTemplate.query(findByIdSql, memeberRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Member> findAll() {
        final String findAllSql = """
                    select *
                    from member as m
                """;
        return jdbcTemplate.query(findAllSql, memeberRowMapper);
    }
}
