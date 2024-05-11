package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.service.auth.exception.MemberNotFoundException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class H2MemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public H2MemberRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("MEMBER")
                .usingGeneratedKeyColumns("ID");
    }

    private Member mapRowMember(ResultSet rs, int rowNum) throws SQLException {
        return new Member(
                rs.getLong("ID"),
                rs.getString("NAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"),
                Role.valueOf(rs.getString("ROLE"))
        );
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM MEMBER";

        return jdbcTemplate.query(sql, this::mapRowMember);
    }

    @Override
    public Optional<Member> findById(long id) {
        String sql = "SELECT * FROM MEMBER WHERE ID = ?";

        return jdbcTemplate.query(sql, this::mapRowMember, id)
                .stream()
                .findAny();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM MEMBER WHERE EMAIL = ?";

        return jdbcTemplate.query(sql, this::mapRowMember, email)
                .stream()
                .findAny();
    }

    @Override
    public void fetchById(long id) {
        findById(id).orElseThrow(() -> new MemberNotFoundException("존재 하지 않는 멤버 입니다."));
    }

    @Override
    public Member save(Member member) {
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(member);
        long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return member.assignId(id);
    }
}
