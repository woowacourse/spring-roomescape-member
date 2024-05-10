package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Password;
import roomescape.domain.PasswordEncoder;
import roomescape.domain.dto.*;
import roomescape.exception.AccessNotAllowException;
import roomescape.exception.SignupFailException;
import roomescape.repository.MemberDao;

import java.util.List;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final PasswordEncoder passwordEncoder;

    public MemberService(final MemberDao memberDao, final PasswordEncoder passwordEncoder) {
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
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
        Password password = passwordEncoder.encode(signupRequest.password());
        Long id = memberDao.create(signupRequest, password);
        return new SignupResponse(id);
    }

    private void validateExist(final SignupRequest signupRequest) {
        if (memberDao.isExist(signupRequest)) {
            throw new SignupFailException("회원 정보가 이미 존재합니다.");
        }
    }

    public void login(final LoginRequest loginRequest) {
        Password password = memberDao.findPasswordByEmail(loginRequest.email())
                .orElseThrow(() -> new AccessNotAllowException("회원 정보가 일치하지 않습니다."));
        Password requestPassword = passwordEncoder.encode(loginRequest.password(), password.getSalt());
        if (!password.check(requestPassword)) {
            throw new AccessNotAllowException("회원 정보가 일치하지 않습니다.");
        }
        // TODO 토큰 반환하기
    }
}
