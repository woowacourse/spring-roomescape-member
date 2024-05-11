package roomescape.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.dao.rowmapper.MemberRowMapper;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;

@Repository
public class MemberDao implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final MemberRowMapper memberRowMapper;

    public MemberDao(JdbcTemplate jdbcTemplate, MemberRowMapper memberRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.memberRowMapper = memberRowMapper;
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM member";
        return jdbcTemplate.query(sql, memberRowMapper);
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        List<Member> member = jdbcTemplate.query(sql, memberRowMapper, id);
        return DataAccessUtils.optionalResult(member);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = """
                SELECT *
                FROM member
                WHERE email = ?
                """;
        List<Member> member = jdbcTemplate.query(sql, memberRowMapper, email);
        return DataAccessUtils.optionalResult(member);
    }

    @Override
    public String findNameById(Long id) {
        String sql = """
                SELECT member.name
                FROM member
                WHERE id = ?
                """;
        List<String> name = jdbcTemplate.queryForList(sql, String.class, id);
        return DataAccessUtils.singleResult(name);
    }
}
