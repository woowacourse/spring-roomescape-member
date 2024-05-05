package roomescape.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;
import roomescape.dto.reservation.ReservationTimeInfoResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({TimeRepository.class, ThemeRepository.class, ReservationRepository.class})
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class TimeRepositoryTest {

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("등록된 시간의 id를 통해 단건 조회할 수 있다.")
    void findTimeById() {
        //given
        timeRepository.insert(new Time(1L, LocalTime.of(17, 30)));

        // when
        Time foundTime = timeRepository.findById(1L);

        // then
        assertThat(foundTime.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("전체 시간 정보를 조회한다.")
    void readDbTimes() {
        // given
        timeRepository.insert(new Time(LocalTime.of(17, 30)));
        timeRepository.insert(new Time(LocalTime.of(19, 30)));

        // when
        List<Time> times = timeRepository.findAll();

        // then
        assertThat(times.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("하나의 시간만 등록한 경우, DB를 조회 했을 때 조회 결과 개수는 1개이다.")
    void postTimeIntoDb() {
        // given
        timeRepository.insert(new Time(1L, LocalTime.of(17, 30)));

        // when
        List<Time> times = timeRepository.findAll();

        // then
        assertThat(times.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("하나의 시간만 등록한 경우, 시간 삭제 뒤 DB를 조회 했을 때 조회 결과 개수는 0개이다.")
    void readTimesSizeFromDbAfterPostAndDelete() {
        // given
        timeRepository.insert(new Time(1L, LocalTime.of(17, 30)));

        // when
        timeRepository.deleteById(1L);
        List<Time> times = timeRepository.findAll();

        // then
        assertThat(times.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("테마ID와 날짜를 통해 예약 정보(전체 시간대, 예약 여부)를 조회한다.")
    void readReservationsByThemeIdAndDate() {
        // given
        Time time1 = timeRepository.insert(new Time(LocalTime.of(17, 30)));
        Time time2 = timeRepository.insert(new Time(LocalTime.of(17, 30)));
        Theme theme = themeRepository.insert(new Theme("테마명", "설명", "썸네일URL"));

        reservationRepository.insert(new Reservation(
                "브라운", LocalDate.of(2024, 4, 25), time1, theme
        ));
        reservationRepository.insert(new Reservation(
                "브라운", LocalDate.of(2024, 4, 26), time1, theme
        ));

        // when
        List<ReservationTimeInfoResponse> reservedTimeInfos = timeRepository.findByDateAndThemeId(
                LocalDate.of(2024, 4, 25),
                theme.getId()
        ).reservationTimes();

        // then
        Assertions.assertAll(
                () -> assertThat(reservedTimeInfos.size()).isEqualTo(2),
                () -> assertThat(reservedTimeInfos.get(0).timeId()).isEqualTo(time1.getId()),
                () -> assertThat(reservedTimeInfos.get(0).alreadyBooked()).isTrue(),
                () -> assertThat(reservedTimeInfos.get(1).timeId()).isEqualTo(time2.getId()),
                () -> assertThat(reservedTimeInfos.get(1).alreadyBooked()).isFalse()
        );
    }
}
