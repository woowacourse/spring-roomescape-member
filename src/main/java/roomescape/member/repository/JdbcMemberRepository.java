package roomescape.member.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.member.entity.Member;
import roomescape.member.entity.RoleType;

@Repository
@RequiredArgsConstructor
public class JdbcMemberRepository implements MemberRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Member save(Member member) {
        String query = "INSERT INTO member(name, email, password) VALUES (:name, :email, :password)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", member.getName())
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(query, params, keyHolder);

        return new Member(
                keyHolder.getKey().longValue(),
                member.getName(),
                member.getEmail(),
                member.getPassword(),
                member.getRole()
        );
    }

    @Override
    public List<Member> findAll() {
        String query = """
                SELECT
                    m.id,
                    m.name,
                    m.email,
                    m.password,
                    m.role
                FROM member as m
                """;

        return jdbcTemplate.query(query, (resultSet, rowNum) -> {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            RoleType role = RoleType.valueOf(resultSet.getString("role"));

            return new Member(
                    id,
                    name,
                    email,
                    password,
                    role
            );
        });
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String query = """
                SELECT
                    m.id,
                    m.name,
                    m.email,
                    m.password,
                    m.role
                FROM member as m
                WHERE m.email = :email
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", email);

        try {
            Member member = jdbcTemplate.queryForObject(query, params, (resultSet, rowNum) -> {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                RoleType role = RoleType.valueOf(resultSet.getString("role"));

                return new Member(
                        id,
                        name,
                        email,
                        password,
                        role
                );
            });
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String query = "DELETE FROM member WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        final int updated = jdbcTemplate.update(query, params);

        return updated > 0;
    }
}
