package roomescape.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
        assertEquals(17, reservations.size());
    }

    @Test
    void 예약을_삭제할_수_있다() {
        // when
        reservationRepository.deleteById(2L);
        // then
        assertEquals(16, reservationRepository.findAll().size());
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
        assertAll(
                () -> assertEquals(18, reservationRepository.findAll().size()),
                () -> assertEquals(18L, saved.id()),
                () -> assertEquals("포비", saved.name()),
                () -> assertEquals(LocalDate.of(2026, 5, 5), saved.date()),
                () -> assertEquals(1L, saved.time().getId()),
                () -> assertEquals(1L, saved.theme().getId())
        );
    }
}
