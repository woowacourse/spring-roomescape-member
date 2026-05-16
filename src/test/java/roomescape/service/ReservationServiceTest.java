package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.domain.reservation.ReservationWithTime;
import roomescape.domain.reservation.ReservationWithTimeAndTheme;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTime.ReservationTimeCommand;
import roomescape.domain.reservationTime.ReservationTimeCondition;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;
import roomescape.domain.theme.PopularThemeCondition;
import roomescape.domain.theme.ReservationThemeCommand;
import roomescape.domain.theme.ReservationThemeWithCount;
import roomescape.domain.theme.Theme;
import roomescape.exception.ConflictException;
import roomescape.exception.InvalidRequestValueException;
import roomescape.exception.NotFoundResourceException;
import roomescape.exception.UnauthorizedException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;
import roomescape.repository.theme.ThemeRepository;

public class ReservationServiceTest {
    private ReservationRepository createReservationRepository(ReservationWithTimeAndTheme reservation, boolean isExist, int updatedRow) {
        return new ReservationRepository() {
            @Override public Optional<ReservationWithTimeAndTheme> getReservationWithTimeAndTheme(long id) { return Optional.ofNullable(reservation); }
            @Override public Optional<ReservationWithTime> getReservationWithTime(long id) {
                if (reservation == null) return Optional.empty();
                return Optional.of(new ReservationWithTime(reservation.id(), reservation.name(), reservation.date(), reservation.time(), reservation.reservationTheme().id()));
            }
            @Override public List<ReservationWithTimeAndTheme> getAllReservation(String name) { return List.of(); }
            @Override public long addReservation(ReservationCommand reservationCommand) {
                return reservation != null ? reservation.id() : 1L;
            }
            @Override public void deleteReservation(long id) {}
            @Override public int updateAll(long id, ReservationCommand cmd) { return updatedRow; }
            @Override public boolean existsByTimeId(long timeId) { return false; }
            @Override public boolean existsByThemeId(long themeId) { return false; }
            @Override public boolean existsByTimeIdAndThemeIdAndDate(long tid, long thid, LocalDate d) { return isExist; }
        };
    }

    private ReservationTimeRepository createReservationTimeRepository(ReservationTime reservationTime) {
        return new ReservationTimeRepository() {
            @Override public ReservationTime addReservationTime(ReservationTimeCommand cmd) { return null; }
            @Override public Optional<ReservationTime> getReservationTime(long id) { return Optional.ofNullable(reservationTime); }
            @Override public List<ReservationTime> getAllReservationTime() { return List.of(); }
            @Override public void deleteReservationTime(long id) {}
            @Override public List<ReservationTimeWithAvailable> getAvailableReservationTimeByDateAndTheme(ReservationTimeCondition cond) { return List.of(); }
            @Override public boolean isExistsById(long id) { return true; }
        };
    }

    private ThemeRepository createThemeRepository(Theme theme) {
        return new ThemeRepository() {
            @Override public Theme addTheme(ReservationThemeCommand cmd) { return null; }
            @Override public List<Theme> getAllTheme() { return List.of(); }
            @Override public Optional<Theme> getTheme(long id) { return Optional.ofNullable(theme); }
            @Override public void deleteTheme(long id) {}
            @Override public List<ReservationThemeWithCount> getPopularTheme(PopularThemeCondition cond) { return List.of(); }
            @Override public boolean isExistsById(long id) { return true; }
        };
    }

    @Test
    @DisplayName("예약 생성 시 중복된 예약이 있으면 ConflictException 발생")
    void addReservationFailByDuplicateTest() {
        ReservationTime reservationTime = new ReservationTime(1, LocalTime.of(10, 0));
        Theme theme = new Theme(1, "테마", "설명", "url");

        ReservationService reservationService = new ReservationService(
                createReservationRepository(null, true, 0),
                createReservationTimeRepository(reservationTime),
                createThemeRepository(theme)
        );

        assertThatThrownBy(() -> reservationService.addReservation(new ReservationCommand("브라운", LocalDate.now(), 1, 1)))
                .isExactlyInstanceOf(ConflictException.class)
                .hasMessage("해당 날짜, 시간, 테마의 예약이 존재하여 예약할 수 없습니다.");
    }

