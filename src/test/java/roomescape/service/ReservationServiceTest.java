package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Sql(scripts = {"/reset.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("예약 저장")
    @Test
    void saveReservation() {
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));
        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest("고구마",  LocalDate.parse("2025-11-11"), reservationTime.getId(), theme.getId());
        final ReservationResponse reservationResponse = reservationService.saveReservation(reservationSaveRequest);
        final Reservation savedReservation = reservationRepository.findById(reservationResponse.id()).get();

        assertThat(reservationResponse).isEqualTo(new ReservationResponse(savedReservation));
    }

    @DisplayName("존재하지 않는 예약 시간으로 예약 저장 시 예외 발생")
    @Test
    void timeForSaveReservationNotFound() {
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));
        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest("고구마", LocalDate.parse("2025-11-11"), 1L, theme.getId());

        assertThatThrownBy(() ->
            reservationService.saveReservation(reservationSaveRequest)
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 예약 시간입니다.");
    }

    @DisplayName("존재하지 않는 테마로 예약 저장 시 예외 발생")
    @Test
    void timeForSaveThemeNotFound() {
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest("고구마", LocalDate.parse("2025-11-11"), reservationTime.getId(), 1L);

        assertThatThrownBy(() ->
            reservationService.saveReservation(reservationSaveRequest)
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 테마입니다.");
    }

    @DisplayName("지나간 시간에 대한 예약 저장 시 예외 발생")
    @Test
    void saveReservationWithGoneDate() {
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));

        assertThatThrownBy(() -> {
            reservationService.saveReservation(new ReservationSaveRequest("백호", LocalDate.now().minusDays(1), reservationTime.getId(), theme.getId()));
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("지나간 시간에 대한 예약은 생성할 수 없습니다.");
    }

    @DisplayName("동일한 날짜, 시간, 테마에 대한 예약 저장 시 예외 발생")
    @Test
    void saveReservationWithDuplicatedDateTimeTheme() {
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));
        final LocalDate localDate = LocalDate.now().plusMonths(1);
        reservationService.saveReservation(new ReservationSaveRequest("감자", localDate, reservationTime.getId(), theme.getId()));

        assertThatThrownBy(() ->
                reservationService.saveReservation(new ReservationSaveRequest("감자감자", localDate, reservationTime.getId(), theme.getId()))
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("동일한 날짜, 시간, 테마에 대한 예약이 이미 존재합니다.");
    }

    @DisplayName("예약 목록 조회")
    @Test
    void getReservations() {
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));
        themeRepository.save(new Theme("이름2", "설명2", "썸네일2"));
        reservationService.saveReservation(new ReservationSaveRequest("감자", LocalDate.now().plusMonths(1), 1L, 1L));
        reservationService.saveReservation(new ReservationSaveRequest("감자", LocalDate.now().plusMonths(2), 2L, 2L));
        final List<ReservationResponse> reservationResponses = reservationService.getReservations();

        assertThat(reservationResponses).hasSize(2)
                .containsExactly(
                        new ReservationResponse(reservationRepository.findById(1L).get()),
                        new ReservationResponse(reservationRepository.findById(2L).get())
                );
    }

    @DisplayName("예약 삭제")
    @Test
    void deleteReservation() {
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));
        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest("고구마",  LocalDate.parse("2025-11-11"), reservationTime.getId(), theme.getId());
        final ReservationResponse reservationResponse = reservationService.saveReservation(reservationSaveRequest);
        reservationService.deleteReservation(reservationResponse.id());

        assertThat(reservationRepository.findById(reservationResponse.id())).isEmpty();
    }

    @DisplayName("존재하지 않는 예약 삭제 시 예외 발생")
    @Test
    void deleteReservationNotFound() {
        assertThatThrownBy(() -> {
            reservationService.deleteReservation(1L);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 예약입니다.");
    }
}
