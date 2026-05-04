package roomescape.domain.time.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.FakeReservationRepository;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.FakeThemeRepository;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.dto.request.TimeCreateRequestDTO;
import roomescape.domain.time.dto.response.TimeResponseDTO;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.FakeTimeRepository;
import roomescape.domain.time.repository.TimeRepository;

class TimeServiceTest {

    private final TimeService timeService;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    TimeServiceTest() {
        this.themeRepository = new FakeThemeRepository();
        this.timeRepository = new FakeTimeRepository();
        this.reservationRepository = new FakeReservationRepository();
        this.timeService = new TimeService(reservationRepository, timeRepository);
    }

    @Nested
    class GetTimesTest {

        @Test
        void 성공() {

            // given
            LocalTime startAt = LocalTime.of(10, 0);

            timeRepository.save(Time.create(startAt));
            timeRepository.save(Time.create(startAt.plusHours(1)));
            timeRepository.save(Time.create(startAt.plusHours(2)));

            // when
            List<TimeResponseDTO> actual = timeService.getTimes();

            // then
            assertAll(
                () -> assertEquals(3, actual.size()),
                () -> assertEquals(new TimeResponseDTO(1L, startAt), actual.get(0)),
                () -> assertEquals(new TimeResponseDTO(2L, startAt.plusHours(1)), actual.get(1)),
                () -> assertEquals(new TimeResponseDTO(3L, startAt.plusHours(2)), actual.get(2))
            );
        }
    }

    @Nested
    class SaveTimeTest {

        @Test
        void 성공() {

            // given
            TimeCreateRequestDTO request = new TimeCreateRequestDTO(LocalTime.of(15, 30));

            // when
            TimeResponseDTO actual = timeService.saveTime(request);

            // then
            assertAll(
                () -> assertEquals(1L, actual.id()),
                () -> assertEquals(LocalTime.of(15, 30), actual.startAt()),
                () -> assertEquals(List.of(actual), timeService.getTimes())
            );
        }
    }

    @Nested
    class DeleteTimeByIdTest {

        @Test
        void 성공() {

            // given
            Time savedTime = timeRepository.save(Time.create(LocalTime.of(12, 0)));
            timeRepository.save(Time.create(LocalTime.of(13, 0)));

            // when
            timeService.deleteTimeById(savedTime.getId());

            // then
            List<TimeResponseDTO> actual = timeService.getTimes();
            assertAll(
                () -> assertEquals(1, actual.size()),
                () -> assertEquals(LocalTime.of(13, 0), actual.getFirst().startAt())
            );
        }
    }

    @Nested
    class FindTimeIdsByDateAndThemeIdTest {

        @Test
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

            reservationRepository.save(Reservation.create("브라이언", LocalDate.of(2026, 5, 10), time1, theme1));
            reservationRepository.save(Reservation.create("제이슨", LocalDate.of(2026, 5, 10), time2, theme2));
            reservationRepository.save(Reservation.create("앨리스", LocalDate.of(2026, 5, 11), time3, theme3));
            reservationRepository.save(Reservation.create("데이브", LocalDate.of(2026, 5, 11), time4, theme1));

            LocalDate date = LocalDate.of(2026, 5, 10);
            Long themeId = 1L;
            List<TimeResponseDTO> expected = List.of(time2.toResponseDTO(), time3.toResponseDTO(),
                time4.toResponseDTO());

            // when
            List<TimeResponseDTO> actual = timeService.getAvailableTimes(date, themeId);

            // then
            assertThat(actual).isEqualTo(expected);
        }
    }
}
