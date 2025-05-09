package roomescape.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import roomescape.entity.Member;
import roomescape.entity.MemberRole;

public class MemberMapper implements RowMapper<Member> {

    @Override
    public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Member(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                MemberRole.from(rs.getString("role"))
        );
    }
}
