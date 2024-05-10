package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.dto.member.MemberResponse;
import roomescape.exception.ClientErrorExceptionWithLog;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberResponse::from)
                .toList();
    }

    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ClientErrorExceptionWithLog(
                        "[ERROR] 존재하지 않는 사용자 입니다.",
                        "member_id : " + id
                ));

        return MemberResponse.from(member);
    }

    public MemberResponse getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ClientErrorExceptionWithLog(
                        "[ERROR] 존재하지 않는 아이디(이메일) 입니다.",
                        "email : " + email
                ));

        return MemberResponse.from(member);
    }
}
