package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.TimeDao;
import roomescape.dao.row.ReservationRow;
import roomescape.dao.row.ThemeRow;
import roomescape.dao.row.TimeRow;
import roomescape.dto.request.ReservationRequestDto;
import roomescape.dto.request.ReservationUpdateDto;
import roomescape.dto.response.ReservationResponseDto;
import roomescape.fixture.FakeReservationDao;
import roomescape.fixture.FakeThemeDao;
import roomescape.fixture.FakeTimeDao;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static roomescape.fixture.FakeDatabase.clearAll;

class ReservationServiceTest {

    private final static Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2026-05-10T03:00:00Z"),
            ZoneId.of("Asia/Seoul")
    );
    private static final LocalDate TODAY = LocalDate.of(2026, 5, 10);
    private static final Long NOT_EXISTS_ID = Long.MAX_VALUE;

    private ReservationService reservationService;
    private ReservationDao reservationDao;
    private TimeDao timeDao;
    private ThemeDao themeDao;

    @BeforeEach
    void setUp() {
        clearAll();
        reservationDao = new FakeReservationDao();
        timeDao = new FakeTimeDao();
        themeDao = new FakeThemeDao();
        reservationService = new ReservationService(
                reservationDao, timeDao, themeDao, FIXED_CLOCK
        );
    }

    private TimeRow givenTime(int hour) {
        return timeDao.create(new TimeRow(LocalTime.of(hour, 0)));
    }

    private ThemeRow givenTheme(String name) {
        return themeDao.create(new ThemeRow(name, "https://test.com", "테스트용 설명입니다"));
    }

    private ReservationRequestDto requestOf(String name, LocalDate date, Long timeId, Long themeId) {
        return new ReservationRequestDto(name, date, timeId, themeId);
    }

    private ReservationUpdateDto updateOf(String name, LocalDate date, Long timeId, Long themeId) {
        return new ReservationUpdateDto(name, date, timeId, themeId);
    }

    @Test
    void 조회할_id가_존재하지_않으면_예외처리한다() {
        assertThatThrownBy(() -> reservationService.findById(NOT_EXISTS_ID))
                .isInstanceOf(NotFoundException.class);
    }

    @Nested
    @DisplayName("예약을 생성할때: ")
    class Create {

        @Test
        void 정상_요청이면_예약이_생성된다() {
            TimeRow time = givenTime(14);
            ThemeRow theme = givenTheme("방탈출");
            ReservationRequestDto request = requestOf("유저1", TODAY, time.id(), theme.id());

            ReservationResponseDto response = reservationService.create(request);

            assertThat(response.id()).isNotNull();
            assertThat(response.name()).isEqualTo("유저1");
        }

        @Test
        void 시간이_존재하지_않는다면_예외_처리한다() {
            ThemeRow theme = givenTheme("방탈출");
            ReservationRequestDto request = requestOf("유저1", TODAY, null, theme.id());

            assertThatThrownBy(() -> reservationService.create(request))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 테마가_존재하지_않는다면_예외_처리한다() {
            TimeRow time = givenTime(14);
            ReservationRequestDto request = requestOf("유저1", TODAY, time.id(), null);

            assertThatThrownBy(() -> reservationService.create(request))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 테마_날짜_시간_동일한_예약이면_예외를_반환한다() {
            TimeRow time = givenTime(14);
            ThemeRow theme = givenTheme("방탈출");
            ReservationRequestDto request = requestOf("유저1", TODAY, time.id(), theme.id());
            reservationService.create(request);

            assertThatThrownBy(() -> reservationService.create(request))
                    .isInstanceOf(ConflictException.class);
        }

        @Test
        void 같은_날짜_같은_테마지만_시간이_다르면_예약_가능() {
            TimeRow time1 = givenTime(14);
            TimeRow time2 = givenTime(15);
            ThemeRow theme = givenTheme("방탈출");

            reservationService.create(requestOf("유저1", TODAY, time1.id(), theme.id()));

            assertThatCode(() ->
                    reservationService.create(requestOf("유저1", TODAY, time2.id(), theme.id()))
            ).doesNotThrowAnyException();
        }

        @Test
        void 같은_시간_같은_날짜지만_테마가_다르면_예약_가능() {
            TimeRow time = givenTime(14);
            ThemeRow theme1 = givenTheme("방탈출1");
            ThemeRow theme2 = givenTheme("방탈출2");
            reservationService.create(requestOf("유저1", TODAY, time.id(), theme1.id()));

            assertThatCode(() ->
                    reservationService.create(requestOf("유저1", TODAY, time.id(), theme2.id()))
            ).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("예약을 삭제할 때: ")
    class DeleteById {

        @Test
        void 정상_요청이면_삭제된다() {
            TimeRow time = givenTime(14);
            ThemeRow theme = givenTheme("방탈출");
            ReservationRequestDto request = requestOf("유저1", TODAY, time.id(), theme.id());
            ReservationResponseDto created = reservationService.create(request);

            reservationService.delete(created.id());

            assertThatThrownBy(() -> reservationService.findById(created.id()))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 이미_지난_예약이면_삭제할_수_없습니다() {
            TimeRow time = givenTime(14);
            ThemeRow theme = givenTheme("방탈출");
            LocalDate pastDate = TODAY.minusDays(1);
            ReservationRequestDto request = requestOf("유저1", pastDate, time.id(), theme.id());

            ReservationRow saved = reservationDao.create(
                    new ReservationRow(null, "유저1", pastDate, time, theme)
            );

            assertThatThrownBy(() -> reservationService.delete(saved.id()))
                    .isInstanceOf(ConflictException.class);
        }

        @Test
        void 삭제하려는_id가_존재하지_않으면_예외() {
            assertThatThrownBy(() -> reservationService.delete(NOT_EXISTS_ID))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("예약을 삭제할 때: ")
    class DeleteByIdAndName {

        @Test
        void 정상_요청이면_삭제된다() {
            TimeRow time = givenTime(14);
            ThemeRow theme = givenTheme("방탈출");
            ReservationRequestDto request = requestOf("유저1", TODAY, time.id(), theme.id());
            ReservationResponseDto created = reservationService.create(request);

            reservationService.delete(created.id(), "유저1");

            assertThatThrownBy(() -> reservationService.findById(created.id()))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 이미_지난_예약이면_삭제할_수_없습니다() {
            TimeRow time = givenTime(14);
            ThemeRow theme = givenTheme("방탈출");
            LocalDate pastDate = TODAY.minusDays(1);
            ReservationRequestDto request = requestOf("유저1", pastDate, time.id(), theme.id());

            ReservationRow saved = reservationDao.create(
                    new ReservationRow(null, "유저1", pastDate, time, theme)
            );

            assertThatThrownBy(() -> reservationService.delete(saved.id(), "유저1"))
                    .isInstanceOf(ConflictException.class);
        }

        @Test
        void 삭제하려는_id가_존재하지_않으면_예외() {
            assertThatThrownBy(() -> reservationService.delete(NOT_EXISTS_ID, "유저1"))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 삭제하려는_name이_존재하지_않으면_예외() {
            TimeRow time = givenTime(14);
            ThemeRow theme = givenTheme("방탈출");
            ReservationRequestDto request = requestOf("유저1", TODAY, time.id(), theme.id());
            ReservationResponseDto created = reservationService.create(request);

            assertThatThrownBy(() -> reservationService.delete(created.id(), "없는유저"))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("예약을 변경할 때: ")
    class Updated {

        @Test
        void 시간이_존재하지_않는다면_예외_처리한다() {
            ThemeRow theme = givenTheme("방탈출");
            TimeRow time = givenTime(14);
            ReservationRequestDto request = requestOf("유저1", TODAY, time.id(), theme.id());
            ReservationResponseDto created = reservationService.create(request);
            assertThatThrownBy(() -> reservationService.update(created.id(), updateOf(created.name(), created.date(), created.timeDto().id(), NOT_EXISTS_ID)))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 테마가_존재하지_않는다면_예외_처리한다() {
            ThemeRow theme = givenTheme("방탈출");
            TimeRow time = givenTime(14);
            ReservationRequestDto request = requestOf("유저1", TODAY, time.id(), theme.id());
            ReservationResponseDto created = reservationService.create(request);

            assertThatThrownBy(() -> reservationService.update(created.id(), updateOf(created.name(), created.date(), NOT_EXISTS_ID, created.themeResponseDto().id())))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 예약이_존재하지_않는다면_예외_처리한다() {
            ThemeRow theme = givenTheme("방탈출");
            TimeRow time = givenTime(14);
            ReservationRequestDto request = requestOf("유저1", TODAY, time.id(), theme.id());
            ReservationResponseDto created = reservationService.create(request);

            assertThatThrownBy(() -> reservationService.update(NOT_EXISTS_ID, updateOf(created.name(), created.date(), created.timeDto().id(), created.themeResponseDto().id())))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 다른_예약과_슬롯이_겹치면_예외를_반환한다() {
            TimeRow time = givenTime(14);
            ThemeRow theme = givenTheme("방탈출");
            reservationService.create(requestOf("다른사람", TODAY.plusDays(1), time.id(), theme.id()));

            ReservationResponseDto mine = reservationService.create(
                    requestOf("나", TODAY, time.id(), theme.id())
            );

            assertThatThrownBy(() -> reservationService.update(
                    mine.id(),
                    updateOf("나", TODAY.plusDays(1), time.id(), theme.id())
            )).isInstanceOf(ConflictException.class);
        }
    }
}
