package roomescape.repository.member.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;


public class MemberRowMapper implements RowMapper<Member> {

    public static final MemberRowMapper INSTANCE = new MemberRowMapper();

    private MemberRowMapper() {
    }

    @Override
    public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Member(
                rs.getLong("id"),
                rs.getString("name"),
                Role.valueOf(rs.getString("role")),
                rs.getString("email"),
                rs.getString("password")
        );
    }
}
