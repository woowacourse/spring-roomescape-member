package roomescape.theme.domain;


import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.Status;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeRepository;

@Transactional
@SpringBootTest
class ThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository timeRepository;

    private final Clock fixedClock = Clock.fixed(
            Instant.parse("2026-05-12T01:00:00Z"),
            ZoneId.of("Asia/Seoul")
    );

    @Test
    @DisplayName("관리자가 테마를 추가하면 정상적으로 저장된다.")
    void normalTest() {
        Theme theme = Theme.builder()
                .name("포비")
                .description("포비가 나와요")
                .durationTime(LocalTime.now(fixedClock))
                .thumbnailImageUrl("http://~~~")
                .build();

        Theme savedTheme = themeRepository.save(theme);

        Assertions.assertThat(savedTheme.getId()).isNotNull();
    }

    @Test
    @DisplayName("관리자가 테마를 삭제하면 정상적으로 삭제된다.")
    void deleteTest() {
        Theme theme = Theme.builder()
                .name("포비")
                .description("포비가 나와요")
                .durationTime(LocalTime.now(fixedClock))
                .thumbnailImageUrl("http://~~~")
                .build();

        Theme savedTheme = themeRepository.save(theme);

        Assertions.assertThatCode(() -> themeRepository.delete(savedTheme.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("테마 2개를 저장한 후에 테마 전체를 조회하면, 크기가 2이다.")
    void findAllTest() {
        Theme themeFirst = Theme.builder()
                .name("포비")
                .description("포비가 나와요")
                .durationTime(LocalTime.now(fixedClock))
                .thumbnailImageUrl("http://~~~")
                .build();
        Theme themeSecond = Theme.builder()
                .name("리사")
                .description("리사가 나와요")
                .durationTime(LocalTime.now(fixedClock))
                .thumbnailImageUrl("http://~~~")
                .build();
        themeRepository.save(themeFirst);
        themeRepository.save(themeSecond);

        Assertions.assertThat(themeRepository.findAll()).hasSize(2);
    }

    @Test
    @DisplayName("예약 수에 따라 정렬된 테마가 조회된다.")
    void findByReservationCountWithLimitTest() {
        Theme horror = saveTheme("공포");
        Theme fantasy = saveTheme("판타지");
        Theme reasoning = saveTheme("추리");
        ReservationTime time = saveTime(LocalTime.now(fixedClock));
        saveReservations("포비", horror, time, LocalDate.now(fixedClock).minusDays( 1));
        saveReservations("브리", fantasy, time, LocalDate.now(fixedClock).minusDays( 2));
        saveReservations("브리", fantasy, time, LocalDate.now(fixedClock).minusDays( 3));
        saveReservations("리사", reasoning, time, LocalDate.now(fixedClock).minusDays(4));
        saveReservations("리사", reasoning, time, LocalDate.now(fixedClock).minusDays(5));
        saveReservations("리사", reasoning, time, LocalDate.now(fixedClock).minusDays(6));
        List<Theme> themes = themeRepository.findByReservationCountWithLimit(
                LocalDate.now(fixedClock).minusWeeks(1),
                LocalDate.now(fixedClock).minusDays(1),
                10
        );
        Assertions.assertThat(themes.getFirst()).isEqualTo(reasoning);
        Assertions.assertThat(themes.get(1)).isEqualTo(fantasy);
        Assertions.assertThat(themes.get(2)).isEqualTo(horror);
    }

    private Theme saveTheme(String name) {
        return themeRepository.save(Theme.builder()
                .name(name)
                .description(name + "가 나와요")
                .durationTime(LocalTime.now(fixedClock))
                .thumbnailImageUrl("http://~~~")
                .build());
    }

    private ReservationTime saveTime(LocalTime time) {
        return timeRepository.save(ReservationTime.builder().startAt(time).build());
    }

    private void saveReservations(String name, Theme theme, ReservationTime time, LocalDate date) {
            reservationRepository.save(Reservation.builder()
                    .name(name)
                    .theme(theme)
                    .date(date)
                    .time(time)
                    .status(Status.ACTIVE)
                    .build());
    }
}
