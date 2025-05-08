package roomescape.integration.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import roomescape.common.RepositoryBaseTest;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.AvailableReservationTime;
import roomescape.domain.time.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.integration.fixture.ReservationDateFixture;
import roomescape.integration.fixture.ReservationDbFixture;
import roomescape.integration.fixture.ReservationTimeDbFixture;
import roomescape.integration.fixture.ThemeDbFixture;

public class ReservationTimeRepositoryTest extends RepositoryBaseTest {

    @Autowired
    private ReservationTimeRepository repository;

    private static final String SELECT_BY_ID = "SELECT * FROM reservation_time WHERE id = ?";
    private static final String COUNT_BY_ID = "SELECT COUNT(*) FROM reservation_time WHERE id = ?";

    @Autowired
    private ReservationTimeDbFixture reservationTimeDbFixture;

    @Autowired
    private ThemeDbFixture themeDbFixture;

    @Autowired
    private ReservationDbFixture reservationDbFixture;

    @Test
    void 예약시간을_저장할_수_있다() {
        // given
        LocalTime time = LocalTime.of(10, 0);

        // when
        ReservationTime saved = repository.save(time);

        // then
        Map<String, Object> row = jdbcTemplate.queryForMap(SELECT_BY_ID, saved.getId());
        assertThat(row.get("start_at")).isEqualTo("10:00");
    }

    @Test
    void 모든_예약시간을_조회할_수_있다() {
        // given
        LocalTime time1 = LocalTime.of(10, 0);
        LocalTime time2 = LocalTime.of(14, 30);

        ReservationTime saved1 = repository.save(time1);
        ReservationTime saved2 = repository.save(time2);

        // when
        List<ReservationTime> all = repository.findAll();

        // then
        assertThat(all).hasSize(2);
        assertThat(all)
                .extracting(ReservationTime::getId, ReservationTime::getStartAt)
                .containsExactlyInAnyOrder(
                        tuple(saved1.getId(), time1),
                        tuple(saved2.getId(), time2)
                );
    }

    @Test
    void 예약시간을_ID로_조회할_수_있다() {
        // given
        ReservationTime saved = repository.save(LocalTime.of(11, 0));

        // when
        Optional<ReservationTime> found = repository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getStartAt()).isEqualTo(LocalTime.of(11, 0));
    }

    @Test
    void 존재하지_않는_ID로_조회하면_빈값을_반환한다() {
        // when
        Optional<ReservationTime> found = repository.findById(999L);

        // then
        assertThat(found).isNotPresent();
    }

    @Test
    void 예약시간을_삭제할_수_있다() {
        // given
        ReservationTime saved = repository.save(LocalTime.of(15, 0));

        // when
        repository.deleteById(saved.getId());

        // then
        Integer count = jdbcTemplate.queryForObject(COUNT_BY_ID, Integer.class, saved.getId());
        assertThat(count).isEqualTo(0);
    }

    @Test
    void 특정_시간이_존재하는지_확인할_수_있다() {
        // given
        repository.save(LocalTime.of(16, 0));

        // when
        boolean exists = repository.existByStartAt(LocalTime.of(16, 0));
        boolean notExists = repository.existByStartAt(LocalTime.of(17, 0));

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(exists).isTrue();
        softly.assertThat(notExists).isFalse();
    }

    @Test
    void 예약_가능_시간들을_조회할_수_있다() {
        // given
        ReservationTime 예약시간 = reservationTimeDbFixture.예약시간_10시();
        reservationTimeDbFixture.예약시간_11시();
        Theme 공포 = themeDbFixture.공포();
        reservationDbFixture.예약_한스_25_4_22(예약시간, 공포);

        // when
        List<AvailableReservationTime> available = repository.findAllAvailableReservationTimes(
                ReservationDateFixture.예약날짜_25_4_22.date(), 공포.getId());

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(available).hasSize(2);
        softly.assertThat(available.get(0).time().getStartAt()).isEqualTo(LocalTime.of(10, 0));
        softly.assertThat(available.get(0).available()).isTrue();
        softly.assertThat(available.get(1).time().getStartAt()).isEqualTo(LocalTime.of(11, 0));
        softly.assertThat(available.get(1).available()).isFalse();
        softly.assertAll();
    }
}
