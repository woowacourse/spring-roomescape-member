package roomescape.unit.service.reservation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import roomescape.auth.JwtTokenProvider;
import roomescape.dto.member.SignupRequestDto;
import roomescape.dto.reservation.AddReservationDto;
import roomescape.dto.reservation.AddReservationTimeDto;
import roomescape.dto.reservation.AddThemeDto;
import roomescape.service.ReservationMemberService;
import roomescape.service.member.MemberService;
import roomescape.service.reservation.ReservationService;
import roomescape.service.reservation.ReservationTimeService;
import roomescape.service.reservation.ThemeService;
import roomescape.unit.config.ServiceFixture;
import roomescape.unit.repository.member.FakeMemberRepository;
import roomescape.unit.repository.reservation.FakeReservationRepository;
import roomescape.unit.repository.reservation.FakeReservationTimeRepository;
import roomescape.unit.repository.reservation.FakeThemeRepository;

class ReservationMemberServiceTest {

    private static ReservationMemberService reservationMemberService;
    private static ReservationTimeService reservationTimeService;
    private static ThemeService themeService;
    private static MemberService memberService;

    @BeforeAll
    static void setup() {
        FakeReservationRepository fakeReservationRepository = ServiceFixture.fakeReservationRepository();
        FakeReservationTimeRepository fakeReservationTimeRepository = ServiceFixture.fakeReservationTimeRepository();
        FakeThemeRepository fakeThemeRepository = ServiceFixture.fakeThemeRepository();
        ReservationService reservationService = new ReservationService(fakeReservationRepository,
                fakeReservationTimeRepository, fakeThemeRepository);

        reservationTimeService = new ReservationTimeService(fakeReservationRepository, fakeReservationTimeRepository);
        themeService = new ThemeService(fakeThemeRepository, fakeReservationRepository);

        JwtTokenProvider jwtTokenProvider = ServiceFixture.jwtTokenProvider();
        FakeMemberRepository fakeMemberRepository = ServiceFixture.fakeMemberRepository();
        BCryptPasswordEncoder bCryptPasswordEncoder = ServiceFixture.passwordEncoder();
        memberService = new MemberService(bCryptPasswordEncoder, fakeMemberRepository, jwtTokenProvider);

        reservationMemberService = new ReservationMemberService(memberService, reservationService);
    }

    @Test
    void 유저정보를_추가하여_예약을_진행한다() {
        long timeId = reservationTimeService.addReservationTime(
                new AddReservationTimeDto(LocalTime.now().plusHours(1L)));
        long themeId = themeService.addTheme(new AddThemeDto("tuda", "asdf", "asdf"));
        AddReservationDto addReservationDto = new AddReservationDto("asdf", LocalDate.now(), Long.valueOf(timeId),
                Long.valueOf(themeId));

        long memberId = memberService.signup(new SignupRequestDto("test@naver.com", "testtest", "test"));

        reservationMemberService.addReservation(addReservationDto, memberId);
    }

    @Test
    void 존재하지않는_유저로_예약을_진행하면_예외가_발생한다() {
        long timeId = reservationTimeService.addReservationTime(
                new AddReservationTimeDto(LocalTime.now().plusHours(1L)));
        long themeId = themeService.addTheme(new AddThemeDto("tuda", "asdf", "asdf"));
        AddReservationDto addReservationDto = new AddReservationDto("asdf", LocalDate.now(), Long.valueOf(timeId),
                Long.valueOf(themeId));

        long memberId = -1;

        assertThatThrownBy(() -> reservationMemberService.addReservation(addReservationDto, memberId)).isInstanceOf(
                IllegalArgumentException.class);
    }
}
