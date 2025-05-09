package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.domain.MemberRole;
import roomescape.exception.NotFoundMemberException;
import roomescape.persistence.query.CreateMemberQuery;
import roomescape.service.param.LoginMemberParam;
import roomescape.service.param.RegisterMemberParam;
import roomescape.service.result.MemberResult;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResult login(final LoginMemberParam loginMemberParam) {
        Member member = memberRepository.findByEmailAndPassword(loginMemberParam.email(), loginMemberParam.password())
                .orElseThrow(() -> new NotFoundMemberException(loginMemberParam.email() + " " + loginMemberParam.password() + "에 해당하는 유저가 없습니다."));
        return MemberResult.from(member);
    }

    public MemberResult create(final RegisterMemberParam registerMemberParam) {
        Long id = memberRepository.create(new CreateMemberQuery(
                registerMemberParam.name(),
                MemberRole.USER,
                registerMemberParam.email(),
                registerMemberParam.password()
        ));

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundMemberException(id + "에 해당하는 유저가 없습니다."));
        return MemberResult.from(member);
    }

    public MemberResult findById(final Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundMemberException(id + "에 해당하는 유저가 없습니다."));
        return MemberResult.from(member);
    }

    public List<MemberResult> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberResult::from)
                .toList();
    }
}
