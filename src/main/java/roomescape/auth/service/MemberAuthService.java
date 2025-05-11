package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.service.dto.response.LoginMemberResponse;
import roomescape.auth.entity.Member;
import roomescape.auth.repository.MemberRepository;
import roomescape.auth.service.dto.request.LoginRequest;
import roomescape.auth.service.dto.request.UserSignupRequest;
import roomescape.auth.service.dto.response.LoginResponse;
import roomescape.auth.service.dto.response.MemberIdAndNameResponse;
import roomescape.global.exception.badRequest.BadRequestException;
import roomescape.global.exception.conflict.MemberEmailConflictException;
import roomescape.global.exception.notFound.MemberNotFoundException;
import roomescape.global.exception.unauthorized.MemberUnauthorizedException;
import roomescape.global.infrastructure.JwtTokenProvider;

import java.util.List;

// TODO: findByXXX - DataAccessException 핸들링
@Service
public class MemberAuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberAuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(MemberUnauthorizedException::new);
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

    // TODO: 파라미터 토큰 ? user id?
    public LoginMemberResponse getLoginMemberByToken(String token) {
        String subject = jwtTokenProvider.resolve(token);
        try {
            final long userId = Long.parseLong(subject);
            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new MemberNotFoundException(userId));
            return new LoginMemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
        } catch (NumberFormatException e) {
            throw new BadRequestException("잘못된 형식의 토큰입니다.");
        }
    }

    public List<MemberIdAndNameResponse> getAllMemberNames() {
        return memberRepository.findAll()
                .stream()
                .map(member -> new MemberIdAndNameResponse(member.getId(), member.getName()))
                .toList();
    }
}
