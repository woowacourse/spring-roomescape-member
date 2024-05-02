package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.dto.ReservationTimeBookedRequest;
import roomescape.dto.ReservationTimeBookedResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ReservationTimeSaveRequest;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    void init() {
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("11:00")));
        final ReservationTime savedReservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("12:00")));
        final Theme theme = themeRepository.save(new Theme("이름", "설명", "썸네일"));
        reservationRepository.save(new Reservation(1L, "백호", LocalDate.parse("2024-12-12"), savedReservationTime, theme));
    }

    @DisplayName("예약 시간 목록 조회")
    @Test
    void getTimes() {
        final List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.getTimes();
        assertThat(reservationTimeResponses.size()).isEqualTo(2);
    }

    @DisplayName("예약 시간 추가")
    @Test
    void saveTime() {
        final ReservationTimeSaveRequest reservationTimeSaveRequest = new ReservationTimeSaveRequest(LocalTime.parse("01:00"));
        final ReservationTimeResponse reservationTimeResponse = reservationTimeService.saveTime(reservationTimeSaveRequest);
        assertThat(reservationTimeResponse).isEqualTo(new ReservationTimeResponse(3L, LocalTime.parse("01:00")));
    }

    @DisplayName("예약 시간 삭제")
    @Test
    void deleteTime() {
        reservationTimeService.deleteTime(1L);
        assertThat(reservationTimeService.getTimes().size()).isEqualTo(1);
    }

    @DisplayName("존재하지 않는 예약 시간 삭제")
    @Test
    void deleteTimeNotFound() {
        assertThatThrownBy(() -> reservationTimeService.deleteTime(3L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약이 존재하는 시간 삭제")
    @Test
    void deleteTimeExistReservation() {
        assertThatThrownBy(() -> reservationTimeService.deleteTime(2L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("중복된 시간 저장")
    @Test
    void saveDuplicatedTime() {
        assertThatThrownBy(() -> reservationTimeService.saveTime(new ReservationTimeSaveRequest(LocalTime.parse("11:00"))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("시간의 예약 여부 조회")
    @Test
    void findBookedTImes() {
        // given
        var reservationTimeBookedRequest = new ReservationTimeBookedRequest(LocalDate.parse("2024-12-12"), 1L);

        // when
        final List<ReservationTimeBookedResponse> timesWithBooked = reservationTimeService.getTimesWithBooked(
                reservationTimeBookedRequest);

        // then
        assertThat(timesWithBooked).hasSize(2)
                .containsExactly(new ReservationTimeBookedResponse(1L, LocalTime.parse("11:00"), false),
                        new ReservationTimeBookedResponse(2L, LocalTime.parse("12:00"), true));
    }
}

