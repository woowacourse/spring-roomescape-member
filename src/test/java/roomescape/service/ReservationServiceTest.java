package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationCreateRequest;
import roomescape.exception.ExistReservationException;
import roomescape.exception.IllegalReservationException;
import roomescape.repository.mock.InMemoryReservationDao;
import roomescape.repository.mock.InMemoryThemeDao;
import roomescape.repository.mock.InMemoryTimeDao;

class ReservationServiceTest {

    private final InMemoryReservationDao reservationDao = new InMemoryReservationDao();
    private final InMemoryTimeDao timeDao = new InMemoryTimeDao();
    private final InMemoryThemeDao themeDao = new InMemoryThemeDao();
    private final ReservationService reservationService = new ReservationService(reservationDao, timeDao, themeDao);

    @BeforeEach
    void setUp() {
        timeDao.times = new ArrayList<>();
        reservationDao.reservations = new ArrayList<>();
    }

    @DisplayName("예약을 생성한 아이디를 반환한다.")
    @Test
    void save() {
        // given
        timeDao.times.add(new ReservationTime(1L, LocalTime.of(10, 0)));
        themeDao.themes.add(new Theme(1L,"이름", "설명", "썸네일"));


        // when
        long id = reservationService.save(new ReservationCreateRequest(null, "커비", LocalDate.of(2099, 12, 31), 1L, 1L));

        // then
        assertThat(id).isEqualTo(1);
    }

    @DisplayName("과거 날짜의 예약을 생성하려고 하면 예외를 발생한다.")
    @Test
    void save_pastDate_IllegalReservationException() {
        // given
        timeDao.times.add(new ReservationTime(1L, LocalTime.of(10, 0)));
        themeDao.themes.add(new Theme(1L,"이름", "설명", "썸네일"));

        // when && then
        assertThatThrownBy(() -> reservationService.save(new ReservationCreateRequest(null, "커비", LocalDate.of(2000,1,1), 1L, 1L)))
                .isInstanceOf(IllegalReservationException.class);
    }

    @DisplayName("과거 시간의 예약을 생성하려고 하면 예외를 발생한다.")
    @Test
    void save_pastTime_IllegalTimeException() {
        // given
        timeDao.times.add(new ReservationTime(1L, LocalTime.of(10, 0)));
        themeDao.themes.add(new Theme(1L,"이름", "설명", "썸네일"));

        // when && then
        assertThatThrownBy(() -> reservationService.save(new ReservationCreateRequest(null, "커비", LocalDate.now(), 1L, 1L)))
                .isInstanceOf(IllegalReservationException.class);
    }


    @DisplayName("중복된 예약을 생성하려고 하면 예외를 발생한다.")
    @Test
    void save_duplicatedTime_IllegalTimeException() {
        // given
        timeDao.times.add(new ReservationTime(1L, LocalTime.of(10, 0)));
        themeDao.themes.add(new Theme(1L,"이름", "설명", "썸네일"));
        reservationDao.reservations.add(new Reservation(1L, "커비", LocalDate.of(2099,12,31),
                new ReservationTime(1L, LocalTime.of(10,0)),
                new Theme(1L, "이름", "설명", "썸네일")));

        // when && then
        assertThatThrownBy(() -> reservationService.save(new ReservationCreateRequest(null, "커비", LocalDate.of(2099,12,31), 1L, 1L)))
                .isInstanceOf(ExistReservationException.class);
    }
}
