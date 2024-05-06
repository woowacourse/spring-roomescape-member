package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.TimeCreateRequest;
import roomescape.dto.TimeMemberResponse;
import roomescape.dto.TimeResponse;
import roomescape.exception.IllegalTimeException;
import roomescape.repository.mock.InMemoryReservationDao;
import roomescape.repository.mock.InMemoryTimeDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeServiceTest {

    private final InMemoryTimeDao timeDao = new InMemoryTimeDao();
    private final InMemoryReservationDao reservationDao = new InMemoryReservationDao();
    private final ReservationTimeService reservationTimeService = new ReservationTimeService(timeDao, reservationDao);

    @BeforeEach
    void setUp() {
        timeDao.times = new ArrayList<>();
        reservationDao.reservations = new ArrayList<>();
    }

    @DisplayName("모든 시간을 조회한다.")
    @Test
    void findAll() {
        // given
        timeDao.times.add(new ReservationTime(1L, LocalTime.of(10, 0)));

        // when
        List<TimeResponse> allTimes = reservationTimeService.findAll();

        // then
        assertThat(allTimes).containsExactly(new TimeResponse(1L, LocalTime.of(10, 0)));
    }

    @DisplayName("이미 예약된 시간인지 여부를 포함해 모든 시간을 조회한다.")
    @Test
    void findAllWithBooking() {
        // given
        timeDao.times.add(new ReservationTime(1L, LocalTime.of(10, 0)));
        timeDao.times.add(new ReservationTime(2L, LocalTime.of(11, 0)));
        reservationDao.reservations.add(new Reservation(1L, "테니", LocalDate.of(2099,12,31),
                new ReservationTime(1L, LocalTime.of(10,0)),
                new Theme(1L, "테마이름", "설명", "썸네일")));

        // when
        List<TimeMemberResponse> allTimes = reservationTimeService.findAllWithBooking(LocalDate.of(2099, 12, 31), 1L);

        // then
        assertThat(allTimes).containsExactly(
                new TimeMemberResponse(1L, LocalTime.of(10, 0), true),
                new TimeMemberResponse(2L, LocalTime.of(11, 0), false));
    }

    @DisplayName("중복된 시간을 생성하려고 하면 예외를 발생한다.")
    @Test
    void save_duplicatedTime_IllegalTimeException() {
        // given
        timeDao.times.add(new ReservationTime(1L, LocalTime.of(10, 0)));

        // when && then
        assertThatThrownBy(() -> reservationTimeService.save(new TimeCreateRequest(1L, LocalTime.of(10, 0))))
                .isInstanceOf(IllegalTimeException.class);
    }

    @DisplayName("시간을 저장한 아이디를 반환한다.")
    @Test
    void save() {
        // given && when
        long id = reservationTimeService.save(new TimeCreateRequest(null, LocalTime.of(10, 0)));

        // then
        assertThat(id).isEqualTo(1L);
    }

//    @DisplayName("예약이 존재하는 경우 예약시간을 삭제하면 예외가 발생한다.")
//    @Test
//    void deleteTimeById_AbsenceId_ExceptionThrown() {
    // given
//        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "12:00");
//        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마이름", "설명", "썸네일");
//        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니", "2099-07-02", 1, 1);
//
//        // when & then
//        assertThatThrownBy(() -> reservationTimeService.deleteTimeById(1L))
//                .isInstanceOf(ExistReservationException.class);
//    }
}
