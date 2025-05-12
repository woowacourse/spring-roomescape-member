package roomescape.service.memeber;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.member.Member;
import roomescape.dto.MemberRequest;
import roomescape.dto.member.MemberResponse;
import roomescape.exceptions.member.MemberDuplicateException;
import roomescape.repository.member.MemberRepository;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<MemberResponse> readAll() {
        List<Member> members = memberRepository.findAll();

        return members.stream()
                .map(MemberResponse::from)
                .toList();
    }

    @Transactional
    public MemberResponse addMember(MemberRequest memberRequest) {
        Member member = memberRequest.toEntity();
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new MemberDuplicateException("중복된 사용자입니다.", memberRequest.email());
        }
        String encoded = passwordEncoder.encode(member.getPassword());
        Member newMember = memberRepository.save(member, encoded);
        return MemberResponse.from(newMember);
    }
}
