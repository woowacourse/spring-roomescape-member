package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.member.Member;
import roomescape.dto.member.MemberCreateRequest;
import roomescape.dto.member.MemberResponse;
import roomescape.service.exception.InvalidRequestException;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<MemberResponse> findAll() {
        return memberDao.readAll().stream()
                .map(MemberResponse::from)
                .toList();
    }

    public MemberResponse add(MemberCreateRequest request) {
        validateUniqueEmail(request.email());
        Member member = memberDao.create(request.toDomain());
        return MemberResponse.from(member);
    }

    private void validateUniqueEmail(String email) {
        boolean isUnique = memberDao.readByEmail(email).isPresent();
        if (isUnique) {
            throw new InvalidRequestException("이미 가입되어 있는 이메일입니다.");
        }
    }
}
