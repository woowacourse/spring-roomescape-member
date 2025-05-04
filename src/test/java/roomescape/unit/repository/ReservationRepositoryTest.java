package roomescape.unit.repository;

import static org.assertj.core.api.Assertions.*;
import static roomescape.common.Constant.FIXED_CLOCK;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.common.RepositoryBaseTest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.reservation.domain.ReserverName;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;
import roomescape.unit.fixture.ReservationDateFixture;
import roomescape.unit.fixture.ReservationDbFixture;
import roomescape.unit.fixture.ReservationTimeDbFixture;
import roomescape.unit.fixture.ReserverNameFixture;
import roomescape.unit.fixture.ThemeDbFixture;

public class ReservationRepositoryTest extends RepositoryBaseTest {

    private static final String SELECT_RESERVATION_BY_ID = "SELECT * FROM reservation WHERE id = ?";
    private static final String SELECT_ALL_RESERVATIONS = "SELECT * FROM reservation";
    private static final String COUNT_RESERVATION_BY_ID = "SELECT COUNT(*) FROM reservation WHERE id = ?";

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationDbFixture reservationDbFixture;

    @Autowired
    private ReservationTimeDbFixture reservationTimeDbFixture;

    @Autowired
    private ThemeDbFixture themeDbFixture;

    @Test
    void 예약을_저장할_수_있다() {
        // given
        ReservationDate date = ReservationDateFixture.예약날짜_오늘;
        ReservationTime time = reservationTimeDbFixture.예약시간_10시();
        Theme theme = themeDbFixture.공포();
        ReserverName name = ReserverNameFixture.한스;
        ReservationDateTime dateTime = new ReservationDateTime(date, time, FIXED_CLOCK);

        // when
        Reservation saved = reservationRepository.save(name, dateTime, theme);

        // then
        Map<String, Object> row = jdbcTemplate.queryForMap(SELECT_RESERVATION_BY_ID, saved.getId());
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(row.get("name")).isEqualTo("한스");
        softly.assertThat(row.get("date")).isEqualTo(date.getDate().toString());
        softly.assertThat(row.get("time_id")).isEqualTo(time.getId());
        softly.assertThat(row.get("theme_id")).isEqualTo(theme.getId());
        softly.assertAll();
    }

    @Test
    void 예약을_모두_조회할_수_있다() {
        // given
        Reservation 예약1 = reservationDbFixture.예약_생성_한스(
                ReservationDateFixture.예약날짜_오늘,
                reservationTimeDbFixture.예약시간_10시(),
                themeDbFixture.공포()
        );

        Reservation 예약2 = reservationDbFixture.예약_생성_한스(
                ReservationDateFixture.예약날짜_7일전,
                reservationTimeDbFixture.예약시간_11시(),
                themeDbFixture.로맨스()
        );

        // when
        List<Reservation> reservations = reservationRepository.findAll();
        List<Map<String, Object>> result = jdbcTemplate.queryForList(SELECT_ALL_RESERVATIONS);

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(reservations).hasSize(2);
        softly.assertThat(result).hasSize(2);

        softly.assertThat(result).anySatisfy(row -> {
            SoftAssertions nested = new SoftAssertions();
            nested.assertThat(row.get("name")).isEqualTo("한스");
            nested.assertThat(row.get("date")).isEqualTo(예약1.getDate().toString());
            nested.assertThat(row.get("time_id")).isEqualTo(예약1.getTimeId());
            nested.assertThat(row.get("theme_id")).isEqualTo(예약1.getTheme().getId());
            nested.assertAll();
        });

        softly.assertThat(result).anySatisfy(row -> {
            SoftAssertions nested = new SoftAssertions();
            nested.assertThat(row.get("name")).isEqualTo("한스");
            nested.assertThat(row.get("date")).isEqualTo(예약2.getDate().toString());
            nested.assertThat(row.get("time_id")).isEqualTo(예약2.getTimeId());
            nested.assertThat(row.get("theme_id")).isEqualTo(예약2.getTheme().getId());
            nested.assertAll();
        });

        softly.assertAll();
    }

    @Test
    void ID로_예약을_조회할_수_있다() {
        // given
        Reservation 예약 = reservationDbFixture.예약_생성_한스(
                ReservationDateFixture.예약날짜_오늘,
                reservationTimeDbFixture.예약시간_10시(),
                themeDbFixture.공포()
        );

        // when
        Optional<Reservation> found = reservationRepository.findById(예약.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getReserverName()).isEqualTo("한스");
    }

    @Test
    void 예약을_삭제할_수_있다() {
        // given
        Reservation 예약 = reservationDbFixture.예약_생성_한스(
                ReservationDateFixture.예약날짜_오늘,
                reservationTimeDbFixture.예약시간_10시(),
                themeDbFixture.공포()
        );

        // when
        reservationRepository.deleteById(예약.getId());

        // then
        Long count = jdbcTemplate.queryForObject(COUNT_RESERVATION_BY_ID, Long.class, 예약.getId());
        assertThat(count).isZero();
    }

    @Test
    void 동일한_시간에_예약이_존재하는지_확인할_수_있다() {
        // given
        ReservationDate date = ReservationDateFixture.예약날짜_오늘;
        ReservationTime time = reservationTimeDbFixture.예약시간_10시();
        reservationDbFixture.예약_생성_한스(date, time, themeDbFixture.공포());

        // when
        boolean exists = reservationRepository.existSameDateTime(date, time.getId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void 동일한_시간에_예약이_없으면_false를_반환한다() {
        // given
        ReservationDate date = ReservationDateFixture.예약날짜_오늘;
        ReservationTime time = reservationTimeDbFixture.예약시간_10시();

        // when
        boolean exists = reservationRepository.existSameDateTime(date, time.getId());

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void 특정_시간에_예약이_있는지_확인할_수_있다() {
        // given
        ReservationTime time = reservationTimeDbFixture.예약시간_10시();
        reservationDbFixture.예약_생성_한스(
                ReservationDateFixture.예약날짜_오늘, time, themeDbFixture.공포()
        );

        // when
        boolean exists = reservationRepository.existReservationByTimeId(time.getId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void 특정_시간에_예약이_없으면_false를_반환한다() {
        // given
        ReservationTime time = reservationTimeDbFixture.예약시간_10시();

        // when
        boolean exists = reservationRepository.existReservationByTimeId(time.getId());

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void 특정_테마에_예약이_있는지_확인할_수_있다() {
        // given
        Theme theme = themeDbFixture.공포();
        reservationDbFixture.예약_생성_한스(
                ReservationDateFixture.예약날짜_오늘, reservationTimeDbFixture.예약시간_10시(), theme
        );

        // when
        boolean exists = reservationRepository.existReservationByThemeId(theme.getId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void 특정_테마에_예약이_없으면_false를_반환한다() {
        // given
        Theme theme = themeDbFixture.공포();

        // when
        boolean exists = reservationRepository.existReservationByThemeId(theme.getId());

        // then
        assertThat(exists).isFalse();
    }
}
