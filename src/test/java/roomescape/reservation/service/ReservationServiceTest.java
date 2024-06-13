package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import roomescape.member.domain.Member;
import roomescape.member.repository.MemberDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservation.request.ReservationRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeDao;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ThemeDao themeDao;

    @Test
    @DisplayName("중복된 예약을 생성하면 예외가 발생한다.")
    void saveDuplicateException() {
        String date = "2024-05-12";

        insertReservation(1L, date, 1L, 1L);
        assertThatThrownBy(() -> reservationService.save(new ReservationRequest(
                LocalDate.parse(date),
                1L,
                1L,
                1L)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reservation already exists");
    }

    void insertReservation(long memberId, String date, long timeId, long themeId) {
        ReservationTime time = reservationTimeDao.findById(timeId).get();
        Theme theme = themeDao.findById(themeId).get();
        Member member = memberDao.findMemberById(memberId).get();
        reservationDao.save(new Reservation(member, LocalDate.parse(date), time, theme));
    }

}
