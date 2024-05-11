package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Member;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    ReservationTimeService reservationTimeService;
    @Autowired
    ReservationService reservationService;
    @Autowired
    ThemeService themeService;

    @Test
    @DisplayName("예약 시간을 저장할 수 있다.")
    void save() {
        final ReservationTime reservationTime = reservationTimeService.save(new ReservationTimeRequest(LocalTime.now()));

        assertThat(reservationTime).isNotNull();
    }

    @Test
    @DisplayName("전체 예약 시간을 조회할 수 있다.")
    void findAll() {
        reservationTimeService.save(new ReservationTimeRequest(LocalTime.now()));

        final List<ReservationTime> reservationTimes = reservationTimeService.findAll();

        assertThat(reservationTimes).hasSize(1);
    }

    @Test
    @DisplayName("예약 시간을 삭제할 수 있다.")
    void delete() {
        reservationTimeService.save(new ReservationTimeRequest(LocalTime.now()));

        reservationTimeService.delete(1L);
        final List<ReservationTime> reservationTimes = reservationTimeService.findAll();

        assertThat(reservationTimes).hasSize(0);
    }

    @Test
    @DisplayName("이미 예약된 예약 시간을 삭제하려 하면 예외가 발생한다.")
    void invalidDelete() {
        ReservationTime savedReservationTime = reservationTimeService.save(new ReservationTimeRequest(LocalTime.now().plusHours(1)));
        Theme savedTheme = themeService.save(new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        final Member member = memberService.join(new MemberRequest("email@email.com", "1234", "뽀로로"));
        reservationService.save(new ReservationRequest(LocalDate.now().plusDays(1), savedReservationTime.getId(), savedTheme.getId(), member.getId()));

        assertThatThrownBy(() -> reservationTimeService.delete(savedReservationTime.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("해당 시간에 대한 예약이 존재하여 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("이미 예약된 예약 시간을 중복하여 저장하려 하면 예외가 발생한다.")
    void invalidSave() {
        LocalTime localTime = LocalTime.of(15, 40);
        reservationTimeService.save(new ReservationTimeRequest(localTime));

        assertThatThrownBy(() -> reservationTimeService.save(new ReservationTimeRequest(localTime)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("중복된 시간을 예약할 수 없습니다.");
    }

    @Test
    @DisplayName("예약 가능한 시간들을 조회할 수 있다.")
    void findAvailableTimes() {
        ReservationTime firstReservationTime = reservationTimeService.save(new ReservationTimeRequest(LocalTime.of(15, 30)));
        reservationTimeService.save(new ReservationTimeRequest(LocalTime.of(16, 30)));
        reservationTimeService.save(new ReservationTimeRequest(LocalTime.of(17, 30)));
        Theme savedTheme = themeService.save(new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        final Member member = memberService.join(new MemberRequest("email@email.com", "1234", "뽀로로"));
        final LocalDate tomorrow = LocalDate.now().plusDays(1);
        reservationService.save(new ReservationRequest(tomorrow, firstReservationTime.getId(), savedTheme.getId(), member.getId()));

        final List<AvailableTimeResponse> availableTimes = reservationTimeService.findAvailableTimes(tomorrow, savedTheme.getId());

        assertAll(
                () -> assertThat(availableTimes.get(0).alreadyBooked()).isTrue(),
                () -> assertThat(availableTimes.get(1).alreadyBooked()).isFalse(),
                () -> assertThat(availableTimes.get(2).alreadyBooked()).isFalse()
        );
    }
}
