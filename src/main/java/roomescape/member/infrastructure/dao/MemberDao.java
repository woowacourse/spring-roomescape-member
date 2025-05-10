package roomescape.member.infrastructure.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.application.dto.CreateMemberRequest;
import roomescape.member.application.dto.GetMemberResponse;
import roomescape.member.application.repository.MemberRepository;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

@Repository
public class MemberDao implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("member")
                .usingColumns("name", "email", "password", "role")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = """
                SELECT id, name, email, password, role FROM `member`
                WHERE email = ?
                """;
        try {
            Member member = jdbcTemplate.queryForObject(sql,
                    (resultSet, rowNum) -> {
                        Role role = Role.valueOf(resultSet.getString("role"));
                        return new Member(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                role
                        );
                    }, email);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Member insert(CreateMemberRequest request) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", request.name())
                .addValue("email", request.email())
                .addValue("password", request.password())
                .addValue("role", request.role());

        long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new Member(id, request.name(), request.email(), request.password(), request.role());
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = """
                SELECT id, name, email, password, role FROM `member`
                WHERE id = ?
                """;
        try {
            Member member = jdbcTemplate.queryForObject(sql,
                    (resultSet, rowNum) -> {
                        Role role = Role.valueOf(resultSet.getString("role"));
                        return new Member(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                role
                        );
                    }, id);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<GetMemberResponse> findAll() {
        String sql = """
                SELECT id, name
                FROM member
                """;
        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> new GetMemberResponse(
                        resultSet.getLong("id"),
                        resultSet.getString("name")
                ));
    }
}
