package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

@JdbcTest
@Import(ReservationRepository.class)
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void 모든_예약을_조회한다() {
        // when
        List<Reservation> reservations = reservationRepository.findAll();
        // then
        Assertions.assertEquals(17, reservations.size());
    }

    @Test
    void 예약을_삭제할_수_있다() {
        // when
        reservationRepository.deleteById(2L);
        // then
        Assertions.assertEquals(16, reservationRepository.findAll().size());
    }

    @Test
    void 예약을_생성할_수_있다() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "붉은 요람",
                "10년 전 의문의 화재로 폐쇄된 안개마을 영아원. 그곳에서 들려오는 울음소리의 정체를 밝혀야 합니다.",
                "https://picsum.photos/seed/cradle/400/300");
        Reservation reservation = new Reservation(null, "포비", LocalDate.of(2026, 5, 5), time, theme);

        // when
        Reservation saved = reservationRepository.save(reservation);

        // then
        Assertions.assertEquals(18, reservationRepository.findAll().size());
        Assertions.assertEquals(18L, saved.getId());
        Assertions.assertEquals("포비", saved.getName());
        Assertions.assertEquals(LocalDate.of(2026, 5, 5), saved.getDate());
        Assertions.assertEquals(1L, saved.getTime().getId());
        Assertions.assertEquals(1L, saved.getTheme().getId());
    }
}
