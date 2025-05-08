package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.exception.ReservationException;
import roomescape.persistence.query.CreateMemberQuery;
import roomescape.service.param.LoginMemberParam;
import roomescape.service.param.RegisterMemberParam;
import roomescape.service.result.CheckLoginUserResult;
import roomescape.service.result.LoginMemberResult;
import roomescape.service.result.RegisterMemberResult;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginMemberResult login(final LoginMemberParam loginMemberParam) {
        Member member = memberRepository.findByEmailAndPassword(loginMemberParam.email(), loginMemberParam.password())
                .orElseThrow(() -> new ReservationException(loginMemberParam.email() + " " + loginMemberParam.password() + "에 해당하는 유저가 없습니다."));
        return LoginMemberResult.from(member);
    }

    public RegisterMemberResult create(final RegisterMemberParam registerMemberParam) {
        Long id = memberRepository.create(new CreateMemberQuery(
                registerMemberParam.name(),
                registerMemberParam.email(),
                registerMemberParam.password()
        ));

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ReservationException(id + "에 해당하는 유저가 없습니다."));
        return RegisterMemberResult.from(member);
    }

    public CheckLoginUserResult findById(final Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ReservationException(id + "에 해당하는 유저가 없습니다."));
        return CheckLoginUserResult.from(member);
    }
}
