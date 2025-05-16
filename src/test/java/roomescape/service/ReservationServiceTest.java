package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.dto.ReservationRecipe;
import roomescape.dto.ReservationResponse;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    @DisplayName("예약을 추가한다.")
    void addReservationTest() {
        //given
        final ReservationRecipe reservationRecipe = new ReservationRecipe(1L, LocalDate.now().plusDays(1), 1L, 1L);

        //when
        final ReservationResponse expected = reservationService.addReservation(reservationRecipe);

        //then
        assertAll(
                () -> assertThat(expected.date()).isEqualTo(reservationRecipe.date()),
                () -> assertThat(expected.theme().id()).isEqualTo(reservationRecipe.themeId()),
                () -> assertThat(expected.time().id()).isEqualTo(reservationRecipe.timeId())
        );
    }

    @Test
    @DisplayName("id로 예약을 성공적으로 삭제한다.")
    void removeReservationSuccessTest() {
        //given
        final ReservationRecipe reservationRecipe = new ReservationRecipe(1L, LocalDate.now().plusDays(1), 1L, 1L);
        final ReservationResponse saved = reservationService.addReservation(reservationRecipe);

        //should
        assertThatCode(() -> reservationService.removeReservation(saved.id())).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 id로 예약을 삭제하여 예외가 발생한다.")
    void removeReservationFailTest() {
        //given
        final long id = 101010101010101010L;

        //should
        assertThatThrownBy(() -> reservationService.removeReservation(id)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("모든 예약을 가져온다.")
    void getAllReservationTest() {
        //given
        final ReservationRecipe reservationRecipe = new ReservationRecipe(1L, LocalDate.now().plusDays(1), 1L, 1L);
        reservationService.addReservation(reservationRecipe);

        //when
        final List<ReservationResponse> expected = reservationService.getAllReservations();

        //then
        assertThat(expected).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("필터링된 예약을 가져온다.")
    void getFilteredReservationsTest() {
        //given
        final long memberId = 1L;
        final LocalDate localDate = LocalDate.now().plusDays(1);
        final long themeId = 1L;
        final long timeId = 1L;
        final LocalDate dateFrom = LocalDate.now();
        final LocalDate dateTo = LocalDate.now().plusDays(1);
        final ReservationRecipe reservationRecipe = new ReservationRecipe(memberId, localDate, themeId, timeId);
        reservationService.addReservation(reservationRecipe);

        //when
        final List<ReservationResponse> expected = reservationService.getFilteredReservations(memberId,
                themeId, dateFrom, dateTo);

        //then
        assertThat(expected).hasSizeGreaterThanOrEqualTo(1);

    }

}
