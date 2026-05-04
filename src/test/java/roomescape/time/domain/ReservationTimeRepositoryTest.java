package roomescape.time.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeRepositoryTest {

    @Autowired
    private ReservationTimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("오늘 날짜로 등록된 테마가 있으면 조회된다.")
    void normalTest() {
        ReservationTime time = timeRepository.save(ReservationTime.builder()
                .startAt(LocalTime.now().withNano(0))
                .build());
        Theme theme = themeRepository.save(Theme.builder()
                .name("포비")
                .description("포비가 나와요")
                .thumbnailImageUrl("https://~~~~")
                .durationTime(LocalTime.of(1, 0))
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                        .name("포비")
                        .date(LocalDate.now())
                        .time(time)
                        .theme(theme)
                .build());

        Assertions.assertThat(reservation.getId()).isNotNull();
        Assertions.assertThat(reservation.getTime()).isEqualTo(time);
        Assertions.assertThat(reservation.getTheme()).isEqualTo(theme);
    }
}
