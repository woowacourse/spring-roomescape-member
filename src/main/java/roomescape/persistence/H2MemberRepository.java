package roomescape.persistence;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Email;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.member.Password;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class H2MemberRepository implements MemberRepository {
    private final MemberRowMapper rowMapper;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public H2MemberRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.rowMapper = new MemberRowMapper();
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
        ;
    }

    public Member save(Member member) {
        String sql = "insert into member(name, email, password) values(?, ?, ?)";
        Long memberId = jdbcInsert.executeAndReturnKey(
                Map.of(
                        "name", member.getName(),
                        "email", member.getEmail(),
                        "password", member.getPassword()
                )
        ).longValue();

        return new Member(
                memberId,
                member.getName(),
                member.getEmail(),
                member.getPassword()
        );
    }

    public List<Member> findAll() {
        return jdbcTemplate.query(getBasicSelectQuery(), rowMapper);
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "select * from member where email = ?";
        try {
            Member savedMember = jdbcTemplate.queryForObject(sql, rowMapper, email);
            return Optional.ofNullable(savedMember);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private String getBasicSelectQuery() {
        return "select * from member";
    }

    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(
                    rs.getLong("id"),
                    new MemberName(rs.getString("name")),
                    new Email(rs.getString("email")),
                    new Password(rs.getString("password"))
            );
        }
    }
}
