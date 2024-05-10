package roomescape.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;

@Component
public class MemberRowMapper implements RowMapper<Member> {

    @Override
    public Member mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return Member.of(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("role")
        );
    }
}
