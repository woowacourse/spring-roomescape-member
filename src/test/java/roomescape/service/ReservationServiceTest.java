package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.DuplicateReservationException;
import roomescape.common.exception.ForbiddenException;
import roomescape.common.exception.InvalidReservationException;
import roomescape.common.exception.ResourceNotFoundException;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationUpdateRequest;
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
    void 자신의_이름에_해당하는_예약_목록을_조회한다() {
        ReservationTime tenClock = createReservationTime(TEN);
        ReservationTime twelveClock = createReservationTime(LocalTime.of(12, 0));
        Theme theme = createTheme();

        String name = "브라운";
        String anotherName = "브리";
        ReservationRequest requestBrown = new ReservationRequest(
                name,
                FUTURE_SECOND_DATE,
                tenClock.getId(),
                theme.getId()
        );
        ReservationRequest requestBrie = new ReservationRequest(
                anotherName,
                FUTURE_SECOND_DATE,
                twelveClock.getId(),
                theme.getId()
        );

        reservationService.addReservation(requestBrown);
        reservationService.addReservation(requestBrie);

        List<Reservation> reservations = reservationService.getReservationsByName(name);

        assertThat(reservations).hasSize(1);

        Reservation result = reservations.getFirst();
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getDate()).isEqualTo(FUTURE_SECOND_DATE);
        assertThat(result.getTime().getId()).isEqualTo(tenClock.getId());
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

    @Test
    void 없는_예약을_삭제할_수_없다() {
        assertThatThrownBy(() -> reservationService.deleteReservation(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }

    @Test
    void 내_예약을_취소한다() {
        ReservationTime reservationTime = createReservationTime(TEN);
        Theme theme = createTheme();
        String name = "브라운";

        ReservationRequest request = new ReservationRequest(
                name,
                FUTURE_SECOND_DATE,
                reservationTime.getId(),
                theme.getId()
        );
        Reservation reservation = reservationService.addReservation(request);

        reservationService.cancelMyReservation(reservation.getId(), name);

        assertThatThrownBy(() -> reservationService.getReservation(reservation.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }

    @Test
    void 사용자_예약을_취소할_때_존재하지_않는_예약이면_예외() {
        assertThatThrownBy(() -> reservationService.cancelMyReservation(1L, "브라운"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }

    @Test
    void 예약을_취소할_때_예약한_이름과_사용자_이름이_일치하지_않으면_예외() {
        ReservationTime reservationTime = createReservationTime(TEN);
        Theme theme = createTheme();
        String name = "브라운";

        ReservationRequest request = new ReservationRequest(
                name,
                FUTURE_SECOND_DATE,
                reservationTime.getId(),
                theme.getId()
        );
        Reservation reservation = reservationService.addReservation(request);

        String userName = "브리";

        assertThatThrownBy(() -> reservationService.cancelMyReservation(reservation.getId(), userName))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("선택한 예약과 사용자 이름이 일치하지 않습니다.");
    }

    @Test
    @Sql("/data_relative_dates.sql")
    void 예약을_취소할_때_이미_지난_예약이면_예외() {
        String name = "김민수";
        Reservation pastReservation = reservationService.getReservationsByName(name).getFirst();

        LocalDateTime dateTime = LocalDateTime.of(
                pastReservation.getDate(),
                pastReservation.getTime().getStartAt()
        );

        assertThat(dateTime.isBefore(LocalDateTime.now())).isTrue();
        assertThatThrownBy(() -> reservationService.cancelMyReservation(pastReservation.getId(), name))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("이미 지난 예약은 취소할 수 없습니다.");
    }

    @Test
    void 선택한_예약에_내_이름이_일치하면_예약의_날짜와_시간을_수정할_수_있다() {
        ReservationTime reservationTime = createReservationTime(TEN);
        Theme theme = createTheme();
        String name = "브라운";
        ReservationRequest request = new ReservationRequest(
                name,
                FUTURE_SECOND_DATE,
                reservationTime.getId(),
                theme.getId()
        );
        Reservation reservation = reservationService.addReservation(request);

        ReservationTime updateTime = createReservationTime(LocalTime.of(12, 0));
        LocalDate updateDate = FUTURE_SECOND_DATE.plusDays(1);
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(
                updateDate,
                updateTime.getId()
        );

        Reservation updatedReservation = reservationService.updateReservation(reservation.getId(), name, updateRequest);

        assertThat(updatedReservation.getId()).isNotNull();
        assertThat(updatedReservation.getName()).isEqualTo(name);
        assertThat(updatedReservation.getDate()).isEqualTo(updateDate);
        assertThat(updatedReservation.getTime().getId()).isEqualTo(updateTime.getId());
        assertThat(updatedReservation.getTime().getStartAt()).isEqualTo(updateTime.getStartAt());
        assertThat(updatedReservation.getTheme().getId()).isEqualTo(theme.getId());
        assertThat(updatedReservation.getTheme().getName()).isEqualTo(theme.getName());
    }

    @Test
    void 예약을_수정할_때_존재하지_않는_예약이면_예외() {
        ReservationTime updateTime = createReservationTime(TEN);
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(
                FUTURE_SECOND_DATE,
                updateTime.getId()
        );

        assertThatThrownBy(() -> reservationService.updateReservation(1L, "브라운", updateRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }

    @Test
    void 예약을_수정할_때_예약한_이름과_사용자_이름이_일치하지_않으면_예외() {
        ReservationTime reservationTime = createReservationTime(TEN);
        Theme theme = createTheme();
        String name = "브라운";
        ReservationRequest request = new ReservationRequest(
                name,
                FUTURE_SECOND_DATE,
                reservationTime.getId(),
                theme.getId()
        );
        Reservation reservation = reservationService.addReservation(request);

        ReservationTime updateTime = createReservationTime(LocalTime.of(12, 0));
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(
                FUTURE_SECOND_DATE.plusDays(1),
                updateTime.getId()
        );
        String userName = "브리";

        assertThatThrownBy(() -> reservationService.updateReservation(reservation.getId(), userName, updateRequest))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("선택한 예약과 사용자 이름이 일치하지 않습니다.");
    }

    @Test
    void 예약을_수정할_때_존재하지_않는_시간_ID이면_예외() {
        ReservationTime reservationTime = createReservationTime(TEN);
        Theme theme = createTheme();
        String name = "브라운";
        ReservationRequest request = new ReservationRequest(
                name,
                FUTURE_SECOND_DATE,
                reservationTime.getId(),
                theme.getId()
        );
        Reservation reservation = reservationService.addReservation(request);

        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(
                FUTURE_SECOND_DATE.plusDays(1),
                999L
        );

        assertThatThrownBy(() -> reservationService.updateReservation(reservation.getId(), name, updateRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("예약할 수 없는 시간입니다.");
    }

    @Test
    @Sql("/data_relative_dates.sql")
    void 예약을_수정할_때_이미_지난_예약이면_예외() {
        String name = "김민수";
        Reservation pastReservation = reservationService.getReservationsByName(name).getFirst();
        Long pastReservationId = pastReservation.getId();

        ReservationTime updateTime = timeRepository.findAll().getFirst();
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(
                FUTURE_SECOND_DATE,
                updateTime.getId()
        );

        LocalDateTime dateTime = LocalDateTime.of(pastReservation.getDate(), pastReservation.getTime().getStartAt());
        assertThat(dateTime.isBefore(LocalDateTime.now())).isTrue();
        assertThatThrownBy(() -> reservationService.updateReservation(pastReservationId, name, updateRequest))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("이미 지난 예약은 변경할 수 없습니다.");
    }

    @Test
    void 예약을_수정할_때_변경하려는_날짜와_시간이_과거이면_예외() {
        ReservationTime reservationTime = createReservationTime(TEN);
        Theme theme = createTheme();
        String name = "브라운";
        ReservationRequest request = new ReservationRequest(
                name,
                FUTURE_SECOND_DATE,
                reservationTime.getId(),
                theme.getId()
        );
        Reservation reservation = reservationService.addReservation(request);

        ReservationTime pastTime = createReservationTime(LocalTime.now().minusMinutes(1));
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(
                LocalDate.now(),
                pastTime.getId()
        );

        assertThatThrownBy(() -> reservationService.updateReservation(reservation.getId(), name, updateRequest))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("지난 날짜와 시간으로는 예약을 수정할 수 없습니다.");
    }

    @Test
    void 예약을_수정할_때_변경하려는_예약_시간이_이미_차_있으면_예외() {
        ReservationTime tenClock = createReservationTime(TEN);
        ReservationTime twelveClock = createReservationTime(LocalTime.of(12, 0));
        Theme theme = createTheme();

        String name = "브라운";
        ReservationRequest request = new ReservationRequest(
                name,
                FUTURE_SECOND_DATE,
                tenClock.getId(),
                theme.getId()
        );
        Reservation reservation = reservationService.addReservation(request);

        ReservationRequest anotherRequest = new ReservationRequest(
                "브리",
                FUTURE_SECOND_DATE.plusDays(1),
                twelveClock.getId(),
                theme.getId()
        );
        reservationService.addReservation(anotherRequest);

        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(
                FUTURE_SECOND_DATE.plusDays(1),
                twelveClock.getId()
        );

        assertThatThrownBy(() -> reservationService.updateReservation(reservation.getId(), name, updateRequest))
                .isInstanceOf(DuplicateReservationException.class)
                .hasMessage("이미 예약된 시간입니다.");
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
