package roomescape.holiday.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import roomescape.holiday.domain.Holiday;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class JdbcHolidayRepositoryTest {

    @Autowired
    private JdbcHolidayRepository holidayRepository;

    @Test
    public void JdbcHolidayIsNotNull() {
        assertThat(holidayRepository).isNotNull();
    }

    @Test
    void save() {
        Holiday holiday = new Holiday(1L, LocalDate.of(2026, 5, 6));
        Holiday saved = holidayRepository.save(holiday);

        assertThat(saved.id()).isEqualTo(1L);
        assertThat(saved.date()).isEqualTo(LocalDate.of(2026, 5, 6));
    }

    @Test
    void findAll() {
        List<Holiday> holidays = List.of(new Holiday(1L, LocalDate.of(2026, 5, 6)),
                new Holiday(2L, LocalDate.of(2026, 6, 6)),
                new Holiday(3L, LocalDate.of(2026, 7, 6)));

        for (Holiday each: holidays) {
            holidayRepository.save(each);
        }

        List<Holiday> results = holidayRepository.findAll();

        assertThat(results).hasSize(3);
        assertThat(results.getFirst().id()).isEqualTo(1L);
        assertThat(results.getFirst().date()).isEqualTo(LocalDate.of(2026, 5, 6));
    }

    @Test
    void delete() {
        Holiday holiday = new Holiday(1L, LocalDate.of(2026, 5, 6));
        holidayRepository.save(holiday);
        assertThat(holidayRepository.delete(1L)).isTrue();
        assertThat(holidayRepository.findAll()).isEmpty();
    }
}