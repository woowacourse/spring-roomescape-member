package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Password;
import roomescape.domain.dto.*;
import roomescape.exception.AccessNotAllowException;
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
        Password password = new Password(signupRequest.password());
        Long id = memberDao.create(signupRequest, password);
        return new SignupResponse(id);
    }

    private void validateExist(final SignupRequest signupRequest) {
        if (memberDao.isExist(signupRequest)) {
            throw new SignupFailException("회원 정보가 이미 존재합니다.");
        }
    }

    public void login(final LoginRequest loginRequest) {
        //hash 값 반환
        Password password = new Password(loginRequest.password());
        if (memberDao.isLoginFail(loginRequest, password)) {
            throw new AccessNotAllowException("회원 정보가 일치하지 않습니다.");
        }
        // TODO 토큰 반환하기
    }
}
