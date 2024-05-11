package roomescape.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.JwtTokenProvider;
import roomescape.domain.Member;
import roomescape.dto.LoginRequest;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberResponse;
import roomescape.repository.JdbcMemberRepository;
import roomescape.repository.JdbcReservationRepository;

@Service
public class MemberService {

    private final JdbcMemberRepository memberRepository;
    private final JdbcReservationRepository reservationRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(JdbcMemberRepository memberRepository, JdbcReservationRepository reservationRepository,
                         JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<MemberResponse> getAllMembers() {
        List<Member> members = memberRepository.findAll();

        return members.stream()
                .map(MemberResponse::from)
                .toList();
    }

    // TODO: 예외처리 통일하기
    public Member getMemberByLogin(LoginRequest request) {
        Optional<Member> member = memberRepository.findByEmailAndPassword(request.email(), request.password());
        if (member.isEmpty()) {
            throw new AuthorizationException("등록되지 않은 사용자입니다.");
        }

        return member.get();
    }

    public Member getMemberByToken(String token) {
        long id = jwtTokenProvider.getIdFromToken(token);
        Member member = memberRepository.findById(id);

        return member;
    }

    public MemberResponse addMember(MemberRequest request) {
        Member member = request.toMember();
        Member savedMember = memberRepository.save(member);

        return MemberResponse.from(savedMember);
    }

    public void deleteMemberById(Long id) {
        boolean exist = reservationRepository.existByMemberId(id);
        if (exist) {
            throw new IllegalArgumentException("해당 시간에 예약이 존재합니다.");
        }

        memberRepository.deleteById(id);
    }
}
