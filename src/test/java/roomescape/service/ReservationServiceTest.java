package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.DuplicateReservationException;
import roomescape.common.exception.InvalidReservationException;
import roomescape.common.exception.ResourceNotFoundException;
import roomescape.dto.ReservationRequest;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    private static final LocalDate FUTURE_SECOND_DATE = LocalDate.now().plusDays(2);
    private static final LocalTime TEN = LocalTime.of(10, 0);

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    void 예약을_추가한다() {
        ReservationTime reservationTime = createReservationTime(TEN);
        Theme theme = createTheme();

        ReservationRequest request = new ReservationRequest(
                "브라운",
                FUTURE_SECOND_DATE,
                reservationTime.getId(),
                theme.getId()
        );

        Reservation reservation = reservationService.addReservation(request);

        assertThat(reservation.getId()).isNotNull();
        assertThat(reservation.getName()).isEqualTo("브라운");
        assertThat(reservation.getDate()).isEqualTo(FUTURE_SECOND_DATE);
        assertThat(reservation.getTime().getId()).isEqualTo(reservationTime.getId());
        assertThat(reservation.getTime().getStartAt()).isEqualTo(reservationTime.getStartAt());
        assertThat(reservation.getTheme().getId()).isEqualTo(theme.getId());
        assertThat(reservation.getTheme().getName()).isEqualTo(theme.getName());
    }

    @Test
    void 예약을_추가할_때_예약시간이_없는_경우_예외() {
        Theme theme = createTheme();

        ReservationRequest request = new ReservationRequest(
                "브라운",
                FUTURE_SECOND_DATE,
                1L,
                theme.getId()
        );

        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("예약할 수 없는 시간입니다.");
    }

    @Test
    void 예약을_추가할_때_테마가_없는_경우_예외() {
        ReservationTime reservationTime = createReservationTime(TEN);

        ReservationRequest request = new ReservationRequest(
                "브라운",
                FUTURE_SECOND_DATE,
                reservationTime.getId(),
                1L
        );

        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("예약할 수 없는 테마입니다.");
    }

    @Test
    void 예약을_추가할_때_이미_존재한_예약인_경우_예외() {
        ReservationTime reservationTime = createReservationTime(TEN);
        Theme theme = createTheme();

        ReservationRequest request = new ReservationRequest(
                "브라운",
                FUTURE_SECOND_DATE,
                reservationTime.getId(),
                theme.getId()
        );

        reservationService.addReservation(request);

        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(DuplicateReservationException.class)
                .hasMessage("이미 예약된 시간입니다.");
    }

    @Test
    void 예약을_추가할_때_지난_날짜인_경우_예외() {
        ReservationTime reservationTime = createReservationTime(TEN);
        Theme theme = createTheme();

        LocalDate pastDate = LocalDate.now().minusDays(1);
        ReservationRequest request = new ReservationRequest(
                "브라운",
                pastDate,
                reservationTime.getId(),
                theme.getId()
        );

        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("지난 날짜와 시간으로는 예약할 수 없습니다.");
    }

    @Test
    void 예약을_추가할_때_지난_시간인_경우_예외() {
        LocalTime pastTime = LocalTime.now().minusMinutes(1);
        ReservationTime reservationTime = createReservationTime(pastTime);
        Theme theme = createTheme();

        ReservationRequest request = new ReservationRequest(
                "브라운",
                LocalDate.now(),
                reservationTime.getId(),
                theme.getId()
        );

        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("지난 날짜와 시간으로는 예약할 수 없습니다.");
    }

    @Test
    void 모든_예약을_조회한다() {
        ReservationTime reservationTime = createReservationTime(TEN);
        Theme theme = createTheme();

        ReservationRequest request = new ReservationRequest(
                "브라운",
                FUTURE_SECOND_DATE,
                reservationTime.getId(),
                theme.getId()
        );

        reservationService.addReservation(request);

        List<Reservation> reservations = reservationService.getReservations();

        assertThat(reservations).hasSize(1);

        Reservation result = reservations.getFirst();
        assertThat(result.getName()).isEqualTo("브라운");
        assertThat(result.getDate()).isEqualTo(FUTURE_SECOND_DATE);
        assertThat(result.getTime().getId()).isEqualTo(reservationTime.getId());
        assertThat(result.getTheme().getId()).isEqualTo(theme.getId());
    }

    @Test
    void id에_맞는_예약을_조회한다() {
        ReservationTime reservationTime = createReservationTime(TEN);
        Theme theme = createTheme();

        ReservationRequest request = new ReservationRequest(
                "브라운",
                FUTURE_SECOND_DATE,
                reservationTime.getId(),
                theme.getId()
        );

        Reservation savedReservation = reservationService.addReservation(request);

        Reservation reservation = reservationService.getReservation(savedReservation.getId());

        assertThat(reservation.getId()).isEqualTo(savedReservation.getId());
        assertThat(reservation.getName()).isEqualTo("브라운");
        assertThat(reservation.getDate()).isEqualTo(FUTURE_SECOND_DATE);
        assertThat(reservation.getTime().getId()).isEqualTo(reservationTime.getId());
        assertThat(reservation.getTheme().getId()).isEqualTo(theme.getId());
    }

    @Test
    void 예약을_삭제한다() {
        ReservationTime reservationTime = createReservationTime(TEN);
        Theme theme = createTheme();

        ReservationRequest request = new ReservationRequest(
                "브라운",
                FUTURE_SECOND_DATE,
                reservationTime.getId(),
                theme.getId()
        );

        Reservation savedReservation = reservationService.addReservation(request);

        reservationService.deleteReservation(savedReservation.getId());

        assertThatThrownBy(() -> reservationService.getReservation(savedReservation.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("존재하지 않는 예약입니다.");
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
