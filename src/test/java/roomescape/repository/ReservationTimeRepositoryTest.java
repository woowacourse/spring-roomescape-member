package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.util.TestDataInitializer;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeRepositoryTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TestDataInitializer dataInitializer;

    @Test
    void 특정_날짜와_테마에_이미_예약된_시간_식별자를_조회한다() {
        LocalDate targetDate = LocalDate.of(2026, 5, 20);
        ReservationTime reservedTime = dataInitializer.createReservationTime(LocalTime.of(10, 0));
        ReservationTime otherTime = dataInitializer.createReservationTime(LocalTime.of(11, 0));
        Theme targetTheme = dataInitializer.createTheme("대상 테마", "설명", "/images/themes/target.webp");
        Theme otherTheme = dataInitializer.createTheme("다른 테마", "설명", "/images/themes/other.webp");

        dataInitializer.createReservation("사용자일", targetDate, reservedTime.getId(), targetTheme.getId());
        dataInitializer.createReservation("사용자이", targetDate.plusDays(1), otherTime.getId(), targetTheme.getId());
        dataInitializer.createReservation("사용자삼", targetDate, otherTime.getId(), otherTheme.getId());

        List<Long> reservedTimeIds = reservationTimeRepository.findReservedTimeIds(targetTheme.getId(), targetDate);

        assertThat(reservedTimeIds).containsExactly(reservedTime.getId());
    }

    @Test
    void 특정_날짜와_테마에_예약이_없으면_빈_목록을_반환한다() {
        Theme theme = dataInitializer.createTheme("빈 테마", "설명", "/images/themes/empty.webp");

        List<Long> reservedTimeIds = reservationTimeRepository.findReservedTimeIds(
                theme.getId(),
                LocalDate.of(2026, 5, 20)
        );

        assertThat(reservedTimeIds).isEmpty();
    }

    @Test
    void 취소된_예약의_시간은_예약된_시간으로_조회하지_않는다() {
        LocalDate targetDate = LocalDate.of(2026, 5, 20);
        ReservationTime reservedTime = dataInitializer.createReservationTime(LocalTime.of(10, 0));
        ReservationTime cancelledTime = dataInitializer.createReservationTime(LocalTime.of(11, 0));
        Theme theme = dataInitializer.createTheme("테마", "설명", "/images/themes/theme.webp");

        dataInitializer.createReservation("예약사용자", targetDate, reservedTime.getId(), theme.getId());
        Reservation cancelledReservation = dataInitializer.createReservation(
                "취소사용자",
                targetDate,
                cancelledTime.getId(),
                theme.getId()
        );
        reservationRepository.updateStatus(cancelledReservation.cancel());

        List<Long> reservedTimeIds = reservationTimeRepository.findReservedTimeIds(theme.getId(), targetDate);

        assertThat(reservedTimeIds).containsExactly(reservedTime.getId());
    }
}
