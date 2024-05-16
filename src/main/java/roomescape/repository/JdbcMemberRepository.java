package roomescape.repository;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Member> rowMapper;

    public JdbcMemberRepository(DataSource dataSource, RowMapper<Member> rowMapper) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Member> findAllMembers() {
        String sql = "SELECT id, name, email, password, role FROM member";

        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Member> findMemberByEmailAndPassword(String email, String password) {
        String sql = """
                SELECT
                id, name, email, password, role
                FROM member
                WHERE email = :email
                AND password = :password
                """;
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("password", password);
        List<Member> members = jdbcTemplate.query(sql, paramMap, rowMapper);

        return members.stream().findFirst();
    }

    @Override
    public Optional<Member> findMemberById(Long memberId) {
        String sql = """
                SELECT
                id, name, email, password, role
                FROM member
                WHERE id = :id
                """;
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("id", memberId);
        Member member = jdbcTemplate.queryForObject(sql, paramMap, rowMapper);

        return Optional.ofNullable(member);
    }

    @Override
    public void insertMember(Member member) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", member.getName().getValue())
                .addValue("role", member.getRole())
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword());

        jdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }
}
