package roomescape.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.fixture.LocalDateFixture.AFTER_ONE_DAYS_DATE;
import static roomescape.fixture.LocalDateFixture.AFTER_TWO_DAYS_DATE;
import static roomescape.fixture.LocalTimeFixture.TEN_HOUR;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.login.service.FakeMemberRepository;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.dto.ReservationAddRequest;
import roomescape.domain.reservationTime.domain.ReservationTime;
import roomescape.domain.reservationTime.service.FakeReservationTimeRepository;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.theme.service.FakeThemeRepository;
import roomescape.global.exception.ClientIllegalArgumentException;

class AdminReservationServiceTest {

    private static final Member ADMIN_MEMBER = new Member(1L, "어드민", "admin@gmail.com", "123456");
    private static final ReservationTime TEN_RESERVATION_TIME = new ReservationTime(1L, TEN_HOUR);
    private static final Theme DUMMY_THEME = new Theme(1L, "dummy", "dummy", "dummy");

    AdminReservationService adminReservationService;
    FakeReservationRepository fakeReservationRepository;
    FakeReservationTimeRepository fakeReservationTimeRepository;
    FakeThemeRepository fakeThemeRepository;
    FakeMemberRepository fakeMemberRepository;

    @BeforeEach
    void setUp() {
        fakeReservationRepository = new FakeReservationRepository();
        fakeReservationTimeRepository = new FakeReservationTimeRepository();
        fakeThemeRepository = new FakeThemeRepository();
        fakeMemberRepository = new FakeMemberRepository();
        adminReservationService = new AdminReservationService(fakeReservationRepository, fakeReservationTimeRepository,
                fakeThemeRepository, fakeMemberRepository);
    }

    @DisplayName("없는 id의 예약을 삭제하면 예외를 발생합니다.")
    @Test
    void should_throw_ClientIllegalArgumentException_when_remove_reservation_with_non_exist_id() {
        assertThatThrownBy(() -> adminReservationService.removeReservation(1L))
                .isInstanceOf(ClientIllegalArgumentException.class)
                .hasMessage("해당 id를 가진 예약이 존재하지 않습니다.");
    }

    @DisplayName("존재하지 않는 예약시각으로 예약 시 예외가 발생합니다.")
    @Test
    void should_throw_ClientIllegalArgumentException_when_reserve_non_exist_time() {
        fakeThemeRepository.insert(DUMMY_THEME);
        fakeMemberRepository.insert(ADMIN_MEMBER);
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(AFTER_TWO_DAYS_DATE, 1L, 1L, 1L);

        assertThatThrownBy(() -> adminReservationService.addReservation(reservationAddRequest))
                .isInstanceOf(ClientIllegalArgumentException.class)
                .hasMessage("존재 하지 않는 예약시각으로 예약할 수 없습니다.");
    }

    @DisplayName("존재 하지 않는 멤버로 예약 시 예외를 발생합니다.")
    @Test
    void should_throw_exception_when_reserve_with_non_exist_member() {
        fakeReservationTimeRepository.insert(TEN_RESERVATION_TIME);
        fakeThemeRepository.insert(DUMMY_THEME);
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(AFTER_ONE_DAYS_DATE, 1L, 1L, 1L);//

        assertThatThrownBy(() -> adminReservationService.addReservation(reservationAddRequest))
                .isInstanceOf(ClientIllegalArgumentException.class)
                .hasMessage("존재 하지 않는 멤버로 예약할 수 없습니다.");
    }

    @DisplayName("존재 하지 않는 테마로 예약 시 예외를 발생합니다.")
    @Test
    void should_throw_exception_when_reserve_with_non_exist_theme() {
        fakeReservationTimeRepository.insert(TEN_RESERVATION_TIME);
        fakeMemberRepository.insert(ADMIN_MEMBER);
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(AFTER_ONE_DAYS_DATE, 1L, 1L, 1L);
        assertThatThrownBy(() -> adminReservationService.addReservation(reservationAddRequest))
                .isInstanceOf(ClientIllegalArgumentException.class)
                .hasMessage("존재 하지 않는 테마로 예약할 수 없습니다");
    }

    @DisplayName("예약 날짜와 예약시각 그리고 테마 아이디가 같은 경우 예외를 발생합니다.")
    @Test
    void should_throw_ClientIllegalArgumentException_when_reserve_date_and_time_duplicated() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "dummy", "description", "url");
        Member member = new Member(1L, "dummy", "dummy", "dummy");
        Reservation reservation = new Reservation(null, AFTER_ONE_DAYS_DATE, reservationTime, theme, member);
        fakeReservationRepository.insert(reservation);

        ReservationAddRequest conflictRequest = new ReservationAddRequest(AFTER_ONE_DAYS_DATE, 1L, 1L, 1L);

        assertThatThrownBy(() -> adminReservationService.addReservation(conflictRequest))
                .isInstanceOf(ClientIllegalArgumentException.class)
                .hasMessage("예약 날짜와 예약시간 그리고 테마가 겹치는 예약은 할 수 없습니다.");
    }
}
