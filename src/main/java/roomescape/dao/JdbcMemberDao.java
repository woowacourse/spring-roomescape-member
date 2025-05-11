package roomescape.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import roomescape.entity.Member;
import roomescape.exception.MemberNotFoundException;
import roomescape.mapper.MemberMapper;

@Component
public class JdbcMemberDao implements MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member";
        return jdbcTemplate.query(
                sql,
                new MemberMapper()
        );
    }

    @Override
    public Member findById(long memberId) {
        try {
            String sql = "select * from member where id = ?";
            return jdbcTemplate.queryForObject(
                    sql,
                    new MemberMapper(),
                    memberId
            );
        } catch (EmptyResultDataAccessException e) {
            throw new MemberNotFoundException();
        }
    }

    @Override
    public Member create(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into member (name, email, password, role) values (?, ?, ?, ?)";
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            sql,
                            new String[]{"id"}
                    );
                    ps.setString(1, member.getName());
                    ps.setString(2, member.getEmail());
                    ps.setString(3, member.getPassword());
                    ps.setString(4, member.getRole().name());
                    return ps;
                },
                keyHolder
        );
        long memberId = keyHolder.getKey().longValue();
        return member.copyWithId(memberId);
    }

    @Override
    public void deleteById(long id) {
        String sql = "delete from member where id = ?";
        int deletedCount = jdbcTemplate.update(
                sql,
                id
        );
        if (deletedCount == 0) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
    }

    @Override
    public boolean existsByEmailAndPassword(String email, String password) {
        String sql = "select exists (select 1 from member where email = ? and password = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                email,
                password
        ));
    }

    @Override
    public boolean existsByEmail(Member member) {
        String sql = "select exists (select 1 from member where email = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                member.getEmail()
        ));
    }

    @Override
    public Member findByEmailAndPassword(String email, String password) {
        String sql = "select * from member where email = ? and password = ?";
        return jdbcTemplate.queryForObject(
                sql,
                new MemberMapper(),
                email,
                password
        );
    }
}
