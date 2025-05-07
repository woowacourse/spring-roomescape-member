package roomescape.service;

import java.util.Date;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.MemberRoleType;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotFoundValueException;
import roomescape.jwt.JwtProvider;
import roomescape.jwt.JwtRequest;
import roomescape.repository.MemberRepository;
import roomescape.service.dto.request.MemberLoginCreation;
import roomescape.service.dto.request.MemberSignUpCreation;
import roomescape.service.dto.response.MemberLoginCheckResult;
import roomescape.service.dto.response.MemberSignUpResult;

@Service
public class MemberLoginService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberLoginService(final MemberRepository memberRepository, final JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public MemberSignUpResult signup(final MemberSignUpCreation creation) {
        boolean exists = memberRepository.existsByEmail(creation.email());
        if (exists) {
            throw new ExistedDuplicateValueException("이미 사용 중인 이메일입니다");
        }
        Member member = new Member(creation.name(), creation.email(), creation.password(), MemberRoleType.MEMBER);
        long id = memberRepository.insert(member);
        Member savedMember = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundValueException("존재하지 않는 멤버입니다"));

        return MemberSignUpResult.from(savedMember);
    }

    public String login(final MemberLoginCreation creation) {
        Member member = memberRepository.findByEmailAndPassword(creation.email(), creation.password())
                .orElseThrow(() -> new NotFoundValueException("존재하지 않는 유저입니다"));
        return publishToken(member);
    }

    public String publishToken(final Member member) {
        JwtRequest jwtRequest = new JwtRequest(member.getName(), member.getEmail(), member.getRole(), new Date());
        return jwtProvider.generateToken(jwtRequest);
    }

    public MemberLoginCheckResult varifyToken(final String token) {
        JwtRequest jwtRequest = jwtProvider.verifyToken(token);
        return new MemberLoginCheckResult(jwtRequest.name());
    }
}
