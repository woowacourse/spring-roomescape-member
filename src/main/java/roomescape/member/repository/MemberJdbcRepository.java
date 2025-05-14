package roomescape.member.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

@Repository
public class MemberJdbcRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member add(Member member) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", member.getName());
        params.put("email", member.getEmail());
        params.put("password", member.getPassword());
        params.put("role", Role.USER.getName());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return Member.createWithId(id, member.getName(), member.getEmail(), member.getPassword(), member.getRole());
    }

    @Override
    public Optional<Member> findIdByEmailAndPassword(String email, String password) {
        String sql = "select * from member where email = ? and password = ?";
        List<Member> members = jdbcTemplate.query(
                sql,
                (resultSet, rowNum) ->
                        Member.createWithId(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                Role.findBy(resultSet.getString("role"))),
                email, password
        );
        return members.stream().findFirst();
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = ?";
        List<Member> members = jdbcTemplate.query(
                sql,
                (resultSet, rowNum) ->
                        Member.createWithId(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                Role.findBy(resultSet.getString("role"))),

                id
        );
        return members.stream().findFirst();
    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member";
        List<Member> members = jdbcTemplate.query(
                sql,
                (resultSet, rowNum) ->
                        Member.createWithId(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                Role.findBy(resultSet.getString("role"))
                        )
        );
        return members;
    }

    @Override
    public Optional<Role> findRoleById(Long id) {
        String sql = "select role from member where id = ?";
        List<Role> role = jdbcTemplate.query(
                sql,
                (resultSet, rowNum) ->
                        Role.findBy(resultSet.getString("role")),
                id
        );

        return role.stream().findFirst();
    }
}
