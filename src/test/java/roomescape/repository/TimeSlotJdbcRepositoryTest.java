package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.Fixtures.JUNK_TIME_SLOT;
import static roomescape.Fixtures.JUNK_TIME_SLOT_1;
import static roomescape.Fixtures.JUNK_TIME_SLOT_2;
import static roomescape.Fixtures.JUNK_TIME_SLOT_3;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.model.TimeSlot;

@Sql(scripts = {"/test-schema.sql", "/test-data.sql"})
@JdbcTest
class TimeSlotJdbcRepositoryTest {

    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    public TimeSlotJdbcRepositoryTest(DataSource dataSource) {
        this.timeSlotRepository = new TimeSlotJdbcRepository(dataSource);
    }

    @Test
    @DisplayName("모든 타임 슬롯 목록을 조회한다.")
    void findAll() {
        // given
        var timeSlots = List.of(
                JUNK_TIME_SLOT_1, JUNK_TIME_SLOT_2, JUNK_TIME_SLOT_3
        );

        // when
        final List<TimeSlot> found = timeSlotRepository.findAll();

        // then
        assertThat(found).containsExactlyInAnyOrderElementsOf(timeSlots);
    }

    @Test
    @DisplayName("타임 슬롯을 저장한다.")
    void save() {
        // given
        var timeSlot = JUNK_TIME_SLOT;

        // when
        final Long saved = timeSlotRepository.save(timeSlot);

        // then
        assertThat(saved).isEqualTo(timeSlot.id());
    }

    @Test
    @DisplayName("타임 슬롯 ID에 해당하는 타임 슬롯을 조회한다.")
    void findById() {
        // given
        var timeSlot = JUNK_TIME_SLOT_1;
        var timeSlotId = timeSlot.id();

        // when
        final Optional<TimeSlot> found = timeSlotRepository.findById(timeSlotId);

        // then
        assertThat(found).isPresent()
                .isEqualTo(Optional.of(timeSlot));
    }

    @Test
    @DisplayName("타임 슬롯 ID에 해당하는 타임 슬롯을 삭제한다.")
    void removeById() {
        // given
        var timeSlot = JUNK_TIME_SLOT;
        timeSlotRepository.save(timeSlot);
        var timeSlotId = timeSlot.id();

        // when
        final Boolean removed = timeSlotRepository.removeById(timeSlotId);

        // then
        assertThat(removed).isTrue();
    }
}
