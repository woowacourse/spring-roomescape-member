package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.model.ReservationTime;

@JdbcTest
@Import(TimeRepository.class)
public class ReservationTimeRepositoryTest {

    @Autowired
    private TimeRepository timeRepository;

    @Test
    void 전쳬_시간_조회를_할_수_있다() {
        // when
        List<ReservationTime> times = timeRepository.findAll();
        // then
        Assertions.assertEquals(14, times.size());
    }

    @Test
    void 특정_시간을_삭제할_수_있다() {
        // when
        timeRepository.deleteById(11L);
        // then
        Assertions.assertEquals(13, timeRepository.findAll().size());
    }

    @Test
    void timeId로_시간을_조회할_수_있다() {
        // when
        Optional<ReservationTime> time = timeRepository.findById(2L);
        // then
        Assertions.assertEquals(LocalTime.of(11, 0), time.get().getStartAt());
    }

    @Test
    void 얘약_가능한_시간을_저장할_수_있다() {
        // given
        LocalTime localTime = LocalTime.of(9, 0);
        // when
        timeRepository.save(localTime);
        // then
        Assertions.assertEquals(15, timeRepository.findAll().size());
    }

    @Test
    void 테마ID와_날짜로_예약_가능한_시간을_조회할_수_있다() {
        // given
        Long themeId = 2L;
        LocalDate date = LocalDate.now().minusDays(2);
        // when
        List<ReservationTime> times = timeRepository.findAllByThemeIdAndDate(themeId, date.toString());
        // then
        Assertions.assertEquals(12, times.size());
    }
}
