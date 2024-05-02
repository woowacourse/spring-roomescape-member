package roomescape.member.dao;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.repository.MemberRepository;

@Repository
public class MemberDao implements MemberRepository {
    private final SimpleJdbcInsert simpleJdbcInsert;
    public MemberDao(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(final Member member) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", member.getName());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Member(id, member.getName());
    }
}
