package roomescape.service.member;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;
import roomescape.exception.DuplicatedException;
import roomescape.exception.LoginException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.member.RoleRepository;
import roomescape.service.member.dto.MemberLoginRequest;
import roomescape.service.member.dto.MemberResponse;
import roomescape.service.member.dto.MemberSignUpRequest;
import roomescape.service.member.dto.MemberTokenResponse;
import roomescape.auth.MemberTokenConverter;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberTokenConverter memberTokenConverter;
    private final RoleRepository roleRepository;

    public MemberService(MemberRepository memberRepository, MemberTokenConverter memberTokenConverter, RoleRepository roleRepository) {
        this.memberRepository = memberRepository;
        this.memberTokenConverter = memberTokenConverter;
        this.roleRepository = roleRepository;
    }

    public MemberResponse signUp(MemberSignUpRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new DuplicatedException("이미 가입되어 있는 이메일 주소입니다.");
        }
        Member member = memberRepository.save(request.toMember());
        MemberRole role = new MemberRole(member.getId());
        roleRepository.save(role);
        return MemberResponse.from(member);
    }

    public MemberTokenResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new LoginException("이메일 혹은 비밀번호가 잘못되었습니다."));
        checkPasswordMatches(member, request.password());
        return memberTokenConverter.generateToken(member);
    }

    private void checkPasswordMatches(Member member, String password) {
        if (!member.isPasswordMatches(password)) {
            throw new LoginException("이메일 혹은 비밀번호가 잘못되었습니다.");
        }
    }

    public Member get(long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당하는 사용자가 없습니다."));
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }

    public boolean checkAdmin(long memberId) {
        return roleRepository.findByMemberId(memberId)
                .map(MemberRole::isAdmin)
                .orElse(Boolean.FALSE);
    }
}
