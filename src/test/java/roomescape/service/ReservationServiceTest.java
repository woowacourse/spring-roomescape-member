package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ThemeService themeService;

    @Test
    void 예약을_저장하고_조회한다() {
        //given
        ReservationTime savedTime = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        Reservation savedReservation = reservationService.save("맥스", LocalDate.of(2030, 5, 20), savedTime.getId(),
                savedTheme.getId());

        //when
        List<Reservation> reservations = reservationService.findAll(0, 20);

        //then
        assertThat(reservations.getFirst().getId()).isEqualTo(savedReservation.getId());
    }

    @Test
    void 예약을_저장하고_삭제한다() {
        //given
        ReservationTime savedTime = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        Reservation savedReservation = reservationService.save("맥스", LocalDate.of(2030, 5, 20), savedTime.getId(),
                savedTheme.getId());

        //when
        reservationService.deleteById(savedReservation.getId());

        //then
        assertThat(reservationService.findAll(0, 20)).hasSize(0);
    }

    @Test
    void 날짜_시간_테마가_같으면_중복예약_불가() {
        //given
        ReservationTime savedTime = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        Reservation savedReservation = reservationService.save("맥스", LocalDate.of(2030, 5, 20), savedTime.getId(),
                savedTheme.getId());

        //when & then
        assertThatThrownBy(() -> reservationService.save(
                "피노",
                LocalDate.of(2030, 5, 20),
                savedTime.getId(),
                savedTheme.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 예약");
    }

    @Test
    void 날짜_시간이_같고_테마가_다르면_예약_가능() {
        //given
        ReservationTime savedTime = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        Theme savedOtherTheme = themeService.save(new Theme("코미디", "웃김", "https://roomescape.com"));

        reservationService.save("맥스", LocalDate.of(2030, 5, 20), savedTime.getId(),
                savedTheme.getId());


        Reservation newReservation = reservationService.save(
                "피노",
                LocalDate.of(2030, 5, 20),
                savedTime.getId(),
                savedOtherTheme.getId());

        assertThat(newReservation.getId()).isNotNull();
    }

    @Test
    void 날짜_테마가_같고_시간이_다르면_예약_가능() {
        //given
        ReservationTime savedTime = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        ReservationTime savedOtherTime = reservationTimeService.save(new ReservationTime(LocalTime.of(11, 0)));

        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));

        reservationService.save("맥스", LocalDate.of(2030, 5, 20), savedTime.getId(),
                savedTheme.getId());


        Reservation newReservation = reservationService.save(
                "피노",
                LocalDate.of(2030, 5, 20),
                savedOtherTime.getId(),
                savedTheme.getId());

        assertThat(newReservation.getId()).isNotNull();
    }

    @Test
    void 지난_날짜의_예약은_생성할_수_없다() {
        ReservationTime savedTime = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));

        assertThatThrownBy(() -> reservationService.save(
                "맥스",
                LocalDate.of(2030, 5, 14),
                savedTime.getId(),
                savedTheme.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지난 날짜 또는 시간은 예약할 수 없습니다.");
    }

    @Test
    void 오늘의_지난_시간은_예약할_수_없다() {
        ReservationTime savedTime = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));

        assertThatThrownBy(() -> reservationService.save(
                "맥스",
                LocalDate.of(2030, 5, 15),
                savedTime.getId(),
                savedTheme.getId()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지난 날짜 또는 시간은 예약할 수 없습니다.");
    }

    @TestConfiguration
    public static class TestClockConfig {
        @Bean
        @Primary
        public Clock fixedClock() {
            return Clock.fixed(
                    LocalDateTime.of(2030, 5, 15, 12, 0)
                            .atZone(ZoneId.of("Asia/Seoul"))
                            .toInstant(),
                    ZoneId.of("Asia/Seoul")
            );
        }
    }
}
