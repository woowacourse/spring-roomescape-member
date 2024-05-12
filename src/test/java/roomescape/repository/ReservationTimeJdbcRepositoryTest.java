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
import roomescape.domain.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeJdbcRepositoryTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("중복된 예약 시간 추가가 불가능한 지 확인한다.")
    void checkDuplicatedReservationTIme() {
        //given
        ReservationTime reservationTime1 = new ReservationTime(LocalTime.parse("10:00"));
        ReservationTime reservationTime2 = new ReservationTime(LocalTime.parse("10:00"));
        reservationTimeRepository.save(reservationTime1);

        //when & then
        assertThatThrownBy(() ->
                reservationTimeRepository.save(reservationTime2)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 추가된 예약 시간입니다.");
    }

    @Test
    @DisplayName("삭제하려는 예약 시간에 예약이 존재한다면 삭제가 불가능한지 확인한다.")
    void checkDeleteReservedTime() {
        //given
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        ReservationTime reservationTime = reservationTimeRepository.findByTimeId(1L);
        themeRepository.save(new Theme("테마명", "테마 설명", "테마 이미지"));
        Theme theme = themeRepository.findByThemeId(1L);
        reservationRepository.save(new Reservation(
                new User("admin1@email.com", "password"),
                LocalDate.parse("2025-10-05"),
                reservationTime,
                theme
        ));

        //when & then
        assertThatThrownBy(() ->
                reservationTimeRepository.deleteById(1L)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("현 예약 시간에 예약이 존재합니다.");
    }
}
