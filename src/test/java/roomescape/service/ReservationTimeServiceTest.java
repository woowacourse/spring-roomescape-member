package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.exception.ReservationBusinessException;
import roomescape.service.dto.ReservationTimeBookedRequest;
import roomescape.service.dto.ReservationTimeBookedResponse;
import roomescape.service.dto.ReservationTimeResponse;
import roomescape.service.dto.ReservationTimeSaveRequest;

@SpringBootTest
@Transactional
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    ReservationTime reservationTime;
    Theme theme;

    @BeforeEach
    void init() {
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("11:00")));
        reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("12:00")));
        theme = themeRepository.save(new Theme("이름", "설명", "썸네일"));
        reservationRepository.save(new Reservation(1L, "백호", LocalDate.parse("2024-12-12"), reservationTime, theme));
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
        assertThat(reservationTimeResponse.startAt()).isEqualTo(LocalTime.parse("01:00"));
    }

    @DisplayName("예약 시간 삭제")
    @Test
    void deleteTime() {
        var savedReservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("14:00")));
        final int size = reservationTimeService.getTimes().size();

        reservationTimeService.deleteTime(savedReservationTime.getId());
        assertThat(reservationTimeService.getTimes().size()).isEqualTo(size - 1);
    }

    @DisplayName("존재하지 않는 예약 시간 삭제")
    @Test
    void deleteTimeNotFound() {
        assertThatThrownBy(() -> reservationTimeService.deleteTime(3L))
                .isInstanceOf(ReservationBusinessException.class);
    }

    @DisplayName("예약이 존재하는 시간 삭제")
    @Test
    void deleteTimeExistReservation() {
        assertThatThrownBy(() -> reservationTimeService.deleteTime(2L))
                .isInstanceOf(ReservationBusinessException.class);
    }

    @DisplayName("중복된 시간 저장")
    @Test
    void saveDuplicatedTime() {
        assertThatThrownBy(() -> reservationTimeService.saveTime(new ReservationTimeSaveRequest(LocalTime.parse("11:00"))))
                .isInstanceOf(ReservationBusinessException.class);
    }

    @DisplayName("시간의 예약 여부 조회")
    @Test
    void findBookedTimes() {
        // given
        var reservationTimeBookedRequest = new ReservationTimeBookedRequest(LocalDate.parse("2024-12-12"), theme.getId());

        // when
        final List<ReservationTimeBookedResponse> timesWithBooked = reservationTimeService.getTimesWithBooked(
                reservationTimeBookedRequest);

        // then
        assertThat(timesWithBooked).hasSize(2)
                .extracting("startAt", "alreadyBooked")
                .containsExactly(
                        tuple(LocalTime.parse("11:00"), false),
                        tuple(LocalTime.parse("12:00"), true)
                );
    }
}

