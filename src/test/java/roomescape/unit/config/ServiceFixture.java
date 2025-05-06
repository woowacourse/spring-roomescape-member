package roomescape.unit.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import roomescape.auth.JwtTokenProvider;
import roomescape.service.ReservationMemberService;
import roomescape.service.member.MemberService;
import roomescape.service.reservation.ReservationService;
import roomescape.service.reservation.ReservationTimeService;
import roomescape.service.reservation.ThemeService;
import roomescape.unit.repository.member.FakeMemberRepository;
import roomescape.unit.repository.reservation.FakeReservationRepository;
import roomescape.unit.repository.reservation.FakeReservationTimeRepository;
import roomescape.unit.repository.reservation.FakeThemeRepository;

public class ServiceFixture {

    public final static String TEST_SECRET_KEY = "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest";

    public static JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(TEST_SECRET_KEY);
    }

    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static FakeMemberRepository fakeMemberRepository() {
        return new FakeMemberRepository();
    }

    public static FakeReservationRepository fakeReservationRepository() {
        return new FakeReservationRepository();
    }

    public static FakeReservationTimeRepository fakeReservationTimeRepository() {
        return new FakeReservationTimeRepository();
    }

    public static FakeThemeRepository fakeThemeRepository() {
        return new FakeThemeRepository();
    }

    public static MemberService createMemberService() {
        return new MemberService(passwordEncoder(), fakeMemberRepository(), jwtTokenProvider());
    }

    public static ReservationService createReservationService() {
        return new ReservationService(
                fakeReservationRepository(),
                fakeReservationTimeRepository(),
                fakeThemeRepository()
        );
    }

    public static ReservationTimeService createReservationTimeService() {
        return new ReservationTimeService(fakeReservationRepository(), fakeReservationTimeRepository());
    }

    public static ThemeService createThemeService() {
        return new ThemeService(fakeThemeRepository(), fakeReservationRepository());
    }

    public static ReservationMemberService createReservationMemberService() {
        return new ReservationMemberService(createMemberService(), createReservationService());
    }
}
