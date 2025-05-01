package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.PlayTimeNotFoundException;
import roomescape.exception.ThemeNotFoundException;
import roomescape.fake.FakePlayTimeDao;
import roomescape.fake.FakeReservationDao;
import roomescape.fake.FakeThemeDao;
import roomescape.persistence.entity.PlayTimeEntity;
import roomescape.persistence.entity.ThemeEntity;
import roomescape.presentation.dto.PlayTimeResponse;
import roomescape.presentation.dto.ReservationRequest;
import roomescape.presentation.dto.ReservationResponse;
import roomescape.presentation.dto.ThemeResponse;

public class ReservationServiceTest {

    private static final LocalDate FORMATTED_MAX_LOCAL_DATE = LocalDate.of(9999, 12, 31);
    private static final LocalTime FORMATTED_MAX_LOCAL_TIME = LocalTime.of(23, 59);

    private ReservationService reservationService;

    private final FakePlayTimeDao timeDaoFixture = new FakePlayTimeDao(new ArrayList<>(List.of(
            new PlayTimeEntity(1L, FORMATTED_MAX_LOCAL_TIME.toString())
    )));
    private final FakeThemeDao themeDaoFixture = new FakeThemeDao(new ArrayList<>(List.of(
            new ThemeEntity(1L, "테마", "소개", "썸네일")
    )));
    private final PlayTimeService playTimeServiceFixture = new PlayTimeService(timeDaoFixture);
    private final ThemeService themeServiceFixture = new ThemeService(themeDaoFixture);

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(
                playTimeServiceFixture,
                themeServiceFixture,
                new FakeReservationDao(timeDaoFixture.getTimes())
        );
    }

    @DisplayName("방탈출 예약을 저장한다.")
    @Test
    void create() {
        // given
        final ReservationRequest reservationRequest = new ReservationRequest(
                "hotteok", FORMATTED_MAX_LOCAL_DATE, 1L, 1L
        );
        final ReservationResponse expected = new ReservationResponse(
                1L,
                "hotteok",
                FORMATTED_MAX_LOCAL_DATE,
                new PlayTimeResponse(1L, FORMATTED_MAX_LOCAL_TIME),
                new ThemeResponse(1L, "테마", "소개", "썸네일")
        );

        // when & then
        assertThat(reservationService.create(reservationRequest))
                .isEqualTo(expected);
    }

    @DisplayName("저장하려는 예약에 해당하는 방탈출 시간이 없다면 예외가 발생한다.")
    @Test
    void createOrThrowIfTimeIdNotExists() {
        // given
        final ReservationRequest reservationRequest = new ReservationRequest(
                "hotteok", FORMATTED_MAX_LOCAL_DATE, 2L, 1L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.create(reservationRequest))
                .isInstanceOf(PlayTimeNotFoundException.class);
    }

    @DisplayName("저장하려는 예약에 해당하는 방탈출 테마가 없다면 예외가 발생한다.")
    @Test
    void createOrThrowIfThemeIdNotExists() {
        // given
        final ReservationRequest reservationRequest = new ReservationRequest(
                "hotteok", FORMATTED_MAX_LOCAL_DATE, 1L, 2L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.create(reservationRequest))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @DisplayName("저장하려는 방탈출 예약에 날짜/시간/테마가 이미 존재한다면 예외가 발생한다.")
    @Test
    void createOrThrowIfDateAndTimeAndThemeDuplicate() {
        // given
        final ReservationRequest reservationRequest = new ReservationRequest(
                "hotteok", FORMATTED_MAX_LOCAL_DATE, 1L, 1L
        );
        reservationService.create(reservationRequest);

        // when & then
        assertThatThrownBy(() -> reservationService.create(reservationRequest))
                .isInstanceOf(DuplicateReservationException.class);
    }

    @DisplayName("저장하려는 예약이 현재 날짜/시간보다 과거라면 예외가 발생한다.")
    @Test
    void createOrThrowIfFuture() {
        // given
        final ReservationRequest reservationRequest = new ReservationRequest(
                "hotteok", LocalDate.of(0, 1, 1), 1L, 1L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.create(reservationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 날짜 및 시간이 현재보다 과거일 수 없습니다.");
    }

    @DisplayName("모든 방탈출 예약을 조회한다.")
    @Test
    void findAll() {
        // given
        reservationService.create(new ReservationRequest(
                "hotteok", FORMATTED_MAX_LOCAL_DATE, 1L, 1L
        ));
        reservationService.create(new ReservationRequest(
                "saba", FORMATTED_MAX_LOCAL_DATE.minusDays(1), 1L, 1L
        ));

        // when & then
        assertThat(reservationService.findAll())
                .containsExactly(
                        new ReservationResponse(1L, "hotteok", FORMATTED_MAX_LOCAL_DATE,
                                new PlayTimeResponse(1L, FORMATTED_MAX_LOCAL_TIME),
                                new ThemeResponse(1L, "테마", "소개", "썸네일")),
                        new ReservationResponse(2L, "saba", FORMATTED_MAX_LOCAL_DATE.minusDays(1),
                                new PlayTimeResponse(1L, FORMATTED_MAX_LOCAL_TIME),
                                new ThemeResponse(1L, "테마", "소개", "썸네일"))
                );
    }

    @DisplayName("방탈출 예약을 삭제한다.")
    @Test
    void remove() {
        // given
        reservationService.create(new ReservationRequest(
                "hotteok", FORMATTED_MAX_LOCAL_DATE, 1L, 1L
        ));

        // when
        reservationService.remove(1L);

        // then
        assertThat(reservationService.findAll()).isEmpty();
    }

    @DisplayName("석재하려는 예약이 존재하지 않으면 예외가 발생한다.")
    @Test
    void removeOrThrowIfIdNotExists() {
        // given & when & then
        assertThatThrownBy(() -> reservationService.remove(1L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
