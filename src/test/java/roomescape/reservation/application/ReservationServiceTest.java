package roomescape.reservation.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.common.ServiceTest;
import roomescape.global.exception.ViolationException;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.dto.response.ReservationTimeResponse;
import roomescape.reservation.dto.response.ReservedThemeResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static roomescape.TestFixture.*;

class ReservationServiceTest extends ServiceTest {
    @Autowired
    private ReservationService reservationService;

    @Test
    @DisplayName("예약을 생성한다.")
    void create() {
        // given
        ReservationTime reservationTime = createTestReservationTime(new ReservationTime(MIA_RESERVATION_TIME));
        Theme theme = createTestTheme(WOOTECO_THEME());
        Member member = createTestMember(USER_MIA());

        Reservation reservation = MIA_RESERVATION(reservationTime, theme, member);

        // when
        ReservationResponse response = reservationService.create(reservation);

        // then
        assertThat(response).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("invalidReservationDate")
    @DisplayName("예약 날짜는 현재 날짜 이후이다.")
    void validateDate(LocalDate invalidDate) {
        // given
        Reservation reservation = new Reservation(
                USER_MIA(), invalidDate, new ReservationTime(MIA_RESERVATION_TIME), WOOTECO_THEME());

        // when & then
        assertThatThrownBy(() -> reservationService.create(reservation))
                .isInstanceOf(ViolationException.class);
    }

    private static Stream<LocalDate> invalidReservationDate() {
        return Stream.of(
                LocalDate.now(),
                LocalDate.now().minusDays(1L)
        );
    }

    @Test
    @DisplayName("동일한 테마, 날짜, 시간에 한 팀만 예약할 수 있다.")
    void createDuplicatedReservation() {
        // given
        Reservation reservation = createTestReservation(MIA_RESERVATION());

        Member tommy = createTestMember(USER_TOMMY());
        Reservation duplicatedReservation = new Reservation(
                tommy, MIA_RESERVATION_DATE, reservation.getTime(), reservation.getTheme());

        // when & then
        assertThatThrownBy(() -> reservationService.create(duplicatedReservation))
                .isInstanceOf(ViolationException.class);
    }

    @Test
    @DisplayName("모든 예약 목록을 조회한다.")
    void findAll() {
        // given
        createTestReservation(MIA_RESERVATION());
        createTestReservation(TOMMY_RESERVATION());

        // when
        List<ReservationResponse> reservations = reservationService.findAll();

        // then
        assertSoftly(softly -> {
            softly.assertThat(reservations).hasSize(2)
                    .extracting(ReservationResponse::memberName)
                    .containsExactly(MIA_NAME, TOMMY_NAME);
            softly.assertThat(reservations).extracting(ReservationResponse::time)
                    .extracting(ReservationTimeResponse::startAt)
                    .containsExactly(MIA_RESERVATION_TIME, TOMMY_RESERVATION_TIME);
            softly.assertThat(reservations).extracting(ReservationResponse::theme)
                    .extracting(ReservedThemeResponse::name)
                    .containsExactly(WOOTECO_THEME_NAME, WOOTECO_THEME_NAME);
        });
    }

    @Test
    @DisplayName("예약자, 테마, 날짜로 예약 목록을 조회한다.")
    void findAllByMemberIdAndThemeIdAndDateBetween() {
        // given
        Reservation miaReservation = createTestReservation(MIA_RESERVATION());
        Reservation tommyReservation = createTestReservation(TOMMY_RESERVATION());

        // when
        List<ReservationResponse> reservations = reservationService.findAllByMemberIdAndThemeIdAndDateBetween(
                miaReservation.getMemberId(), miaReservation.getThemeId(), miaReservation.getDate(), tommyReservation.getDate());

        // then
        assertThat(reservations).hasSize(1)
                .extracting(ReservationResponse::memberName)
                .containsExactly(MIA_NAME);
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void delete() {
        // given
        Reservation reservation = createTestReservation(MIA_RESERVATION());

        // when & then
        assertThatCode(() -> reservationService.delete(reservation.getId()))
                .doesNotThrowAnyException();
    }
}
