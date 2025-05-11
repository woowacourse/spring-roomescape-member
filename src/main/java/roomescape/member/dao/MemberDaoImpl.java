package roomescape.member.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;

@Repository
public class MemberDaoImpl implements MemberDao {
    private static final String ADMIN_NAME = "admin";

    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MemberDaoImpl(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT id, name, email, password FROM member";
        return namedParameterJdbcTemplate.query(sql, (resultSet, rowNum) -> createMember(resultSet));
    }

    @Override
    public Optional<Member> findById(final Long id) {
        String sql = "SELECT id, name, email, password from member where id = :id";
        Map<String, Object> parameter = Map.of("id", id);

        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(sql, parameter,
                    (resultSet, rowNum) -> createMember(resultSet)));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
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

    @Override
    public Boolean isPasswordMatch(final String email, final String password) {
        String sql = "SELECT password FROM member WHERE email = :email";
        Map<String, Object> parameter = Map.of("email", email);

        try {
            String memberPassword = namedParameterJdbcTemplate.queryForObject(sql, parameter, String.class);
            return password.equals(memberPassword);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public Boolean isAdmin(final String email, final String password) {
        String sql = "SELECT name FROM member WHERE email = :email AND password = :password";
        Map<String, Object> parameter = Map.of("email", email, "password", password);

        try {
            String memberName = namedParameterJdbcTemplate.queryForObject(sql, parameter, String.class);
            return memberName.equals(ADMIN_NAME);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public Boolean existsByEmail(final String email) {
        String sql = "SELECT COUNT(*) FROM member WHERE email = :email";
        Map<String, Object> parameter = Map.of("email", email);

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, parameter, Integer.class);
        return count != 0;
    }

    @Override
    public Member add(final Member member) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("name", member.getName());
        parameters.put("email", member.getEmail());
        parameters.put("password", member.getPassword());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        return new Member(
                id,
                member.getName(),
                member.getEmail(),
                member.getPassword()
        );
    }

    @Override
    public void deleteById(final Long id) {
        String sql = "DELETE FROM member WHERE id = :id";
        Map<String, Object> parameter = Map.of("id", id);

        namedParameterJdbcTemplate.update(sql, parameter);
    }

    private Member createMember(final ResultSet resultSet) throws SQLException {
        return new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password")
        );
    }
}
