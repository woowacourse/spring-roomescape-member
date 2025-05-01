package roomescape.reservation.service.usecase;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.service.dto.AvailableReservationTimeServiceRequest;
import roomescape.reservation.service.dto.AvailableReservationTimeServiceResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.domain.ReserverName;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeDescription;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.domain.ThemeThumbnail;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class ReservationQueryUseCaseImplTest {

    @Autowired
    private ReservationQueryUseCaseImpl reservationQueryUseCase;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("예약을 조회할 수 있다")
    void createAndFindReservation() {
        // given
        final ReservationTime reservationTime = reservationTimeRepository.save(
                ReservationTime.withoutId(
                        LocalTime.of(10, 0)));

        final Theme theme = themeRepository.save(
                Theme.withoutId(ThemeName.from("공포"),
                        ThemeDescription.from("지구별 방탈출 최고"),
                        ThemeThumbnail.from("www.making.com")));

        final Reservation given1 = Reservation.withoutId(
                ReserverName.from("강산"),
                ReservationDate.from(LocalDate.now().plusDays(1)),
                reservationTime,
                theme);

        final Reservation given2 = Reservation.withoutId(
                ReserverName.from("강산2"),
                ReservationDate.from(LocalDate.now().plusDays(1)),
                reservationTime,
                theme);

        final Reservation saved1 = reservationRepository.save(given1);
        final Reservation saved2 = reservationRepository.save(given2);

        // when
        final List<Reservation> reservations = reservationQueryUseCase.getAll();

        // then
        assertThat(reservations).hasSize(2);
        final Reservation found1 = reservations.getFirst();
        final Reservation found2 = reservations.get(1);

        assertAll(() -> {
            assertThat(found1).isEqualTo(saved1);
            assertThat(found2).isEqualTo(saved2);
        });
    }

    @Test
    @DisplayName("특정 날짜와 테마에 대한 예약 가능 여부가 포함된 시간 정보를 받을 수 있다")
    void getTimesWithAvailability() {
        // given
        final ReservationTime booked = reservationTimeRepository.save(
                ReservationTime.withoutId(
                        LocalTime.of(10, 0)));

        final ReservationTime unbooked = reservationTimeRepository.save(
                ReservationTime.withoutId(
                        LocalTime.of(11, 0)));

        final Theme theme = themeRepository.save(
                Theme.withoutId(ThemeName.from("공포"),
                        ThemeDescription.from("지구별 방탈출 최고"),
                        ThemeThumbnail.from("www.making.com")));

        final ReservationDate date = ReservationDate.from(LocalDate.now().plusDays(1));

        final Reservation reservation = reservationRepository.save(Reservation.withoutId(
                ReserverName.from("강산"),
                date,
                booked,
                theme));

        // when
        final List<AvailableReservationTimeServiceResponse> timesWithAvailability = reservationQueryUseCase.getTimesWithAvailability(
                new AvailableReservationTimeServiceRequest(date.getValue(), theme.getId()));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {

            assertThat(timesWithAvailability)
                    .hasSize(2);

            assertThat(timesWithAvailability.stream().filter(AvailableReservationTimeServiceResponse::isBooked))
                    .hasSize(1);

            assertThat(timesWithAvailability.stream()
                    .filter(AvailableReservationTimeServiceResponse::isBooked)
                    .map(AvailableReservationTimeServiceResponse::startAt)
                    .findFirst()
                    .orElseThrow()
            ).isEqualTo(booked.getValue());
        });
    }
}
