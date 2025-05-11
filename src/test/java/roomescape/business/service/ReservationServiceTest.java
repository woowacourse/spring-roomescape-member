package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Reservation;
import roomescape.business.domain.Theme;
import roomescape.exception.DuplicateException;
import roomescape.exception.InvalidDateAndTimeException;
import roomescape.exception.NotFoundException;
import roomescape.persistence.dao.JdbcPlayTimeDao;
import roomescape.persistence.dao.JdbcReservationDao;
import roomescape.persistence.dao.JdbcThemeDao;
import roomescape.persistence.dao.PlayTimeDao;
import roomescape.persistence.dao.ReservationDao;
import roomescape.persistence.dao.ThemeDao;
import roomescape.presentation.dto.ReservationRequest;
import roomescape.presentation.dto.ReservationResponse;

@JdbcTest
public class ReservationServiceTest {

    private static final LocalDate MAX_DATE_FIXTURE = LocalDate.of(9999, 12, 31);
    private static final LocalTime MAX_TIME_FIXTURE = LocalTime.of(23, 59);

    private ReservationService reservationService;
    private final ReservationDao reservationDao;
    private final PlayTimeDao playTimeDao;
    private final ThemeDao themeDao;

    private Long timeId;
    private Long themeId;

    @Autowired
    public ReservationServiceTest(final JdbcTemplate jdbcTemplate) {
        reservationDao = new JdbcReservationDao(jdbcTemplate);
        playTimeDao = new JdbcPlayTimeDao(jdbcTemplate);
        themeDao = new JdbcThemeDao(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(reservationDao, playTimeDao, themeDao);
        timeId = playTimeDao.insert(new PlayTime(MAX_TIME_FIXTURE))
                .getId();
        themeId = themeDao.insert(new Theme("테마", "소개", "썸네일"))
                .getId();
    }

    @Test
    @DisplayName("방탈출 예약 요청 객체로 방탈출 예약을 저장한다")
    void insert() {
        // given
        final String name = "name";
        final LocalDate date = MAX_DATE_FIXTURE;
        final ReservationRequest reservationRequest = new ReservationRequest(name, date, timeId, themeId);

        // when
        final ReservationResponse reservationResponse = reservationService.insert(reservationRequest);

        // then
        assertAll(
                () -> assertThat(reservationResponse.name()).isEqualTo(name),
                () -> assertThat(reservationResponse.date()).isEqualTo(date),
                () -> assertThat(reservationResponse.time()
                        .startAt()).isEqualTo(MAX_TIME_FIXTURE)
        );
    }

    @Test
    @DisplayName("존재하지 않는 방탈출 시간으로 예약하면 예외가 발생한다")
    void insertWhenNotExistReservationTime() {
        // given
        final String name = "name";
        final LocalDate date = MAX_DATE_FIXTURE;
        final Long notExistTimeId = 999L;
        final ReservationRequest reservationRequest = new ReservationRequest(name, date, notExistTimeId, themeId);

        // when & then
        assertThatThrownBy(() -> reservationService.insert(reservationRequest))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 방탈출 테마로 예약하면 예외가 발생한다")
    void insertWhenNotExistTheme() {
        // given
        final String name = "name";
        final LocalDate date = MAX_DATE_FIXTURE;
        final Long notExistThemeId = 999L;
        final ReservationRequest reservationRequest = new ReservationRequest(name, date, timeId, notExistThemeId);

        // when & then
        assertThatThrownBy(() -> reservationService.insert(reservationRequest))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("예약하려는 방탈출 예약과 동일한 날짜, 시간, 테마가 이미 존재한다면 예외가 발생한다")
    void insertWhenDuplicateDateAndTimeAndTheme() {
        // given
        final String name = "name";
        final LocalDate date = MAX_DATE_FIXTURE;
        final ReservationRequest reservationRequest = new ReservationRequest(name, date, timeId, themeId);
        reservationService.insert(reservationRequest);

        // when & then
        assertThatThrownBy(() -> reservationService.insert(reservationRequest))
                .isInstanceOf(DuplicateException.class);
    }

    @Test
    @DisplayName("예약 시간이 현재를 기준으로 과거라면 예외가 발생한다")
    void insertWhenDateAndTimeIsPast() {
        // TODO: 현재 시간을 비교하는 유틸을 인터페이스로 구현하여 테스트 완성도 높이기
        // given
        final String name = "name";
        final LocalDate pastDate = LocalDate.MIN;
        final ReservationRequest reservationRequest = new ReservationRequest(name, pastDate, timeId, themeId);

        // when & then
        assertThatThrownBy(() -> reservationService.insert(reservationRequest))
                .isInstanceOf(InvalidDateAndTimeException.class);
    }

    @Test
    @DisplayName("모든 방탈출 예약을 조회한다")
    void findAll() {
        // given
        final ReservationRequest reservationRequest1 = new ReservationRequest("kim",
                MAX_DATE_FIXTURE, timeId, themeId);
        final ReservationRequest reservationRequest2 = new ReservationRequest("lee",
                MAX_DATE_FIXTURE.minusDays(1), timeId, themeId);
        final ReservationResponse expected1 = reservationService.insert(reservationRequest1);
        final ReservationResponse expected2 = reservationService.insert(reservationRequest2);

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
    void deleteById() {
        // given
        final ReservationRequest reservationRequest = new ReservationRequest("name", MAX_DATE_FIXTURE, timeId, themeId);
        final ReservationResponse reservationResponse = reservationService.insert(reservationRequest);
        final Long id = reservationResponse.id();

        // when
        reservationService.deleteById(id);

        // then
        final Optional<Reservation>findReservation = reservationDao.findById(id);
        assertThat(findReservation).isEmpty();
    }

    @Test
    @DisplayName("id를 통해 예약을 삭제할 때 대상이 없다면 예외가 발생한다")
    void deleteByIdWhenNotExistReservation() {
        // given
        final Long notExistId = 999L;

        // when & then
        assertThatThrownBy(() -> reservationService.deleteById(notExistId))
                .isInstanceOf(NotFoundException.class);
    }
}
