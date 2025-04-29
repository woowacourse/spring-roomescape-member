package roomescape.reservationtime.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.reservationtime.domain.ReservationTime;

class JdbcReservationTimeRepositoryTest {

    private static EmbeddedDatabase db;
    private JdbcReservationTimeRepository repository;

    @BeforeEach
    void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .addScript("classpath:data.sql")
                .build();
        repository = new JdbcReservationTimeRepository(db);
    }

    @AfterEach
    void shutdownDatabase() {
        db.shutdown();
    }

    @Test
    void 예약_시간을_올바르게_저장한다() {
        // given
        LocalTime startAt = LocalTime.of(13, 0);

        // when
        ReservationTime reservationTime = repository.save(startAt);

        // then
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(reservationTime.getId()).isNotNull();
            soft.assertThat(reservationTime.getStartAt()).isEqualTo(startAt);
        });
    }

    @Test
    void 모든_예약_시간을_조회한다() {
        // given
        // when
        List<ReservationTime> reservationTimes = repository.findAll();

        // then
        assertThat(reservationTimes).hasSize(2);
    }

    @Test
    void id에_알맞은_예약_시간을_삭제한다() {
        // given
        // when
        repository.deleteById(1L);
        List<ReservationTime> reservationTimes = repository.findAll();

        // then
        assertThat(reservationTimes).hasSize(1)
                .extracting(ReservationTime::getId)
                .doesNotContain(1L);
    }

    @Test
    void id에_알맞은_예약_시간을_가져온다() {
        // given
        Long id = 1L;

        // when
        ReservationTime reservationTime = repository.findById(id).get();

        // then
        assertThat(reservationTime.getId()).isEqualTo(id);
    }

    @Test
    void 존재하지_않는_id면_빈_Optional을_반환한다() {
        // given
        Long invalidId = 999L;
        // when
        // then
        assertThat(repository.findById(invalidId)).isEmpty();
    }
}
