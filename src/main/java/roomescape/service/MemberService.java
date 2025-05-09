package roomescape.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.common.exception.AlreadyExistMemberException;
import roomescape.common.exception.AuthorizationException;
import roomescape.domain.Member;
import roomescape.dto.request.MemberSignUpRequest;
import roomescape.dto.response.MemberSignUpResponse;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberSignUpResponse signup(MemberSignUpRequest request) {
        // todo: 이미 존재하는 회원 또는 회원가입 불가능한 상태인 경우 isSuccess false로 보내는 로직 구현
        String email = request.email();
        boolean alreadyExistMember = memberRepository.findByEmail(email).isPresent();
        if (alreadyExistMember) {
            throw new AlreadyExistMemberException("회원 정보가 이미 존재합니다.");
        }
        Member member = memberRepository.save(request.toMember());
        return MemberSignUpResponse.of(member, true);
    }
}
