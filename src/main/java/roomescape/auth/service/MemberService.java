package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.entity.Member;
import roomescape.auth.repository.MemberRepository;
import roomescape.auth.service.dto.LoginMember;
import roomescape.auth.service.dto.request.LoginRequest;
import roomescape.auth.service.dto.request.UserSignupRequest;
import roomescape.auth.service.dto.response.LoginResponse;
import roomescape.auth.service.dto.response.MemberBasicInfoResponse;
import roomescape.global.exception.conflict.MemberEmailConflictException;
import roomescape.global.exception.notFound.MemberNotFoundException;
import roomescape.global.exception.notFound.NotFoundException;
import roomescape.global.infrastructure.JwtTokenProvider;

import java.util.List;

// TODO: findByXXX - DataAccessException 핸들링
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new NotFoundException("입력한 정보와 일치하는 회원 정보가 존재하지 않습니다."));
        String token = jwtTokenProvider.createToken(member);
        return new LoginResponse(token);
    }

    public void signup(UserSignupRequest request) {
        memberRepository.findByEmail(request.email())
                .ifPresentOrElse(user -> {
                    throw new MemberEmailConflictException();
                }, () -> {
                    Member member = request.toEntity();
                    memberRepository.save(member);
                });
    }

    public LoginMember getLoginMemberById(final Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberNotFoundException(userId));
        return LoginMember.of(member);
    }

    public List<MemberBasicInfoResponse> getAllMemberNames() {
        return memberRepository.findAll()
                .stream()
                .map(member -> new MemberBasicInfoResponse(member.getId(), member.getName()))
                .toList();
    }
}
