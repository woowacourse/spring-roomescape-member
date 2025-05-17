package roomescape.repository;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.Member;
import roomescape.repository.support.DomainMapper;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcMemberRepository(final DataSource dateSource) {
        this.jdbcTemplate = new JdbcTemplate(dateSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dateSource)
                .withTableName("MEMBER")
                .usingGeneratedKeyColumns("ID");
    }

    @Override
    public List<Member> findAll() {
        final String sql = "SELECT * FROM MEMBER";

        return jdbcTemplate.query(sql, DomainMapper.MEMBER);
    }

    @Override
    public Long save(final Member member) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("ID", member.id())
                .addValue("NAME", member.name())
                .addValue("EMAIL", member.email())
                .addValue("PASSWORD", member.password())
                .addValue("ROLE", member.role());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    @Override
    public Optional<Member> findById(final Long id) {
        final String sql = "SELECT * FROM MEMBER WHERE ID = ?";
        final List<Member> members = jdbcTemplate.query(sql, DomainMapper.MEMBER, id);

        return members.stream().findAny();
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        final String sql = "SELECT * FROM MEMBER WHERE EMAIL = ?";
        final List<Member> members = jdbcTemplate.query(sql, DomainMapper.MEMBER, email);

        return members.stream().findAny();
    }

    @Override
    public Boolean removeById(final Long id) {
        final String sql = "DELETE FROM MEMBER WHERE ID = ?";
        final int removedRowsCount = jdbcTemplate.update(sql, id);

        return removedRowsCount > 0;
    }
}
