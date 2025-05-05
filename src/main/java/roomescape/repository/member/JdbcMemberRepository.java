package roomescape.repository.member;

import java.sql.PreparedStatement;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> memberRowMapper = (resultSet, rowNumber) -> {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("username");
        String password = resultSet.getString("password");
        Role role = Role.valueOf(resultSet.getString("role"));

        return new Member(id, name, password, role);
    };

    @Override
    public long add(Member member) {
        String sql = "insert into member (username,password,role) values(?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, member.getEmail());
            ps.setString(2, member.getPassword());
            ps.setString(3, member.getRole().name());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public boolean existsByUsernameAndPassword(String usename, String password) {
        String sql = "select exists (select 1 from member where username = ? and password = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, usename, password);
    }

    @Override
    public Optional<Member> findById(long id) {
        try {
            String sql = "select id,username,password,role from member where id=?";
            return Optional.of(jdbcTemplate.queryForObject(sql, memberRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String username, String password) {
        try {
            String sql = "select id,username,password,role from member where username=? and password=?";
            return Optional.of(jdbcTemplate.queryForObject(sql, memberRowMapper, username, password));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
