package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.dto.MemberResponse;
import roomescape.domain.dto.MemberResponses;
import roomescape.domain.dto.SignupRequest;
import roomescape.domain.dto.SignupResponse;
import roomescape.exception.SignupFailException;
import roomescape.repository.MemberDao;

import java.util.List;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponses findEntireMembers() {
        final List<MemberResponse> memberResponses = memberDao.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
        return new MemberResponses(memberResponses);
    }

    public SignupResponse createUser(final SignupRequest signupRequest) {
        validateExist(signupRequest);
        Long id = memberDao.create(signupRequest);
        return new SignupResponse(id);
    }

    private void validateExist(final SignupRequest signupRequest) {
        if (memberDao.isExist(signupRequest)) {
            throw new SignupFailException("회원 정보가 이미 존재합니다.");
        }
    }
}
