package roomescape.domain.time.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.global.exception.ConflictException;
import roomescape.domain.global.exception.ErrorCode;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.FakeReservationRepository;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.FakeThemeRepository;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.dto.request.TimeCreateRequestDto;
import roomescape.domain.time.dto.response.TimeResponseDto;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.FakeTimeRepository;
import roomescape.domain.time.repository.TimeRepository;
import roomescape.global.ExceptionAssertions;

class TimeServiceTest {

    private final Clock fixedClock;
    private final TimeService timeService;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    TimeServiceTest() {
        this.fixedClock = Clock.fixed(
            Instant.parse("2026-01-01T10:00:00Z"),
            ZoneId.of("UTC")
        );
        this.themeRepository = new FakeThemeRepository();
        this.timeRepository = new FakeTimeRepository();
        this.reservationRepository = new FakeReservationRepository();
        this.timeService = new TimeService(reservationRepository, timeRepository);
    }

    @Nested
    @DisplayName("getTimes 테스트")
    class GetTimesTest {

        @DisplayName("모든 시간을 조회한다.")
        @Test
        void 성공() {

            // given
            LocalTime startAt = LocalTime.of(10, 0);

            timeRepository.save(Time.create(startAt));
            timeRepository.save(Time.create(startAt.plusHours(1)));
            timeRepository.save(Time.create(startAt.plusHours(2)));

            // when
            List<TimeResponseDto> actual = timeService.getTimes();

            // then
            assertAll(
                () -> assertEquals(3, actual.size()),
                () -> assertEquals(new TimeResponseDto(1L, startAt), actual.get(0)),
                () -> assertEquals(new TimeResponseDto(2L, startAt.plusHours(1)), actual.get(1)),
                () -> assertEquals(new TimeResponseDto(3L, startAt.plusHours(2)), actual.get(2))
            );
        }
    }

    @Nested
    @DisplayName("이용 가능한 시간 조회")
    class GetAvailableTimes {

        @Test
        @DisplayName("주어진 날짜와 테마 아이디를 가진 예약 리스트에 없는 시간을 조회한다.")
        void 성공() {

            // given
            Time time1 = timeRepository.save(Time.create(LocalTime.of(18, 0)));
            Time time2 = timeRepository.save(Time.create(LocalTime.of(19, 0)));
            Time time3 = timeRepository.save(Time.create(LocalTime.of(20, 0)));
            Time time4 = timeRepository.save(Time.create(LocalTime.of(21, 0)));

            Theme theme1 = themeRepository.save(Theme.create(
                "셜록: 런던의 그림자",
                "안개 낀 런던, 의문의 살인 사건 현장에 남겨진 마지막 단서를 찾아 범인을 검거하세요.",
                "https://images.unsplash.com/photo-1585076641399-5c0f74268a2d?auto=format&fit=crop&q=80&w=800"
            ));
            Theme theme2 = themeRepository.save(Theme.create(
                "폐쇄병동의 비밀",
                "버려진 정신병원, 자정마다 들려오는 비명소리... 당신은 이곳에서 제정신으로 나갈 수 있을까요?",
                "https://images.unsplash.com/photo-1509248961158-e54f6934749c?auto=format&fit=crop&q=80&w=800"
            ));
            Theme theme3 = themeRepository.save(Theme.create(
                "파라오의 저주",
                "모래 폭풍 속에 발견된 고대 피라미드. 함정을 피하고 숨겨진 황금 마스크를 찾아 탈출해야 합니다.",
                "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&q=80&w=800"
            ));
            themeRepository.save(Theme.create(
                "이상한 나라의 초대장",
                "토끼굴로 떨어진 당신, 모든 것이 거꾸로 된 세상에서 원래 세계로 돌아가는 길을 찾으세요.",
                "https://images.unsplash.com/photo-1582139329536-e7284fece509?auto=format&fit=crop&q=80&w=800"
            ));

            reservationRepository.save(
                Reservation.create("브라이언", LocalDate.of(2026, 5, 10), time1, theme1, fixedClock));
            reservationRepository.save(
                Reservation.create("제이슨", LocalDate.of(2026, 5, 10), time2, theme2, fixedClock));
            reservationRepository.save(
                Reservation.create("앨리스", LocalDate.of(2026, 5, 11), time3, theme3, fixedClock));
            reservationRepository.save(
                Reservation.create("데이브", LocalDate.of(2026, 5, 11), time4, theme1, fixedClock));

            LocalDate date = LocalDate.of(2026, 5, 10);
            Long themeId = 1L;
            List<TimeResponseDto> expected = List.of(TimeResponseDto.from(time2),
                TimeResponseDto.from(time3), TimeResponseDto.from(time4));

            // when
            List<TimeResponseDto> actual = timeService.getAvailableTimes(date, themeId);

            // then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("saveTime 테스트")
    class SaveTimeTest {

        @Test
        @DisplayName("시간을 저장하고, 저장된 시간을 반환한다.")
        void 성공() {

            // given
            TimeCreateRequestDto request = new TimeCreateRequestDto(LocalTime.of(15, 30));

            // when
            TimeResponseDto actual = timeService.saveTime(request);

            // then
            assertAll(
                () -> assertEquals(1L, actual.id()),
                () -> assertEquals(LocalTime.of(15, 30), actual.startAt()),
                () -> assertEquals(List.of(actual), timeService.getTimes())
            );
        }

        @Test
        @DisplayName("저장하려는 시간이 이미 존재하는 경우 예외가 발생한다.")
        void 실패() {
            timeRepository.save(Time.create(LocalTime.of(15, 30)));
            TimeCreateRequestDto request = new TimeCreateRequestDto(LocalTime.of(15, 30));

            ExceptionAssertions.assertErrorCode(
                () -> timeService.saveTime(request),
                ConflictException.class,
                ErrorCode.TIME_DUPLICATE
            );
        }
    }

    @Nested
    @DisplayName("deleteTimeById 테스트")
    class DeleteTimeByIdTest {

        @Test
        @DisplayName("주어진 아이디를 가지는 시간을 삭제한다.")
        void 성공() {

            // given
            Time savedTime = timeRepository.save(Time.create(LocalTime.of(12, 0)));
            timeRepository.save(Time.create(LocalTime.of(13, 0)));

            // when
            timeService.deleteTimeById(savedTime.getId());

            // then
            List<TimeResponseDto> actual = timeService.getTimes();
            assertAll(
                () -> assertEquals(1, actual.size()),
                () -> assertEquals(LocalTime.of(13, 0), actual.getFirst().startAt())
            );
        }

        @Test
        @DisplayName("주어진 아이디를 참조하는 예약이 존재하는 경우 예외가 발생한다.")
        void 실패() {
            Time time = timeRepository.save(Time.create(LocalTime.of(12, 0)));
            Theme theme = themeRepository.save(Theme.create("테마명", "테마 설명", "썸네일 Url"));
            reservationRepository.save(
                Reservation.create("브라운", LocalDate.of(2026, 5, 12), time, theme, fixedClock));

            ExceptionAssertions.assertErrorCode(
                () -> timeService.deleteTimeById(time.getId()),
                ConflictException.class,
                ErrorCode.TIME_REFERENCED_BY_RESERVATION
            );
        }
    }
}
