package roomescape.repository;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.dto.MemberRequest;
import roomescape.model.user.Email;
import roomescape.model.user.Member;
import roomescape.model.user.Name;
import roomescape.model.user.Password;
import roomescape.model.user.Role;

@Repository
public class JdbcMemberRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> memberRowMapper = (rs, rowNum) ->
            new Member(
                    rs.getLong("id"),
                    new Name(rs.getString("name")),
                    new Email(rs.getString("email")),
                    new Password(rs.getString("password")),
                    Role.of(rs.getString("role")
                    )
            );

    @Override
    public Member login(String email, String password) {
        String sql = "select * from member where email = ? and password = ?";
        try {
            return jdbcTemplate.queryForObject(sql, memberRowMapper, email, password);
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: " + email, ex);
        }
    }

    @Override
    public Name findNameById(Long id) {
        String sql = "select name from member where id = ?";
        try {
            return new Name(jdbcTemplate.queryForObject(sql, String.class, id));
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: userId :" + id, ex);
        }
    }

    @Override
    public Role findRoleByEmail(String email) {
        String sql = "select role from member where email = ?";
        return Role.of(jdbcTemplate.queryForObject(sql, String.class, email));
    }

    @Override
    public Member findById(Long id) {
        String sql = "select id, name, email, password, role from member where id = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper);
    }

    @Override
    public Member addMember(MemberRequest memberRequest) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into member(name, email, password, role) values(?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, memberRequest.name());
            ps.setString(2, memberRequest.email());
            ps.setString(3, memberRequest.password());
            ps.setString(4, Role.USER.getValue());
            return ps;
        }, keyHolder);
        return new Member(keyHolder.getKey().longValue(), new Name(memberRequest.name()),
                new Email(memberRequest.email()), new Password(memberRequest.password()), Role.USER);
    }

    @Override
    public Member findByEmail(String userEmail) {
        String sql = "select id, name, email, password, role from member where email = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper, userEmail);
    }

    @Override
    public Name findNameByEmail(String userEmail) {
        String sql = "select name from member where email = ?";
        try {
            return new Name(jdbcTemplate.queryForObject(sql, String.class, userEmail));
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: userEmail :" + userEmail, ex);
        }
    }

    @Override
    public List<Member> findAllUsers() {
        String sql = "select id, name, email, password, role from member";
        return jdbcTemplate.query(sql, memberRowMapper);
    }
}
