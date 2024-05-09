package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.auth.dto.MemberResponse;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.exception.ReservationBusinessException;
import roomescape.service.dto.MemberSaveRequest;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse save(MemberSaveRequest memberRequest) {
        Member member = memberRequest.toMember();
        Member savedMember = memberRepository.save(member);

        return MemberResponse.from(savedMember);
    }

    public List<MemberResponse> findAll() {
         return memberRepository.findAll().stream()
                 .map(MemberResponse::from)
                 .toList();
    }

    public void delete(Long id) {
        if (memberRepository.findById(id).isEmpty()) {
            throw new ReservationBusinessException("회원이 존재하지 않습니다.");
        }

        memberRepository.deleteById(id);
    }

    public MemberResponse findByEmailAndPassword(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new ReservationBusinessException("회원이 존재하지 않습니다."));

        return MemberResponse.from(member);
    }
}