    @Test
    @DisplayName("예약 삭제 시 본인의 예약이 아니면 UnauthorizedException 발생")
    void deleteReservationFailByUnauthorizedTest() {
        ReservationWithTimeAndTheme reservation = new ReservationWithTimeAndTheme(
                1, "브라운", LocalDate.now().plusDays(1),
                new ReservationTime(1, LocalTime.parse("10:00")),
                new Theme(1, "테스트", "설명", "url")
        );
        ReservationService reservationService = new ReservationService(
                createReservationRepository(reservation, false, 0), null, null
        );

        assertThatThrownBy(() -> reservationService.deleteReservation(1, "테스트"))
                .isExactlyInstanceOf(UnauthorizedException.class)
                .hasMessage("해당 예약을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("예약 삭제 시 이미 지난 날짜의 예약이면 InvalidRequestValueException 발생")
    void deleteReservationFailByPastDateTest() {
        ReservationWithTimeAndTheme reservation = new ReservationWithTimeAndTheme(
                1, "브라운", LocalDate.now().minusDays(1),
                new ReservationTime(1, LocalTime.parse("10:00")),
                new Theme(1, "테스트", "설명", "url")
        );
        ReservationService reservationService = new ReservationService(
                createReservationRepository(reservation, false, 0), null, null
        );

        assertThatThrownBy(() -> reservationService.deleteReservation(1, "브라운"))
                .isExactlyInstanceOf(InvalidRequestValueException.class)
                .hasMessage("이미 지난 예약은 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("예약 수정 시 기존 정보와 동일하면 InvalidRequestValueException 발생")
    void updateReservationFailBySameValueTest() {
        LocalDate date = LocalDate.now().plusDays(6);
        ReservationWithTimeAndTheme reservation = new ReservationWithTimeAndTheme(
                1, "브라운", date,
                new ReservationTime(1, LocalTime.parse("10:00")),
                new Theme(1, "테스트", "설명", "url")
        );
        ReservationService reservationService = new ReservationService(
                createReservationRepository(reservation, false, 0),
                createReservationTimeRepository(new ReservationTime(1, LocalTime.parse("10:00"))),
                createThemeRepository(new Theme(1, "테스트", "설명", "url"))
        );

        ReservationCommand sameCommand = new ReservationCommand("브라운", date, 1, 1);

        assertThatThrownBy(() -> reservationService.updateReservation(1, "브라운", sameCommand))
                .isExactlyInstanceOf(InvalidRequestValueException.class)
                .hasMessage("기존 정보와 동일하여 수정할 내용이 없습니다.");
    }

    @Test
    @DisplayName("예약 수정 시 수정하려는 시간에 이미 다른 예약이 존재하면 ConflictException 발생")
    void updateReservationFailByDuplicateTest() {
        LocalDate date = LocalDate.now().plusDays(6);
        ReservationWithTimeAndTheme reservation = new ReservationWithTimeAndTheme(
                1, "브라운", date,
                new ReservationTime(1, LocalTime.parse("10:00")),
                new Theme(1, "테스트", "설명", "url")
        );

        ReservationService reservationService = new ReservationService(
                createReservationRepository(reservation, true, 0),
                createReservationTimeRepository(null),
                createThemeRepository(null)
        );

        ReservationCommand newCommand = new ReservationCommand("브라운", date, 2, 2);

        assertThatThrownBy(() -> reservationService.updateReservation(1, "브라운", newCommand))
                .isExactlyInstanceOf(ConflictException.class)
                .hasMessage("해당 날짜, 시간, 테마의 예약이 존재하여 예약할 수 없습니다.");
    }

    @Test
    @DisplayName("예약 수정 시 이미 지난 날짜의 예약이면 InvalidRequestValueException 발생")
    void updateReservationFailByPastDateTest() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        ReservationWithTimeAndTheme reservation = new ReservationWithTimeAndTheme(
                1, "브라운", pastDate,
                new ReservationTime(1, LocalTime.parse("10:00")),
                new Theme(1, "테스트", "설명", "url")
        );

        ReservationService reservationService = new ReservationService(
                createReservationRepository(reservation, false, 0),
                createReservationTimeRepository(new ReservationTime(1, LocalTime.parse("10:00"))),
                createThemeRepository(new Theme(1, "테스트", "설명", "url"))
        );

        ReservationCommand updateCommand = new ReservationCommand("브라운", LocalDate.now().plusDays(1), 1, 1);

        assertThatThrownBy(() -> reservationService.updateReservation(1, "브라운", updateCommand))
                .isExactlyInstanceOf(InvalidRequestValueException.class)
                .hasMessage("이미 지난 예약은 수정할 수 없습니다.");
    }

    @Test
    @DisplayName("예약 수정 시 본인의 예약이 아니면 UnauthorizedException 발생")
    void updateReservationFailByUnauthorizedTest() {
        ReservationWithTimeAndTheme reservation = new ReservationWithTimeAndTheme(
                1, "브라운", LocalDate.now().plusDays(6),
                new ReservationTime(1, LocalTime.parse("10:00")),
                new Theme(1, "테스트", "설명", "url")
        );
        ReservationService reservationService = new ReservationService(
                createReservationRepository(reservation, false, 0), null, null
        );

        ReservationCommand updateCommand = new ReservationCommand("브라운", LocalDate.now().plusDays(2), 2, 2);

        assertThatThrownBy(() -> reservationService.updateReservation(1, "테스트", updateCommand))
                .isExactlyInstanceOf(UnauthorizedException.class)
                .hasMessage("해당 예약을 수정할 권한이 없습니다.");
    }

    @Test
    @DisplayName("예약 조회 시 존재하지 않는 ID면 NotFoundResourceException 발생")
    void getReservationFailByNotFoundTest() {
        ReservationService reservationService = new ReservationService(
                createReservationRepository(null, false, 0), null, null
        );

        assertThatThrownBy(() -> reservationService.deleteReservation(999, "테스트"))
                .isExactlyInstanceOf(NotFoundResourceException.class)
                .hasMessage("존재하지 않는 예약 id입니다.");
    }
}