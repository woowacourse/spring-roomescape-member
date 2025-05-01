package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.ReservationAvailableTimeResponse;
import roomescape.fake.ReservationFakeRepository;
import roomescape.fake.ReservationTimeFakeRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

class UserReservationTimeServiceTest {

    private final ReservationRepository reservationRepository = new ReservationFakeRepository();
    private final ReservationTimeRepository reservationTimeRepository = new ReservationTimeFakeRepository();
    private final UserReservationTimeService service = new UserReservationTimeService(
            reservationRepository,
            reservationTimeRepository);

    @Test
    @DisplayName("사용자가 날짜와 테마를 선택하면 예약 가능한 시간들을 DTO로 반환한다.")
    void test_readAvailableReservationTimes() {
        //given
        LocalDate givenDate = LocalDate.MAX;
        Long givenTheme = 1L;
        //when
        List<ReservationAvailableTimeResponse> actual = service.readAvailableReservationTimes(givenDate, givenTheme);
        //then
        assertThat(actual.size()).isEqualTo(2);
        ReservationAvailableTimeResponse bookedResponse = actual.stream()
                .filter(current -> current.startAt().equals(LocalTime.MAX))
                .findFirst()
                .orElseThrow(() -> new AssertionError("예약된 시간이 결과에 없습니다"));
        assertThat(bookedResponse.isBooked()).isTrue();

        ReservationAvailableTimeResponse availableResponse = actual.stream()
                .filter(current -> current.startAt().equals(LocalTime.of(11, 0)))
                .findFirst()
                .orElseThrow(() -> new AssertionError("가능한 시간이 결과에 없습니다"));
        assertThat(availableResponse.isBooked()).isFalse();
    }
}
