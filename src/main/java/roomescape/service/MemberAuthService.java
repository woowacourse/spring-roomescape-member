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
import roomescape.service.request.MemberSignUpAppRequest;
import roomescape.service.response.MemberAppResponse;

@Service
public class MemberAuthService {

    public static final MemberRole USER_ROLE = new MemberRole("USER");

    private final MemberRepository memberRepository;

    public MemberAuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberAppResponse signUp(MemberSignUpAppRequest request) {
        if (memberRepository.isExistsByEmail(request.email())) {
            throw new AuthorizationException("해당 이메일의 회원이 이미 존재합니다.");
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

    public MemberAppResponse findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .map(member -> new MemberAppResponse(member.getId(), member.getName().getValue(),
                member.getRole().getValue()))
            .orElseThrow(() -> new NoSuchElementException("회원 정보를 찾지 못했습니다. 다시 로그인 해주세요."));
    }

    public List<MemberAppResponse> findAll() {
        return memberRepository.findAll().stream()
            .map(member -> new MemberAppResponse(member.getId(), member.getName().getValue(),
                member.getRole().getValue()))
            .toList();
    }
}
