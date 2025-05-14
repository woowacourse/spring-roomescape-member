package roomescape.service;

import java.util.Date;
import java.util.List;
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
import roomescape.service.dto.response.MemberResult;
import roomescape.service.dto.response.MemberSignUpResult;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(final MemberRepository memberRepository, final JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public MemberSignUpResult register(final MemberSignUpCreation creation) {
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

    public String publishAccessToken(final MemberLoginCreation creation) {
        Member member = memberRepository.findByEmailAndPassword(creation.email(), creation.password())
                .orElseThrow(() -> new NotFoundValueException("존재하지 않는 유저입니다"));
        return publishToken(member);
    }

    private String publishToken(final Member member) {
        JwtRequest jwtRequest = new JwtRequest(member.getId(), member.getName(), member.getRole(), new Date());
        return jwtProvider.generateToken(jwtRequest);
    }

    public List<MemberResult> getAllMemberByRole(MemberRoleType role) {
        return memberRepository.findAllByRole(role)
                .stream()
                .map(MemberResult::from)
                .toList();
    }
}
