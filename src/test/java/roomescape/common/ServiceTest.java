package roomescape.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;
import roomescape.reservation.domain.*;

@Sql("/test-schema.sql")
@ActiveProfiles(value = "test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public abstract class ServiceTest {
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    protected Member createTestMember(Member member) {
        return memberRepository.save(member);
    }

    protected Theme createTestTheme(Theme theme) {
        return themeRepository.save(theme);
    }

    protected ReservationTime createTestReservationTime(ReservationTime reservationTime) {
        return reservationTimeRepository.save(reservationTime);
    }

    protected Reservation createTestReservation(Reservation reservation) {
        Member member = createTestMember(reservation.getMember());
        Theme theme = createTestTheme(reservation.getTheme());
        ReservationTime reservationTime = createTestReservationTime(reservation.getTime());
        return reservationRepository.save(new Reservation(member, reservation.getDate(), reservationTime, theme));
    }
}
