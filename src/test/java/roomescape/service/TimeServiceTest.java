package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.controller.time.TimeRequest;
import roomescape.controller.time.TimeResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReserveName;
import roomescape.domain.Theme;
import roomescape.repository.H2ReservationRepository;
import roomescape.repository.H2ReservationTimeRepository;
import roomescape.repository.H2ThemeRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.exception.TimeDuplicatedException;
import roomescape.service.exception.TimeNotFoundException;
import roomescape.service.exception.TimeUsedException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import({
        TimeService.class,
        H2ReservationRepository.class,
        H2ReservationTimeRepository.class,
        H2ThemeRepository.class
})
@JdbcTest
class TimeServiceTest {

    final List<TimeRequest> sampleTimes = List.of(
            new TimeRequest("08:00"),
            new TimeRequest("09:10"),
            new TimeRequest("10:20"),
            new TimeRequest("11:30")
    );

    @Autowired
    TimeService timeService;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ThemeRepository themeRepository;

    @Test
    @DisplayName("예약 시간 목록을 조회한다.")
    void getTimes() {
        // given
        final List<TimeResponse> expected = sampleTimes.stream()
                .map(timeService::addTime)
                .toList();

        // when
        final List<TimeResponse> actual = timeService.getTimes();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("해당 날짜와 테마에 해당하는 시간 목록을 예약 여부가 포함 되도록 조회한다.")
    void getTimesWithBooked() {
        // given
        final TimeResponse timeResponse = timeService.addTime(sampleTimes.get(0));
        final Theme theme = new Theme(null, "Theme 1", "Description 1", "Thumbnail 1");
        final LocalDate tomorrow = LocalDate.now().plusDays(1);
        final Reservation reservation = new Reservation(
                null,
                new ReserveName("User 1"),
                tomorrow,
                new ReservationTime(timeResponse.id()),
                null
        );

        final Theme savedTheme = themeRepository.save(theme);
        final Reservation assignedReservation = reservation.assignTheme(savedTheme);
        reservationRepository.save(assignedReservation);


        // when
        final List<TimeResponse> timesWithBooked = timeService.getTimesWithBooked(
                tomorrow.format(DateTimeFormatter.ISO_LOCAL_DATE),
                savedTheme.getId()
        );

        // then
        assertThat(timesWithBooked).hasSize(1);
        assertThat(timesWithBooked.get(0).booked()).isTrue();
    }

    @Test
    @DisplayName("예약 시간을 추가한다.")
    void addTIme() {
        // given
        final TimeRequest timeRequest = sampleTimes.get(0);

        // when
        final TimeResponse actual = timeService.addTime(timeRequest);
        final TimeResponse expected = new TimeResponse(actual.id(), timeRequest.startAt(), false);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("이미 예약된 시간을 추가할 경우 예외가 발생한다.")
    void addTimeDuplicated() {
        // given
        final TimeRequest timeRequest = sampleTimes.get(0);

        // when
        timeService.addTime(timeRequest);

        // then
        assertThatThrownBy(() -> timeService.addTime(timeRequest))
                .isInstanceOf(TimeDuplicatedException.class);
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void deleteTimePresent() {
        // given
        final Long id = timeService.addTime(sampleTimes.get(0)).id();

        // when & then
        assertThat(timeService.deleteTime(id)).isOne();
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간을 삭제할 경우 예외가 발생한다.")
    void deleteTimeNotExist() {
        assertThatThrownBy(() -> timeService.deleteTime(1L))
                .isInstanceOf(TimeNotFoundException.class);
    }

    @Test
    @DisplayName("예약이 된 시간을 삭제할 경우 예외가 발생한다.")
    void exceptionOnDeletingTimeAlreadyReserved() {
        // given
        final TimeResponse timeResponse = timeService.addTime(sampleTimes.get(0));
        final Long timeId = timeResponse.id();
        final Theme theme = new Theme(null, "Theme 1", "Description 1", "Thumbnail 1");
        final Reservation reservation = new Reservation(
                null,
                new ReserveName("User 1"),
                LocalDate.now().plusDays(1),
                new ReservationTime(timeId),
                null
        );

        final Theme savedTheme = themeRepository.save(theme);
        final Reservation assignedReservation = reservation.assignTheme(savedTheme);
        reservationRepository.save(assignedReservation);

        // when & then
        assertThatThrownBy(() -> timeService.deleteTime(timeId))
                .isInstanceOf(TimeUsedException.class);
    }
}
