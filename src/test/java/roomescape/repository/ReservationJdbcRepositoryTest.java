package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.exceptions.ExistingEntryException;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationJdbcRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        ReservationTime reservationTime = reservationTimeRepository.findByTimeId(1L);
        themeRepository.save(new Theme("테마명", "테마 설명", "테마 이미지"));
        Theme theme = themeRepository.findByThemeId(1L);
        Member member = memberRepository.findByMemberId(1L);
        Reservation reservation1 = new Reservation(
                LocalDate.parse("2025-10-05"),
                reservationTime,
                theme,
                member
        );
        reservationRepository.save(reservation1);
    }

    @Test
    @DisplayName("전체 예약 조회 기능을 확인한다.")
    void checkFindAllReservation() {
        //when & then
        assertThat(reservationRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("날짜와 테마 id로 예약을 조회하는 기능을 확인한다.")
    void checkFindReservationByDateAndThemeId() {
        //given
        LocalDate date = LocalDate.parse("2025-10-05");
        Long themeId = 1L;

        //when
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);

        //then
        assertThat(reservations.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("중복된 예약 시간에 예약 추가가 불가능한 지 확인한다.")
    void checkDuplicatedReservationTIme() {
        //given
        LocalDate date = LocalDate.parse("2025-10-05");
        ReservationTime reservationTime = reservationTimeRepository.findByTimeId(1L);
        Theme theme = themeRepository.findByThemeId(1L);
        Member member = memberRepository.findByMemberId(1L);
        Reservation reservation2 = new Reservation(
                date,
                reservationTime,
                theme,
                member
        );

        //when & then
        assertThatThrownBy(() ->
                reservationRepository.save(reservation2)
        ).isInstanceOf(ExistingEntryException.class)
                .hasMessage(date + " " + reservationTime.getStartAt() + "은 이미 예약된 시간입니다.");
    }
}
