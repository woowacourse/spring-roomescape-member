package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Member;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.domain.exception.InvalidReservationTimeException;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ThemeRequest;
import roomescape.service.exception.DeleteException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeServiceTest {

    @Autowired
    ReservationTimeService reservationTimeService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    ThemeService themeService;

    @Test
    @DisplayName("예약 시간을 저장할 수 있다.")
    void save() {
        ReservationTime reservationTime = reservationTimeService.save(new ReservationTimeRequest(LocalTime.now()));

        assertThat(reservationTime).isNotNull();
    }

    @Test
    @DisplayName("전체 예약 시간을 조회할 수 있다.")
    void findAll() {
        reservationTimeService.save(new ReservationTimeRequest(LocalTime.now()));

        List<ReservationTime> reservationTimes = reservationTimeService.findAll();

        assertThat(reservationTimes).hasSize(1);
    }

    @Test
    @DisplayName("예약 시간을 삭제할 수 있다.")
    void delete() {
        reservationTimeService.save(new ReservationTimeRequest(LocalTime.now()));

        reservationTimeService.delete(1L);
        List<ReservationTime> reservationTimes = reservationTimeService.findAll();

        assertThat(reservationTimes).hasSize(0);
    }

    @Test
    @DisplayName("이미 예약된 예약 시간을 삭제하려 하면 예외가 발생한다.")
    void invalidDelete() {
        LocalTime localTime = LocalTime.now();

        ReservationTime savedReservationTime = reservationTimeService.save(new ReservationTimeRequest(localTime.plusHours(1)));
        Theme savedTheme = themeService.save(new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        Member member = new Member("잉크", "asdf@a.com", "1234", Role.ADMIN);
        reservationService.save(member, new ReservationRequest(LocalDate.now(), savedReservationTime.getId(), savedTheme.getId()));

        assertThatThrownBy(() -> reservationTimeService.delete(savedReservationTime.getId()))
                .isInstanceOf(DeleteException.class);
    }

    @Test
    @DisplayName("이미 예약된 예약 시간을 중복하여 저장하려 하면 예외가 발생한다.")
    void invalidSave() {
        LocalTime localTime = LocalTime.of(15, 40);
        reservationTimeService.save(new ReservationTimeRequest(localTime));

        assertThatThrownBy(() -> reservationTimeService.save(new ReservationTimeRequest(localTime)))
                .isInstanceOf(InvalidReservationTimeException.class);
    }
}
