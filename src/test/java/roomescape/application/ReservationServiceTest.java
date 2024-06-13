package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.member.Member;
import roomescape.domain.member.repository.MemberRepository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.ReservationTime;
import roomescape.domain.time.repository.ReservationTimeRepository;
import roomescape.dto.reservation.ReservationInfo;
import roomescape.fixture.MemberFixture;
import roomescape.fixture.ThemeFixture;
import roomescape.support.extension.TableTruncateExtension;

@SpringBootTest
@ExtendWith({TableTruncateExtension.class})
@Sql("/clear.sql")
class ReservationServiceTest {

    private final ReservationTime time = new ReservationTime(1L, LocalTime.parse("12:12"));
    private final Theme theme = ThemeFixture.theme();
    private final Member member = MemberFixture.member();

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        reservationTimeRepository.save(time);
        themeRepository.save(theme);
        memberRepository.save(member);
    }

    @Test
    void 예약을_성공한다() {
        LocalDate date = LocalDate.now().plusDays(2);
        ReservationInfo reservationInfo = new ReservationInfo(member.getName().getValue(), date, time.getId(),
                theme.getId());

        Reservation reservation = reservationService.addReservation(reservationInfo);

        assertThat(reservation.getMember().getName().getValue()).isEqualTo(member.getName().getValue());
        assertEquals(date, reservation.getDate());
        assertThat(reservation.getStartAtTime()).isEqualTo(time.getStartAt());
    }

    @Test
    void 최소_1일_전에_예약하지_않으면_예약을_실패한다() {
        LocalDate invalidDate = LocalDate.now().minusDays(1);
        ReservationInfo reservationInfo = new ReservationInfo(member.getName().getValue(), invalidDate, time.getId(),
                theme.getId());

        assertThatThrownBy(() -> reservationService.addReservation(reservationInfo))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("예약은 최소 1일 전에 해야합니다.");
    }

    @Test
    void 중복된_예약이_있으면_예약을_실패한다() {
        LocalDate date = LocalDate.now().plusDays(2);
        ReservationInfo reservationInfo = new ReservationInfo(member.getName().getValue(), date, time.getId(),
                theme.getId());
        reservationService.addReservation(reservationInfo);

        assertThatThrownBy(() -> reservationService.addReservation(reservationInfo))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 예약된 날짜, 시간입니다.");
    }

    @Test
    void 예약을_취소한다() {
        LocalDate date = LocalDate.now().plusDays(2);
        ReservationInfo reservationInfo = new ReservationInfo(member.getName().getValue(), date, time.getId(),
                theme.getId());
        Reservation reservation = reservationService.addReservation(reservationInfo);

        reservationService.cancel(reservation.getId());

        assertThat(reservationService.getReservations()).isEmpty();
    }

    @Test
    void 존재하지_않는_예약을_취소하면_예외가_발생한다() {
        assertThatThrownBy(() -> reservationService.cancel(1L))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 예약입니다.");
    }
}
