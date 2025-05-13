package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.dto.MemberResponse;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

class ReservationTest {

    private final Time validTime = new Time(1L, LocalTime.of(15, 0));
    private final Theme validTheme = new Theme(1L, "테마 이름", "테마 설명", "테마 썸네일");

    @Test
    @DisplayName("Reservation 객체를 정상적으로 생성할 수 있다.")
    void test1() {
        // given
        Long id = 1L;
        LocalDate date = LocalDate.of(2025, 5, 10);
        Member member = new Member(1L, "미미", "mimi@email.com", "password", Role.MEMBER);

        // when
        Reservation reservation = new Reservation(id, member, date, validTime, validTheme);

        // then
        assertThat(reservation.id()).isEqualTo(id);
        assertThat(reservation.member()).isEqualTo(member);
        assertThat(reservation.date()).isEqualTo(date);
        assertThat(reservation.time()).isEqualTo(validTime);
        assertThat(reservation.theme()).isEqualTo(validTheme);
    }

    @Test
    @DisplayName("DB에 저장하기 이전 기본 id로 Reservation 객체를 생성할 수 있다.")
    void test2() {
        // given
        LocalDate date = LocalDate.of(2025, 6, 15);
        Member member = new Member(1L, "미미", "mimi@email.com", "password", Role.MEMBER);

        // when
        Reservation reservation = Reservation.createBeforeSaved(member, date, validTime, validTheme);

        // then
        assertThat(reservation.id()).isEqualTo(0L);
        assertThat(reservation.member()).isEqualTo(member);
        assertThat(reservation.date()).isEqualTo(date);
        assertThat(reservation.time()).isEqualTo(validTime);
        assertThat(reservation.theme()).isEqualTo(validTheme);
    }

    @Test
    @DisplayName("id가 null인 경우 예외가 발생한다.")
    void test3() {
        // given
        LocalDate date = LocalDate.now();
        Member member = new Member(1L, "미미", "mimi@email.com", "password", Role.MEMBER);

        // when & then
        assertThatThrownBy(() -> new Reservation(null, member, date, validTime, validTheme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] id가 null이 되어서는 안 됩니다.");
    }

    @Test
    @DisplayName("member가 null인 경우 예외가 발생한다.")
    void test4() {
        // given
        Long id = 1L;
        LocalDate date = LocalDate.now();

        // when & then
        assertThatThrownBy(() -> new Reservation(id, null, date, validTime, validTheme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 사용자가 null이 되어서는 안 됩니다.");
    }

    @Test
    @DisplayName("날짜가 null인 경우 예외가 발생한다.")
    void test5() {
        // given
        Long id = 1L;
        Member member = new Member(1L, "미미", "mimi@email.com", "password", Role.MEMBER);

        // when & then
        assertThatThrownBy(() -> new Reservation(id, member, null, validTime, validTheme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 날짜가 null이 되어서는 안 됩니다.");
    }

    @Test
    @DisplayName("시간이 null인 경우 예외가 발생한다.")
    void test6() {
        // given
        Long id = 1L;
        LocalDate date = LocalDate.of(2025, 7, 1);
        Member member = new Member(1L, "미미", "mimi@email.com", "password", Role.MEMBER);

        // when & then
        assertThatThrownBy(() -> new Reservation(id, member, date, null, validTheme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 시간이 null이 되어서는 안 됩니다.");
    }

    @Test
    @DisplayName("테마가 null인 경우 예외가 발생한다.")
    void test7() {
        // given
        Long id = 1L;
        LocalDate date = LocalDate.of(2025, 8, 20);
        Member member = new Member(1L, "미미", "mimi@email.com", "password", Role.MEMBER);

        // when & then
        assertThatThrownBy(() -> new Reservation(id, member, date, validTime, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 테마가 null이 되어서는 안 됩니다.");
    }
}
