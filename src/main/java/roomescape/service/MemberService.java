package roomescape.service;


import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.dto.MemberResponse;
import roomescape.dto.SignUpRequest;
import roomescape.repository.JdbcMemberRepository;

@Service
public class MemberService {
    private final JdbcMemberRepository jdbcMemberRepository;

    public MemberService(JdbcMemberRepository jdbcMemberRepository) {
        this.jdbcMemberRepository = jdbcMemberRepository;
    }

    public Long addMember(SignUpRequest signUpRequest) {
        Member member = new Member(signUpRequest.name(), signUpRequest.email(), signUpRequest.password());
        if (jdbcMemberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new IllegalArgumentException(String.format("%s는 중복되는 이메일입니다. ", signUpRequest.email()));
        }
        return jdbcMemberRepository.save(member);
    }

    public Member findById(Long id) {
        return jdbcMemberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("id가 %d인 유저가 존재하지 않습니다.", id)));
    }

    public List<MemberResponse> findAll() {
        return jdbcMemberRepository.findAll().stream().map(MemberResponse::from).toList();
    }
}
