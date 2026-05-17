package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import roomescape.common.exception.BadRequestException;
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
        requestDto1 = new ReservationRequestDto("유저1", LocalDate.now().plusDays(1), savedTime1.getId(), savedTheme1.getId());
        requestDto2 = new ReservationRequestDto("유저2", LocalDate.now().plusDays(2), savedTime2.getId(), savedTheme2.getId());
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
                    "유저1", LocalDate.now().plusDays(1), -1L, savedTheme1.getId());

            assertThatThrownBy(() -> reservationService.create(dto))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("테마가 존재하지 않으면 예외를 반환한다")
        void throwsWhenThemeNotFound() {
            ReservationRequestDto dto = new ReservationRequestDto(
                    "유저1", LocalDate.now().plusDays(1), savedTime1.getId(), -1L);

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
    class FindActiveById {

        @Test
        @DisplayName("BOOKED 예약을 조회한다")
        void returnsActiveReservation() {
            Reservation saved = reservationService.create(requestDto1);

            assertThat(reservationService.findActiveById(saved.getId())).usingRecursiveComparison().isEqualTo(saved);
        }

        @Test
        @DisplayName("CANCELED 예약을 조회하면 예외를 반환한다")
        void throwsWhenCanceled() {
            Reservation saved = reservationDao.insert(
                    new Reservation("유저", LocalDate.now().plusDays(1), savedTime1, savedTheme1));
            reservationService.cancel(saved.getId());

            assertThatThrownBy(() -> reservationService.findActiveById(saved.getId()))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    class FindAllByName {

        @Test
        @DisplayName("이름으로 활성 예약 목록을 조회한다")
        void returnsActiveReservationsByName() {
            Reservation saved = reservationService.create(requestDto1);

            List<Reservation> result = reservationService.findAllByName(requestDto1.name());

            assertThat(result).usingRecursiveComparison().isEqualTo(List.of(saved));
        }

        @Test
        @DisplayName("취소된 예약은 반환하지 않는다")
        void excludesCanceledReservations() {
            Reservation saved = reservationDao.insert(
                    new Reservation("유저", LocalDate.now().plusDays(1), savedTime1, savedTheme1));
            reservationService.cancel(saved.getId());

            assertThat(reservationService.findAllByName("유저")).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 이름이면 빈 목록을 반환한다")
        void returnsEmptyWhenNameNotFound() {
            assertThat(reservationService.findAllByName("없는이름")).isEmpty();
        }
    }

    @Nested
    class UpdateByUser {

        @Test
        @DisplayName("본인 이름으로 예약을 수정한다")
        void updatesReservation() {
            Reservation saved = reservationService.create(requestDto1);
            LocalDate newDate = LocalDate.now().plusDays(3);
            ReservationPatchDto updateDto = new ReservationPatchDto(newDate, savedTime2.getId());

            Reservation updated = reservationService.updateByUser(saved.getId(), requestDto1.name(), updateDto);

            assertThat(updated.getDate()).isEqualTo(newDate);
            assertThat(updated.getTime()).isEqualTo(savedTime2);
        }

        @Test
        @DisplayName("다른 사람의 예약을 수정하면 예외를 반환한다")
        void throwsWhenNameMismatch() {
            Reservation saved = reservationService.create(requestDto1);
            ReservationPatchDto updateDto = new ReservationPatchDto(LocalDate.now().plusDays(3), savedTime2.getId());

            assertThatThrownBy(() -> reservationService.updateByUser(saved.getId(), "다른사람", updateDto))
                    .isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("존재하지 않는 id를 수정하면 예외를 반환한다")
        void throwsWhenIdNotFound() {
            ReservationPatchDto updateDto = new ReservationPatchDto(LocalDate.now().plusDays(3), savedTime1.getId());

            assertThatThrownBy(() -> reservationService.updateByUser(-1L, "유저1", updateDto))
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
            assertThat(canceled.getStatus()).isEqualTo(ReservationStatus.CANCELED);
        }

        @Test
        @DisplayName("존재하지 않는 id를 취소하면 예외를 반환한다")
        void throwsWhenIdNotFound() {
            assertThatThrownBy(() -> reservationService.cancel(-1L))
                    .isInstanceOf(NotFoundException.class);
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
}
