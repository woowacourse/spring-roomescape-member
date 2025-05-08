package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Theme;
import roomescape.exception.DuplicateException;
import roomescape.exception.InvalidReservationDateException;
import roomescape.exception.NotFoundException;
import roomescape.fake.FakePlayTimeDao;
import roomescape.fake.FakeReservationDao;
import roomescape.fake.FakeThemeDao;
import roomescape.persistence.dao.PlayTimeDao;
import roomescape.persistence.dao.ThemeDao;
import roomescape.presentation.dto.ReservationRequest;
import roomescape.presentation.dto.ReservationResponse;

public class ReservationServiceTest {

    private static final LocalDate FORMATTED_MAX_LOCAL_DATE = LocalDate.of(9999, 12, 31);
    private static final LocalTime FORMATTED_MAX_LOCAL_TIME = LocalTime.of(23, 59);

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        final PlayTimeDao fakePlayTimeDao = new FakePlayTimeDao();
        fakePlayTimeDao.insert(
                new PlayTime(1L, FORMATTED_MAX_LOCAL_TIME)
        );
        final ThemeDao fakeThemeDao = new FakeThemeDao();
        fakeThemeDao.insert(
                new Theme(1L, "테마", "소개", "썸네일")
        );
        reservationService = new ReservationService(
                new PlayTimeService(fakePlayTimeDao),
                new ThemeService(fakeThemeDao),
                new FakeReservationDao()
        );
    }

    @Test
    @DisplayName("방탈출 예약 요청 객체로 방탈출 예약을 저장한다")
    void create() {
        // given
        final String name = "name";
        final LocalDate date = FORMATTED_MAX_LOCAL_DATE;
        final Long timeId = 1L;
        final Long themeId = 1L;
        final ReservationRequest reservationRequest = new ReservationRequest(name, date, timeId, themeId);

        // when
        final ReservationResponse reservationResponse = reservationService.create(reservationRequest);

        // then
        assertAll(
                () -> assertThat(reservationResponse.name()).isEqualTo(name),
                () -> assertThat(reservationResponse.date()).isEqualTo(date),
                () -> assertThat(reservationResponse.time().startAt()).isEqualTo(FORMATTED_MAX_LOCAL_TIME)
        );
    }

    @Test
    @DisplayName("존재하지 않는 방탈출 시간으로 예약하면 예외가 발생한다")
    void createWhenNotExistReservationTime() {
        // given
        final String name = "name";
        final LocalDate date = FORMATTED_MAX_LOCAL_DATE;
        final Long notExistTimeId = 999L;
        final Long themeId = 1L;
        final ReservationRequest reservationRequest = new ReservationRequest(name, date, notExistTimeId, themeId);

        // when & then
        assertThatThrownBy(() -> reservationService.create(reservationRequest))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 방탈출 테마로 예약하면 예외가 발생한다")
    void createWhenNotExistTheme() {
        // given
        final String name = "name";
        final LocalDate date = FORMATTED_MAX_LOCAL_DATE;
        final Long timeId = 999L;
        final Long notExistThemeId = 1L;
        final ReservationRequest reservationRequest = new ReservationRequest(name, date, timeId, notExistThemeId);

        // when & then
        assertThatThrownBy(() -> reservationService.create(reservationRequest))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("예약하려는 방탈출 예약과 동일한 날짜, 시간, 테마가 이미 존재한다면 예외가 발생한다")
    void createWhenDuplicateDateAndTimeAndTheme() {
        // given
        final String name = "name";
        final LocalDate date = FORMATTED_MAX_LOCAL_DATE;
        final Long timeId = 1L;
        final Long themeId = 1L;
        final ReservationRequest reservationRequest = new ReservationRequest(name, date, timeId, themeId);
        reservationService.create(reservationRequest);

        // when & then
        assertThatThrownBy(() -> reservationService.create(reservationRequest))
                .isInstanceOf(DuplicateException.class);
    }

    @Test
    @DisplayName("예약 시간이 현재를 기준으로 과거라면 예외가 발생한다")
    void createWhenDateAndTimeIsPast() {
        // TODO: 현재 시간을 비교하는 유틸을 인터페이스로 구현하여 테스트 완성도 높이기
        // given
        final String name = "name";
        final LocalDate pastDate = LocalDate.MIN;
        final Long timeId = 1L;
        final Long themeId = 1L;
        final ReservationRequest reservationRequest = new ReservationRequest(name, pastDate, timeId, themeId);

        // when & then
        assertThatThrownBy(() -> reservationService.create(reservationRequest))
                .isInstanceOf(InvalidReservationDateException.class);
    }

    @Test
    @DisplayName("모든 방탈출 예약을 조회한다")
    void findAll() {
        // given
        final ReservationRequest reservationRequest1 = new ReservationRequest(
                "kim", FORMATTED_MAX_LOCAL_DATE, 1L, 1L
        );
        final ReservationRequest reservationRequest2 = new ReservationRequest(
                "lee", FORMATTED_MAX_LOCAL_DATE.minusDays(1), 1L, 1L
        );
        final ReservationResponse expected1 = reservationService.create(reservationRequest1);
        final ReservationResponse expected2 = reservationService.create(reservationRequest2);

        // when
        final List<ReservationResponse> reservationResponses = reservationService.findAll();

        // then
        assertAll(
                () -> assertThat(reservationResponses).hasSize(2),
                () -> assertThat(reservationResponses).contains(expected1, expected2)
        );
    }

    @Test
    @DisplayName("id를 통해 방탈출 예약을 삭제한다")
    void remove() {
        // given
        final ReservationRequest reservationRequest = new ReservationRequest(
                "name", FORMATTED_MAX_LOCAL_DATE, 1L, 1L
        );

        final ReservationResponse reservationResponse = reservationService.create(reservationRequest);

        // when
        assertAll(
                () -> assertThatCode(() -> reservationService.remove(1L)),
                () -> assertThat(reservationService.findAll()).isEmpty()
        );
    }

    @Test
    @DisplayName("id를 통해 예약을 삭제할 때 대상이 없다면 예외가 발생한다")
    void removeWhenNotExistReservation() {
        // given
        final Long notExistId = 999L;

        // when & then
        assertThatThrownBy(() -> reservationService.remove(notExistId))
                .isInstanceOf(NotFoundException.class);
    }
}
