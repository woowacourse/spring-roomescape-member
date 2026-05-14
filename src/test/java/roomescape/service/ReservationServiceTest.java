package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.TimeDao;
import roomescape.dao.jdbc.ReservationJdbcDao;
import roomescape.dao.jdbc.ThemeJdbcDao;
import roomescape.dao.jdbc.TimeJdbcDao;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.domain.vo.Name;
import roomescape.dto.request.ReservationRequestDto;

@JdbcTest
@Import({ReservationService.class, ReservationJdbcDao.class, TimeJdbcDao.class, ThemeJdbcDao.class})
@ActiveProfiles("test")
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private TimeDao timeDao;
    @Autowired
    private ThemeDao themeDao;

    private Time savedTime1;
    private Time savedTime2;
    private Theme savedTheme1;
    private Theme savedTheme2;
    private ReservationRequestDto requestDto1;
    private ReservationRequestDto requestDto2;

    @BeforeEach
    void setUp() {
        savedTime1 = timeDao.insert(new Time(LocalTime.of(13, 0)));
        savedTime2 = timeDao.insert(new Time(LocalTime.of(14, 0)));
        savedTheme1 = themeDao.insert(new Theme(new Name("방탈출 이름1"), "http://thumbnail_url", "방탈출을 할 수 있다."));
        savedTheme2 = themeDao.insert(new Theme(new Name("방탈출 이름2"), "http://thumbnail_url", "방탈출을 할 수 있다."));
        requestDto1 = new ReservationRequestDto("유저1", LocalDate.of(2026, 5, 3), savedTime1.getId(),
                savedTheme1.getId());
        requestDto2 = new ReservationRequestDto("유저2", LocalDate.of(2026, 5, 3), savedTime2.getId(),
                savedTheme2.getId());
    }

    @Nested
    class FindAll {

        @Test
        @DisplayName("예약이 없으면 빈 목록을 반환한다")
        void returnsEmptyList() {
            assertThat(reservationService.findAll()).isEmpty();
        }

        @Test
        @DisplayName("전체 예약 목록을 반환한다")
        void returnsAllReservations() {
            List<Reservation> saved = new ArrayList<>();
            saved.add(reservationService.create(requestDto1));
            saved.add(reservationService.create(requestDto2));

            assertThat(reservationService.findAll()).isEqualTo(saved);
        }
    }

    @Nested
    class FindById {

        @Test
        @DisplayName("존재하는 id로 예약을 조회한다")
        void returnsReservationById() {
            Reservation saved = reservationService.create(requestDto1);

            assertThat(reservationService.findById(saved.getId())).isEqualTo(saved);
        }

        @Test
        @DisplayName("존재하지 않는 id를 조회하면 예외를 반환한다")
        void throwsWhenIdNotFound() {
            assertThatThrownBy(() -> reservationService.findById(-1L))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    class Create {

        @Test
        @DisplayName("유효한 요청으로 예약을 생성한다")
        void createsReservation() {
            Reservation saved = reservationService.create(requestDto1);

            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getName()).isEqualTo(requestDto1.name());
            assertThat(saved.getDate()).isEqualTo(requestDto1.date());
        }

        @Test
        @DisplayName("시간이 존재하지 않으면 예외를 반환한다")
        void throwsWhenTimeNotFound() {
            ReservationRequestDto dto = new ReservationRequestDto(
                    "유저1", LocalDate.of(2026, 5, 3), -1L, savedTheme1.getId());

            assertThatThrownBy(() -> reservationService.create(dto))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("테마가 존재하지 않으면 예외를 반환한다")
        void throwsWhenThemeNotFound() {
            ReservationRequestDto dto = new ReservationRequestDto(
                    "유저1", LocalDate.of(2026, 5, 3), savedTime1.getId(), -1L);

            assertThatThrownBy(() -> reservationService.create(dto))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("동일한 테마, 날짜, 시간으로 예약을 생성하면 예외를 반환한다")
        void throwsWhenDuplicateReservation() {
            reservationService.create(requestDto1);

            assertThatThrownBy(() -> reservationService.create(requestDto1))
                    .isInstanceOf(ConflictException.class);
        }
    }

    @Nested
    class Delete {

        @Test
        @DisplayName("예약을 삭제한다")
        void deletesReservation() {
            Reservation saved = reservationService.create(requestDto1);
            reservationService.delete(saved.getId());

            assertThat(reservationDao.existsById(saved.getId())).isFalse();
        }

        @Test
        @DisplayName("존재하지 않는 id를 삭제하면 예외를 반환한다")
        void throwsWhenDeletingNonExistentId() {
            assertThatThrownBy(() -> reservationService.delete(-1L))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}
