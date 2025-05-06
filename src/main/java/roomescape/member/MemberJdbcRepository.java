package roomescape.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MemberJdbcRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MemberJdbcRepository(
            final JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveMember(final Member member) {
        final String sql = "INSERT INTO member(email, password, name) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getName());
    }

    @Override
    public Member findByEmail(final String email) {
        final String sql = "SELECT * FROM member WHERE email=?";
        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) ->
                new Member(
                        resultSet.getLong("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("name")
                ), email);
    }

    @Override
    public Boolean existsById(final Long id) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM member WHERE id=?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public Boolean existsByEmail(final String email) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM member WHERE email=?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, email);
    }
}
