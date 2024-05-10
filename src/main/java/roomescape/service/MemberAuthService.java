package roomescape.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.domain.vo.MemberEmail;
import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.MemberPassword;
import roomescape.domain.vo.MemberRole;
import roomescape.exception.AuthorizationException;
import roomescape.exception.EmailAlreadyExistsException;
import roomescape.service.request.MemberAppRequest;
import roomescape.service.request.TokenAppRequest;
import roomescape.service.response.MemberAppResponse;

@Service
public class MemberAuthService {

    public static final MemberRole USER_ROLE = new MemberRole("USER");

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    public MemberAuthService(JwtProvider jwtProvider, MemberRepository memberRepository) {
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }

    public MemberAppResponse signUp(MemberAppRequest request) {
        if (isExistsMember(request.email(), request.password())) {
            throw new EmailAlreadyExistsException();
        }

        Member newMember = new Member(
            new MemberName(request.name()),
            new MemberEmail(request.email()),
            new MemberPassword(request.password()),
            USER_ROLE);

        Member savedMember = memberRepository.save(newMember);
        return new MemberAppResponse(savedMember.getId(), savedMember.getName().getValue(),
            savedMember.getRole().getValue());
    }

    public String createExpireToken(String token) {
        return jwtProvider.getExpiredToken(token);
    }

    public String createToken(TokenAppRequest request) {
        if (isExistsMember(request.email(), request.password())) {
            return jwtProvider.createToken(request.email());
        }

        throw new AuthorizationException();
    }

    public MemberAppResponse findMemberByToken(String token) {
        String payload = jwtProvider.getPayload(token);

        return memberRepository.findByEmail(payload)
            .map(member -> new MemberAppResponse(member.getId(), member.getName().getValue(),
                member.getRole().getValue()))
            .orElseThrow(() -> new NoSuchElementException("회원 정보를 찾지 못했습니다. 다시 로그인 해주세요."));
    }

    public MemberAppResponse findMemberById(Long id) {
        return memberRepository.findById(id)
            .map(member -> new MemberAppResponse(member.getId(), member.getName().getValue(),
                member.getRole().getValue()))
            .orElseThrow(() -> new NoSuchElementException("회원 정보를 찾지 못했습니다."));
    }

    public List<MemberAppResponse> findAll() {
        return memberRepository.findAll().stream()
            .map(member -> new MemberAppResponse(member.getId(), member.getName().getValue(),
                member.getRole().getValue()))
            .toList();
    }

    private boolean isExistsMember(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password).isPresent();
    }
}
