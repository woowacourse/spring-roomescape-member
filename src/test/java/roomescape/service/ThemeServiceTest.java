package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.BookedReservationTimeResponseDto;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;

@Sql(scripts = {"/test-schema.sql"})
@SpringBootTest
public class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @MockitoBean
    private ReservationTimeDao reservationTimeDao;

    @MockitoBean
    private ReservationDao reservationDao;

    @Autowired
    private ReservationService reservationService;


    @DisplayName("테마의 정보를 받아와 저장하고, ID를 설정할 수 있어야 한다.")
    @Test
    void given_theme_dto_then_set_id_and_save() {
        ThemeRequestDto themeRequestDto = new ThemeRequestDto(
            "테마1", "공포테마입니다", "http://aaa");
        ThemeResponseDto themeResponseDto = themeService.saveTheme(themeRequestDto);
        assertThat(themeResponseDto.id()).isNotNull();
        assertThat(themeResponseDto.name()).isEqualTo("테마1");
        assertThat(themeResponseDto.description()).isEqualTo("공포테마입니다");
        assertThat(themeResponseDto.thumbnail()).isEqualTo("http://aaa");
    }

    @DisplayName("존재하지 않는 테마를 삭제할 경우, 예외가 발생해야 한다.")
    @Test
    void when_delete_invalid_theme_then_throw_exception() {
        assertThatThrownBy(() -> themeService.deleteTheme(9999999999999L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약 여부를 포함한 예약 시간을 가져올 수 있어야 한다")
    @Test
    void get_all_reservation_contains_book_info() {
        ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime reservationTime2 = new ReservationTime(2L, LocalTime.of(11, 0));
        ReservationTime reservationTime3 = new ReservationTime(3L, LocalTime.of(12, 0));

        List<ReservationTime> reservationTimes = List.of(reservationTime1, reservationTime2,
            reservationTime3);
        when(reservationTimeDao.findAllReservationTimes()).thenReturn(reservationTimes);
        List<BookedReservationTimeResponseDto> allBookedReservationTimes =
            themeService.getAllBookedReservationTimes("2025-05-02", 1L);

        assertThat(allBookedReservationTimes.get(0).alreadyBooked()).isFalse();
        assertThat(allBookedReservationTimes.get(1).alreadyBooked()).isFalse();
        assertThat(allBookedReservationTimes.get(2).alreadyBooked()).isFalse();
    }

    @DisplayName("예약이 존재한 시간이라면 true를 반환해야 한다.")
    @Test
    void get_all_reservation_contains_book_info_already_exist_case() {
        ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));

        String date = "2025-05-02";
        List<ReservationTime> reservationTimes = List.of(reservationTime1);
        when(reservationTimeDao.findAllReservationTimes()).thenReturn(reservationTimes);
        when(reservationDao.calculateAlreadyExistReservationBy(date, 1L, 1L)).thenReturn(1);

        List<BookedReservationTimeResponseDto> allBookedReservationTimes =
            themeService.getAllBookedReservationTimes("2025-05-02", 1L);
        assertThat(allBookedReservationTimes.get(0).alreadyBooked()).isTrue();
    }

    //TODO : getAllThemeOfRanks 메서드 테스트 코드 작성
}


