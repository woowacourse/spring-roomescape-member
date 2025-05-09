package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @DisplayName("두 예약이 서로 중복된 예약인지 판단한다.")
    @Test
    void isDuplicateReservation() {
        // given
        LocalDate date = LocalDate.of(2025, 4, 18);
        ReservationTime time = new ReservationTime("08:30");
        ReservationTheme theme = new ReservationTheme("제목", "설명", "썸네일");
        Reservation reservation = new Reservation("제프리", date, time, theme);
        Reservation duplicated = new Reservation("플린트", date, time, theme);

        // when
        boolean isDuplicated = reservation.isDuplicateReservation(duplicated);

        // then
        assertThat(isDuplicated).isTrue();
    }

    @DisplayName("날짜가 다르면 중복이 아니다.")
    @Test
    void isNotDuplicate_whenDateIsDifferent() {
        //given
        LocalDate date1 = LocalDate.of(2025, 4, 18);
        LocalDate date2 = LocalDate.of(2025, 4, 19);
        ReservationTime time = new ReservationTime("08:30");
        ReservationTheme theme = new ReservationTheme("제목", "설명", "썸네일");
        Reservation reservation1 = new Reservation("제프리", date1, time, theme);
        Reservation reservation2 = new Reservation("플린트", date2, time, theme);

        //when
        boolean isDuplicated = reservation1.isDuplicateReservation(reservation2);

        //then
        assertThat(isDuplicated).isFalse();
    }

    @DisplayName("시간이 다르면 중복이 아니다.")
    @Test
    void isNotDuplicate_whenTimeIsDifferent() {
        //given
        LocalDate date = LocalDate.of(2025, 4, 18);
        ReservationTheme theme = new ReservationTheme("제목", "설명", "썸네일");
        ReservationTime time1 = new ReservationTime("08:30");
        ReservationTime time2 = new ReservationTime("09:00");
        Reservation reservation1 = new Reservation("제프리", date, time1, theme);
        Reservation reservation2 = new Reservation("플린트", date, time2, theme);

        //when
        boolean isDuplicated = reservation1.isDuplicateReservation(reservation2);

        //then
        assertThat(isDuplicated).isFalse();
    }

    @DisplayName("이름의 사이즈가 최댓값 이내로 입력되었는지 검증한다.")
    @Test
    void validateNameSize() {
        // given
        String validName = "가나다라마";
        String invalidName = "가나다라마바";
        LocalDate date = LocalDate.of(2025, 4, 18);

        // when
        Reservation reservation = new Reservation(validName, date, null, null);

        // then
        assertThat(reservation.getName()).isEqualTo("가나다라마");
        assertThatThrownBy(() -> new Reservation(invalidName, date, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름은 5글자 이내로 입력해 주세요. 현재 길이는 6글자 입니다.");

    }

    @DisplayName("이름이 한국어로만 입력되었는지 검증한다.")
    @Test
    void validateNameIsKorean() {
        //given
        String validName = "제프리";
        String invalidName = "Jeffrey";
        LocalDate date = LocalDate.of(2025, 4, 18);

        // when
        Reservation reservation = new Reservation(validName, date, null, null);

        // then
        assertThat(reservation.getName()).isEqualTo("제프리");
        assertThatThrownBy(() -> new Reservation(invalidName, date, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름은 한국어로만 입력해 주세요.");

    }

}
