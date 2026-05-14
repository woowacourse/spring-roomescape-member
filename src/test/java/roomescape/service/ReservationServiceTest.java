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
import roomescape.domain.ReservationStatus;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.domain.vo.Name;
import roomescape.dto.request.ReservationPatchDto;
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
        requestDto1 = new ReservationRequestDto("유저1", LocalDate.now().plusDays(1), savedTime1.getId(),
                savedTheme1.getId());
        requestDto2 = new ReservationRequestDto("유저2", LocalDate.now().plusDays(2), savedTime2.getId(),
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
    class FindActiveById {

        @Test
        @DisplayName("BOOKED 상태의 예약을 조회한다")
        void returnsActiveReservation() {
            Reservation saved = reservationService.create(requestDto1);

            assertThat(reservationService.findActiveById(saved.getId())).isEqualTo(saved);
        }

        @Test
        @DisplayName("CANCELED 상태의 예약을 조회하면 예외를 반환한다")
        void throwsWhenCanceled() {
            Reservation saved = reservationDao.insert(
                    new Reservation("유저", LocalDate.now().plusDays(1), savedTime1, savedTheme1));
            reservationService.cancel(saved.getId());

            assertThatThrownBy(() -> reservationService.findActiveById(saved.getId()))
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

        @Test
        @DisplayName("과거 날짜로 예약을 생성하면 예외를 반환한다")
        void throwsWhenPastDate() {
            ReservationRequestDto pastDto = new ReservationRequestDto(
                    "유저1", LocalDate.now().minusDays(1), savedTime1.getId(), savedTheme1.getId());

            assertThatThrownBy(() -> reservationService.create(pastDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class CreateByAdmin {

        @Test
        @DisplayName("과거 날짜로도 예약을 생성한다")
        void createsReservationWithPastDate() {
            ReservationRequestDto pastDto = new ReservationRequestDto(
                    "유저1", LocalDate.now().minusDays(1), savedTime1.getId(), savedTheme1.getId());

            Reservation saved = reservationService.createByAdmin(pastDto);

            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getDate()).isEqualTo(LocalDate.now().minusDays(1));
        }

        @Test
        @DisplayName("미래 날짜로도 예약을 생성한다")
        void createsReservationWithFutureDate() {
            Reservation saved = reservationService.createByAdmin(requestDto1);

            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getDate()).isEqualTo(requestDto1.date());
        }
    }

    @Nested
    class Update {

        @Test
        @DisplayName("예약의 날짜와 시간을 수정한다")
        void updatesReservation() {
            Reservation saved = reservationService.create(requestDto1);
            LocalDate newDate = LocalDate.of(2026, 5, 4);
            ReservationPatchDto updateDto = new ReservationPatchDto(newDate, savedTime2.getId());

            Reservation updated = reservationService.update(saved.getId(), updateDto);

            assertThat(updated.getDate()).isEqualTo(newDate);
            assertThat(updated.getTime()).isEqualTo(savedTime2);
        }

        @Test
        @DisplayName("존재하지 않는 id를 수정하면 예외를 반환한다")
        void throwsWhenIdNotFound() {
            ReservationPatchDto updateDto = new ReservationPatchDto(LocalDate.of(2026, 5, 4),
                    savedTime1.getId());

            assertThatThrownBy(() -> reservationService.update(-1L, updateDto))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("존재하지 않는 시간으로 수정하면 예외를 반환한다")
        void throwsWhenTimeNotFound() {
            Reservation saved = reservationService.create(requestDto1);
            ReservationPatchDto updateDto = new ReservationPatchDto(LocalDate.of(2026, 5, 4), -1L);

            assertThatThrownBy(() -> reservationService.update(saved.getId(), updateDto))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    class Cancel {

        @Test
        @DisplayName("미래 예약을 취소하면 상태가 CANCELED로 변경된다")
        void cancelsReservation() {
            Reservation saved = reservationDao.insert(
                    new Reservation("유저", LocalDate.now().plusDays(1), savedTime1, savedTheme1));

            reservationService.cancel(saved.getId());

            Reservation canceled = reservationDao.findById(saved.getId()).orElseThrow();
            assertThat(canceled.getReservationStatus()).isEqualTo(ReservationStatus.CANCELED);
        }

        @Test
        @DisplayName("존재하지 않는 id를 취소하면 예외를 반환한다")
        void throwsWhenIdNotFound() {
            assertThatThrownBy(() -> reservationService.cancel(-1L))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("취소 후 같은 슬롯을 재예약할 수 있다")
        void allowsRebookingAfterCancel() {
            Reservation saved = reservationDao.insert(
                    new Reservation("유저", LocalDate.now().plusDays(1), savedTime1, savedTheme1));
            reservationService.cancel(saved.getId());

            Reservation rebooked = reservationService.create(requestDto1);

            assertThat(rebooked.getId()).isNotNull();
        }

        @Test
        @DisplayName("이미 지난 예약을 취소하면 예외를 반환한다")
        void throwsWhenPastReservation() {
            Reservation saved = reservationDao.insert(
                    new Reservation("유저", LocalDate.now().minusDays(1), savedTime1, savedTheme1));

            assertThatThrownBy(() -> reservationService.cancel(saved.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
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
