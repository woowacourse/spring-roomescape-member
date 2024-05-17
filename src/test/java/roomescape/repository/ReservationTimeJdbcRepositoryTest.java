package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.global.exception.exceptions.ExistingEntryException;
import roomescape.global.exception.exceptions.ReferencedRowExistsException;
import roomescape.member.domain.MemberRepository;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.theme.domain.ThemeRepository;
import roomescape.time.domain.ReservationTimeRepository;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeJdbcRepositoryTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("중복된 예약 시간 추가가 불가능한 지 확인한다.")
    void checkDuplicatedReservationTIme() {
        //given
        LocalTime time = LocalTime.parse("10:00");
        ReservationTime reservationTime1 = new ReservationTime(time);
        ReservationTime reservationTime2 = new ReservationTime(time);
        reservationTimeRepository.save(reservationTime1);

        //when & then
        assertThatThrownBy(() ->
                reservationTimeRepository.save(reservationTime2)
        ).isInstanceOf(ExistingEntryException.class)
                .hasMessage(time + "은 이미 추가된 예약 시간입니다.");
    }

    @Test
    @DisplayName("삭제하려는 예약 시간에 예약이 존재한다면 삭제가 불가능한지 확인한다.")
    void checkDeleteReservedTime() {
        //given
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        ReservationTime reservationTime = reservationTimeRepository.findByTimeId(1L);
        themeRepository.save(new Theme("테마명", "테마 설명", "테마 이미지"));
        Theme theme = themeRepository.findByThemeId(1L);
        Member member = memberRepository.findByMemberId(1L);
        reservationRepository.save(new Reservation(
                LocalDate.parse("2025-10-05"),
                reservationTime,
                theme,
                member
        ));

        //when & then
        assertThatThrownBy(() ->
                reservationTimeRepository.deleteById(1L)
        ).isInstanceOf(ReferencedRowExistsException.class)
                .hasMessage("현 예약 시간에 예약이 존재합니다.");
    }
}
