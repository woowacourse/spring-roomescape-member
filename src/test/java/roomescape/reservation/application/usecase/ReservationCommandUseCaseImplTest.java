package roomescape.reservation.application.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.DuplicateException;
import roomescape.common.exception.NotFoundException;
import roomescape.reservation.application.dto.CreateReservationServiceRequest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationId;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.ReserverName;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeDescription;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.domain.ThemeThumbnail;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ReservationCommandUseCaseImplTest {

    @Autowired
    private ReservationCommandUseCaseImpl reservationCommandUseCase;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("예약을 생성할 수 있다")
    void createAndFindReservation() {
        // given
        final ReservationTime reservationTime = reservationTimeRepository.save(
                ReservationTime.withoutId(
                        LocalTime.of(10, 0)));

        final Theme theme = themeRepository.save(
                Theme.withoutId(ThemeName.from("공포"),
                        ThemeDescription.from("지구별 방탈출 최고"),
                        ThemeThumbnail.from("www.making.com")));

        final CreateReservationServiceRequest requestDto = new CreateReservationServiceRequest(
                ReserverName.from("브라운"),
                ReservationDate.from(LocalDate.of(2025, 8, 5)),
                reservationTime.getId(),
                theme.getId());

        // when
        final Reservation reservation = reservationCommandUseCase.create(requestDto);

        // then
        final Reservation found = reservationRepository.findById(reservation.getId())
                .orElseThrow(NoSuchElementException::new);

        assertThat(reservation).isEqualTo(found);
        assertThat(reservation.getId()).isEqualTo(found.getId());
        assertThat(reservation.getName()).isEqualTo(found.getName());
        assertThat(reservation.getDate()).isEqualTo(found.getDate());
        assertThat(reservation.getTime()).isEqualTo(found.getTime());
        assertThat(reservation.getTheme()).isEqualTo(found.getTheme());
    }

    @Test
    @DisplayName("중복된 예약을 생성할 수 없다.")
    void existsReservation() {
        // given
        final ReservationTime reservationTime = reservationTimeRepository.save(
                ReservationTime.withoutId(
                        LocalTime.of(10, 0)));

        final Theme theme = themeRepository.save(
                Theme.withoutId(ThemeName.from("공포"),
                        ThemeDescription.from("지구별 방탈출 최고"),
                        ThemeThumbnail.from("www.making.com")));

        final Reservation savedReservation = reservationCommandUseCase.create(
                new CreateReservationServiceRequest(
                        ReserverName.from("브라운"),
                        ReservationDate.from(LocalDate.of(2025, 8, 5)),
                        reservationTime.getId(),
                        theme.getId()
                ));

        // when
        // then
        assertThatThrownBy(() -> reservationCommandUseCase.create(
                new CreateReservationServiceRequest(
                        ReserverName.from("강산"),
                        ReservationDate.from(LocalDate.of(2025, 8, 5)),
                        reservationTime.getId(),
                        theme.getId())))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("RESERVATION already exists. params={ReservationDate=ReservationDate(value=2025-08-05), ReservationTimeId=ReservationTimeId(3), ThemeId=ThemeId(3)}");
    }

    @Test
    @DisplayName("예약을 삭제할 수 있다")
    void deleteReservation() {
        // given
        final ReservationTime reservationTime = reservationTimeRepository.save(
                ReservationTime.withoutId(
                        LocalTime.of(10, 0)));

        final Theme theme = themeRepository.save(
                Theme.withoutId(ThemeName.from("공포"),
                        ThemeDescription.from("지구별 방탈출 최고"),
                        ThemeThumbnail.from("www.making.com")));

        final Reservation reservation = reservationRepository.save(
                Reservation.withoutId(
                        ReserverName.from("브라운"),
                        ReservationDate.from(LocalDate.of(2025, 8, 5)),
                        reservationTime,
                        theme));

        // when
        reservationCommandUseCase.delete(reservation.getId());

        // then
        assertThat(reservationRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하려 하면 예외가 발생한다")
    void deleteNonExistentReservation() {
        // given
        final ReservationId id = ReservationId.from(-1L);

        // when
        // then
        assertThatThrownBy(() -> reservationCommandUseCase.delete(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Tried to delete [RESERVATION] that does not exist. params={ReservationId=ReservationId(-1)}");
    }
}
