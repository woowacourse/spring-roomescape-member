package roomescape.member.infrastructure.db;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

@Component
@RequiredArgsConstructor
public class MemberH2Dao implements MemberDao {

    private static final RowMapper<Member> DEFAULT_ROW_MAPPER = (resultSet, rowNum) ->
            Member.builder()
                    .id(resultSet.getLong("id"))
                    .name(resultSet.getString("name"))
                    .email(resultSet.getString("email"))
                    .password(resultSet.getString("password"))
                    .role(Role.valueOf(resultSet.getString("role")))
                    .build();

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<Member> selectByEmailAndPassword(String email, String password) {
        String selectQuery = """
                SELECT id, name, email, password, role
                FROM member
                WHERE member.email = :email AND member.password = :password
                """;

        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(selectQuery,
                    Map.of("email", email, "password", password),
                    DEFAULT_ROW_MAPPER
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
