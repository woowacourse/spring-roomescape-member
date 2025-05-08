package roomescape.application;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import roomescape.application.dto.MemberDto;
import roomescape.domain.Member;
import roomescape.domain.repository.MemberRepository;
import roomescape.exception.NotFoundException;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberDto getMemberById(@NotNull Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 사용자가 없습니다."));
        return MemberDto.from(member);
    }

    public MemberDto getMemberBy(@NotNull String email, @NotNull String password) {
        Member member = memberRepository.findBy(email, password)
                .orElseThrow(() -> new NotFoundException("이메일과 비밀번호가 일치하는 사용자가 없습니다."));
        return MemberDto.from(member);
    }
}
