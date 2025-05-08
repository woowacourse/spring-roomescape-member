package roomescape.login.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.login.domain.Member;

@Repository
public class MemberDao {
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MemberDao(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Optional<Member> findByEmail(final String email) {
        String sql = "SELECT id, name, email, password from member where email = :email";
        Map<String, Object> parameter = Map.of("email", email);

        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(sql, parameter,
                    (resultSet, rowNum) -> createMember(resultSet)));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

//    public Member add(final Member member) {
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("name", member.getName());
//        parameters.put("email", member.getEmail());
//        parameters.put("password", member.getPassword());
//        long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
//        return new Member(id, member.getName(), member.getEmail(), member.getPassword());
//    }

    private Member createMember(final ResultSet resultSet) throws SQLException {
        return new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password")
        );
    }
}
