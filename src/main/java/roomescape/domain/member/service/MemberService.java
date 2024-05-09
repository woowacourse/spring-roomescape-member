package roomescape.domain.member.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import roomescape.domain.member.dao.MemberDao;
import roomescape.domain.member.domain.ReservationMember;
import roomescape.domain.member.dto.MemberResponse;
import roomescape.domain.member.mapper.MemberMapper;

import java.util.List;

@Service
public class MemberService {

    private final MemberMapper memberMapper = new MemberMapper();

    private final JdbcTemplate jdbcTemplate;
    private final MemberDao memberDao;

    private final RowMapper<ReservationMember> memberRowMapper = (resultSet, rowNum) ->
            new ReservationMember(
                    resultSet.getLong("id"),
                    resultSet.getString("name")
            );

    public MemberService(JdbcTemplate jdbcTemplate, MemberDao memberDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.memberDao = memberDao;
    }

    public List<MemberResponse> findAll() {
        List<ReservationMember> members = memberDao.findAll();

        return members.stream()
                .map(memberMapper::mapToResponse)
                .toList();
    }

    public ReservationMember findById(Long id) {
        String sql = "SELECT id, name FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper, id);
    }
}
