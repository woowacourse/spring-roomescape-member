package roomescape.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.common.exception.AlreadyExistMemberException;
import roomescape.common.exception.NotFoundMemberException;
import roomescape.domain.Member;
import roomescape.dto.request.MemberSignUpRequest;
import roomescape.dto.response.MemberNameSelectResponse;
import roomescape.dto.response.MemberSignUpResponse;
import roomescape.repository.MemberRepository;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberSignUpResponse signup(MemberSignUpRequest request) {
        String email = request.email();
        boolean alreadyExistMember = memberRepository.findByEmail(email).isPresent();
        if (alreadyExistMember) {
            throw new AlreadyExistMemberException("회원 정보가 이미 존재합니다.");
        }
        Member member = memberRepository.save(request.toMember());
        return MemberSignUpResponse.of(member, true);
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id).
                orElseThrow(() -> new NotFoundMemberException("회원 정보가 존재하지 않습니다."));
    }

    public boolean isExistMemberById(Long id) {
        return memberRepository.findById(id).isPresent();
    }

    public List<MemberNameSelectResponse> findMemberNames() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberNameSelectResponse::from)
                .toList();
    }
}
