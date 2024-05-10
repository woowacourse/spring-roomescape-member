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
import java.util.Optional;

@Repository
public class H2MemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public H2MemberRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("MEMBER");
    }

    private Member mapRowMember(final ResultSet rs, final int rowNum) throws SQLException {
        return new Member(
                rs.getLong("ID"),
                rs.getString("NAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"),
                Role.valueOf(rs.getString("ROLE"))
        );
    }

    @Override
    public Optional<Member> findById(final long id) {
        final String sql = "SELECT * FROM MEMBER WHERE ID = ?";

        return jdbcTemplate.query(sql, this::mapRowMember, id)
                .stream()
                .findAny();
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        final String sql = "SELECT * FROM MEMBER WHERE EMAIL = ?";

        return jdbcTemplate.query(sql, this::mapRowMember, email)
                .stream()
                .findAny();
    }

    @Override
    public void fetchById(final long id) {
        findById(id).orElseThrow(() -> new MemberNotFoundException("존재 하지 않는 멤버 입니다."));
    }

    @Override
    public Member save(final Member member) {
        final BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(member);
        simpleJdbcInsert.execute(params);

        return member;
    }
}
