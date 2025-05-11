package roomescape.domain.member.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.model.Member;
import roomescape.domain.member.model.Role;

@Repository
public class JdbcMemberDaoImpl implements MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcMemberDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
            .withTableName("member")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public long save(Member member) {
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("role", member.getRole().toString());
        parameters.put("name", member.getName());
        parameters.put("email", member.getEmail());
        parameters.put("password", member.getPassword());
        Number newId = insertActor.executeAndReturnKey(parameters);
        return newId.longValue();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String query = """
            select *
             from member
             where email = ?;
            """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query,
                (resultSet, rowNum) -> {
                    return Member.createMember(
                        resultSet.getLong("id"),
                        Role.convertFrom(resultSet.getString("role")),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                    );
                }, email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String query = """
            select *
             from member
             where email = ? AND password = ?;
            """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query,
                (resultSet, rowNum) -> {
                    return Member.createMember(
                        resultSet.getLong("id"),
                        Role.convertFrom(resultSet.getString("role")),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                    );
                }, email, password));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        String query = """
            select *
             from member
             where id = ?;
            """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query,
                (resultSet, rowNum) -> {
                    return Member.createMember(
                        resultSet.getLong("id"),
                        Role.convertFrom(resultSet.getString("role")),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                    );
                }, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        String query = "select * from member";
        return jdbcTemplate.query(query,
            (resultSet, RowNum) -> {
                return Member.createMember(
                    resultSet.getLong("id"),
                    Role.convertFrom(resultSet.getString("role")),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"));
            });
    }
}
