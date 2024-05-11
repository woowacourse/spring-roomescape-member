package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static roomescape.TestFixture.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dao.ReservationDao;
import roomescape.domain.member.Member;
import roomescape.domain.member.Name;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.member.Role;
import roomescape.domain.theme.Theme;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.reservation.ReservationTimeResponse;
import roomescape.dto.theme.ReservedThemeResponse;

import java.time.LocalDate;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationDao reservationDao;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("예약을 생성한다.")
    void create() {
        // given
        final Reservation reservation = MIA_RESERVATION();

        given(reservationDao.save(reservation))
                .willReturn(new Reservation(1L, reservation.getMember(), reservation.getDate(),
                        reservation.getTime(), reservation.getTheme()));

        // when
        final ReservationResponse response = reservationService.create(reservation);

        // then
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("동일한 테마, 날짜, 시간에 한 팀 이상 예약하려는 경우 예외가 발생한다.")
    void throwExceptionWhenCreateDuplicatedReservation() {
        // given
        final ReservationTime miaReservationTime = new ReservationTime(1L, MIA_RESERVATION_TIME);
        final Reservation miaReservation = MIA_RESERVATION(miaReservationTime, WOOTECO_THEME(1L));

        given(reservationDao.findAllByDateAndTimeAndThemeId(any(), any(), anyLong()))
                .willReturn(List.of(miaReservation));

        // when & then
        assertThatThrownBy(() -> reservationService.create(miaReservation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("모든 예약 목록을 조회한다.")
    void findAllReservations() {
        // given
        final Member member1 = new Member(new Name("냥인"), "nyangin@email.com", "1234", Role.USER);
        final Member member2 = new Member(new Name("미아"), "mia@email.com", "1234", Role.USER);
        final String startAt1 = "18:00";
        final String startAt2 = "19:00";
        final ReservationTime reservationTime1 = new ReservationTime(startAt1);
        final ReservationTime reservationTime2 = new ReservationTime(startAt2);
        final String themeName1 = "호러";
        final String themeName2 = "추리";
        final Theme theme1 = new Theme(themeName1, "매우 무섭습니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        final Theme theme2 = new Theme(themeName2, "매우 어렵습니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        final Reservation reservation1 = new Reservation(member1, "2034-05-08", reservationTime1, theme1);
        final Reservation reservation2 = new Reservation(member2, "2034-05-08", reservationTime2, theme2);

        given(reservationDao.findAll())
                .willReturn(List.of(reservation1, reservation2));

        // when
        final List<ReservationResponse> reservations = reservationService.findAll();

        // then
        assertAll(() -> {
            assertThat(reservations).hasSize(2)
                    .extracting(ReservationResponse::name)
                    .containsExactly("냥인", "미아");
            assertThat(reservations).extracting(ReservationResponse::time)
                    .extracting(ReservationTimeResponse::startAt)
                    .containsExactly(startAt1, startAt2);
            assertThat(reservations).extracting(ReservationResponse::theme)
                    .extracting(ReservedThemeResponse::name)
                    .containsExactly(themeName1, themeName2);
        });
    }

    @Test
    @DisplayName("테마, 사용자, 예약 날짜에 따른 예약 목록을 조회한다.")
    void findAllByThemeAndMemberAndPeriod() {
        // given
        final Member member1 = new Member(new Name("냥인"), "nyangin@email.com", "1234", Role.USER);
        final String startAt1 = "18:00";
        final ReservationTime reservationTime1 = new ReservationTime(startAt1);
        final String themeName1 = "호러";
        final Theme theme1 = new Theme(themeName1, "매우 무섭습니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        final Reservation reservation1 = new Reservation(member1, "2034-05-08", reservationTime1, theme1);
        final Reservation reservation2 = new Reservation(member1, "2034-05-09", reservationTime1, theme1);

        given(reservationDao.findAllByThemeAndMemberAndPeriod(
                theme1.getId(), member1.getId(), LocalDate.parse("2034-05-08"), LocalDate.parse("2034-05-09")))
                .willReturn(List.of(reservation1, reservation2));

        // when
        final List<ReservationResponse> reservations = reservationService.findAllByThemeAndMemberAndPeriod(
                theme1.getId(), member1.getId(), LocalDate.parse("2034-05-08"), LocalDate.parse("2034-05-09"));

        // then
        assertAll(() -> {
            assertThat(reservations).hasSize(2)
                    .extracting(ReservationResponse::name)
                    .containsExactly("냥인", "냥인");
            assertThat(reservations).extracting(ReservationResponse::time)
                    .extracting(ReservationTimeResponse::startAt)
                    .containsExactly(startAt1, startAt1);
            assertThat(reservations).extracting(ReservationResponse::theme)
                    .extracting(ReservedThemeResponse::name)
                    .containsExactly(themeName1, themeName1);
        });
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void delete() {
        // given
        final Long existingId = 1L;

        given(reservationDao.existById(existingId))
                .willReturn(true);

        // when & then
        assertThatCode(() -> reservationService.delete(existingId))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("삭제하려는 예약이 존재하지 않는 경우 예외가 발생한다.")
    void throwExceptionWhenDeleteNotExistingReservation() {
        // given
        final Long notExistingId = 1L;

        given(reservationDao.existById(notExistingId))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> reservationService.delete(notExistingId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
