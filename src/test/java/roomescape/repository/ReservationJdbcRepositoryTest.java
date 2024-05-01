package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.UserName;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationJdbcRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("중복된 예약 시간 추가가 불가능한 지 확인한다.")
    void checkDuplicatedReservationTIme() {
        //given
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        ReservationTime reservationTime = reservationTimeRepository.findByTimeId(1L);
        themeRepository.save(new Theme("테마명", "테마 설명", "테마 이미지"));
        Theme theme = themeRepository.findByThemeId(1L);
        Reservation reservation1 = new Reservation(
                new UserName("초롱"),
                LocalDate.parse("2025-10-05"),
                reservationTime,
                theme
        );
        Reservation reservation2 = new Reservation(
                new UserName("메이슨"),
                LocalDate.parse("2025-10-05"),
                reservationTime,
                theme
        );
        reservationRepository.save(reservation1);

        //when & then
        assertThatThrownBy(() ->
                reservationRepository.save(reservation2)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 예약된 시간입니다.");
    }
}
