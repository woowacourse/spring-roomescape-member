package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.entity.Member;
import roomescape.auth.repository.MemberRepository;
import roomescape.auth.service.dto.response.CheckResponse;
import roomescape.auth.service.dto.request.LoginRequest;
import roomescape.auth.service.dto.response.LoginResponse;
import roomescape.auth.service.dto.request.SignupRequest;
import roomescape.exception.badRequest.BadRequestException;
import roomescape.exception.conflict.MemberEmailConflictException;
import roomescape.exception.notFound.MemberNotFoundException;
import roomescape.exception.unauthorized.MemberUnauthorizedException;

@Service
public class MemberAuthService {
    private final MemberRepository memberRepository;
    // TODO: TokenProvider 인터페이스 삭제 - 테스트 Stub으로 변경
    private final TokenProvider jwtTokenProvider;

    public MemberAuthService(MemberRepository memberRepository, TokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(MemberUnauthorizedException::new);
        String token = jwtTokenProvider.createToken(member);
        return new LoginResponse(token);
    }

    public void signup(SignupRequest request) {
        memberRepository.findByEmail(request.email())
                .ifPresentOrElse(user -> {
                    throw new MemberEmailConflictException();
                }, () -> {
                    Member member = request.toEntity();
                    memberRepository.save(member);
                });
    }

    public CheckResponse checkLogin(String token) {
        String subject = jwtTokenProvider.resolve(token);
        try {
            final long userId = Long.parseLong(subject);
            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new MemberNotFoundException(userId));
            return new CheckResponse(member.getName());
        } catch (NumberFormatException e) {
            throw new BadRequestException("잘못된 형식의 토큰입니다.");
        }
    }
}
