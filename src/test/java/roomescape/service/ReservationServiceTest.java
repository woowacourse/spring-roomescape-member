package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.ReservationRequest;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    void 예약을_추가한다() {
        ReservationTime reservationTime = createReservationTime(LocalTime.of(10, 0));
        Theme theme = createTheme();

        ReservationRequest request = new ReservationRequest(
                "브라운",
                LocalDate.parse("2026-05-15"),
                reservationTime.getId(),
                theme.getId()
        );

        Reservation reservation = reservationService.addReservation(request);

        assertThat(reservation.getId()).isNotNull();
        assertThat(reservation.getName()).isEqualTo("브라운");
        assertThat(reservation.getDate()).isEqualTo(LocalDate.parse("2026-05-15"));
        assertThat(reservation.getTime().getId()).isEqualTo(reservationTime.getId());
        assertThat(reservation.getTime().getStartAt()).isEqualTo(reservationTime.getStartAt());
        assertThat(reservation.getTheme().getId()).isEqualTo(theme.getId());
        assertThat(reservation.getTheme().getName()).isEqualTo(theme.getName());
    }

    @Test
    void 모든_예약을_조회한다() {
        ReservationTime reservationTime = createReservationTime(LocalTime.of(10, 0));
        Theme theme = createTheme();

        ReservationRequest request = new ReservationRequest(
                "브라운",
                LocalDate.parse("2026-05-15"),
                reservationTime.getId(),
                theme.getId()
        );

        reservationService.addReservation(request);

        List<Reservation> reservations = reservationService.getReservations();

        assertThat(reservations).hasSize(1);

        Reservation result = reservations.getFirst();
        assertThat(result.getName()).isEqualTo("브라운");
        assertThat(result.getDate()).isEqualTo(LocalDate.parse("2026-05-15"));
        assertThat(result.getTime().getId()).isEqualTo(reservationTime.getId());
        assertThat(result.getTheme().getId()).isEqualTo(theme.getId());
    }

    @Test
    void id에_맞는_예약을_조회한다() {
        ReservationTime reservationTime = createReservationTime(LocalTime.of(10, 0));
        Theme theme = createTheme();

        ReservationRequest request = new ReservationRequest(
                "브라운",
                LocalDate.parse("2026-05-15"),
                reservationTime.getId(),
                theme.getId()
        );

        Reservation savedReservation = reservationService.addReservation(request);

        Reservation reservation = reservationService.getReservation(savedReservation.getId());

        assertThat(reservation.getId()).isEqualTo(savedReservation.getId());
        assertThat(reservation.getName()).isEqualTo("브라운");
        assertThat(reservation.getDate()).isEqualTo(LocalDate.parse("2026-05-15"));
        assertThat(reservation.getTime().getId()).isEqualTo(reservationTime.getId());
        assertThat(reservation.getTheme().getId()).isEqualTo(theme.getId());
    }

    @Test
    void 예약을_삭제한다() {
        ReservationTime reservationTime = createReservationTime(LocalTime.of(10, 0));
        Theme theme = createTheme();

        ReservationRequest request = new ReservationRequest(
                "브라운",
                LocalDate.parse("2026-05-15"),
                reservationTime.getId(),
                theme.getId()
        );

        Reservation savedReservation = reservationService.addReservation(request);

        reservationService.deleteReservation(savedReservation.getId());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> reservationService.getReservation(savedReservation.getId()))
                .withMessageContaining("존재하지 않는 예약 ID입니다.");
    }

    private ReservationTime createReservationTime(LocalTime time) {
        ReservationTime reservationTime = new ReservationTime(time);
        Long id = timeRepository.save(reservationTime);
        return new ReservationTime(id, reservationTime.getStartAt());
    }

    private Theme createTheme() {
        Theme theme = new Theme("방탈출 제목", "방탈출 설명", "thumbnail.png");
        Long id = themeRepository.save(theme);
        return new Theme(
                id,
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnailImageUrl()
        );
    }
}
