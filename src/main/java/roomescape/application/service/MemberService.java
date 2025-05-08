package roomescape.application.service;

import org.springframework.stereotype.Service;
import roomescape.domain.repository.MemberRepository;
import roomescape.presentation.dto.response.MemberResponse;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> getAll() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }
}
