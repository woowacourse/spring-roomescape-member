package roomescape.service.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.controller.time.TimeRequest;
import roomescape.controller.time.TimeResponse;
import roomescape.domain.*;
import roomescape.domain.exception.InvalidDateException;
import roomescape.repository.*;
import roomescape.repository.exception.TimeNotFoundException;
import roomescape.service.time.exception.TimeDuplicatedException;
import roomescape.service.time.exception.TimeUsedException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import({
        TimeService.class,
        H2ReservationRepository.class,
        H2ReservationTimeRepository.class,
        H2ThemeRepository.class,
        H2MemberRepository.class})
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
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("예약 시간 목록을 조회한다.")
    void getTimes() {
        // given
        List<TimeResponse> expected = sampleTimes.stream()
                .map(timeService::addTime)
                .toList();

        // when
        List<TimeResponse> actual = timeService.getTimes();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("해당 날짜와 테마에 해당하는 시간 목록을 예약 여부가 포함 되도록 조회한다.")
    void getTimesWithBooked() {
        // given
        TimeResponse timeResponse = timeService.addTime(sampleTimes.get(0));
        Theme theme = new Theme(null, "Theme 1", "Description 1", "Thumbnail 1");
        Member member = new Member(null, "User", "user@test.com", "user", Role.USER);
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Reservation reservation = new Reservation(
                null,
                tomorrow,
                new ReservationTime(timeResponse.id()),
                null,
                null
        );

        Theme savedTheme = themeRepository.save(theme);
        Member savedMember = memberRepository.save(member);
        Reservation assignedReservation = reservation
                .assignTheme(savedTheme)
                .assignMember(savedMember);
        reservationRepository.save(assignedReservation);


        // when
        List<TimeResponse> timesWithBooked = timeService.getTimesWithBooked(
                tomorrow.format(DateTimeFormatter.ISO_LOCAL_DATE),
                savedTheme.getId()
        );

        // then
        assertThat(timesWithBooked).hasSize(1);
        assertThat(timesWithBooked.get(0).booked()).isTrue();
    }

    @Test
    @DisplayName("유효하지 않는 날짜 형식으로 시간 목록의 예약 여부를 요청할 경우 예외가 발생한다.")
    void getTimesWithBookedInvalidDate() {
        assertThatThrownBy(() -> timeService.getTimesWithBooked("2024-05-111", 1L))
                .isInstanceOf(InvalidDateException.class);
    }

    @Test
    @DisplayName("예약 시간을 추가한다.")
    void addTIme() {
        // given
        TimeRequest timeRequest = sampleTimes.get(0);

        // when
        TimeResponse actual = timeService.addTime(timeRequest);
        TimeResponse expected = new TimeResponse(actual.id(), timeRequest.startAt(), false);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("이미 예약된 시간을 추가할 경우 예외가 발생한다.")
    void addTimeDuplicated() {
        // given
        TimeRequest timeRequest = sampleTimes.get(0);

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
        Long id = timeService.addTime(sampleTimes.get(0)).id();

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
        TimeResponse timeResponse = timeService.addTime(sampleTimes.get(0));
        Long timeId = timeResponse.id();
        Theme theme = new Theme(null, "Theme 1", "Description 1", "Thumbnail 1");
        Member member = new Member(null, "User", "user@test.com", "user", Role.USER);
        Reservation reservation = new Reservation(
                null,
                LocalDate.now().plusDays(1),
                new ReservationTime(timeId),
                null,
                null
        );

        Theme savedTheme = themeRepository.save(theme);
        Member savedMember = memberRepository.save(member);
        Reservation assignedReservation = reservation
                .assignTheme(savedTheme)
                .assignMember(savedMember);
        reservationRepository.save(assignedReservation);

        // when & then
        assertThatThrownBy(() -> timeService.deleteTime(timeId))
                .isInstanceOf(TimeUsedException.class);
    }
}
