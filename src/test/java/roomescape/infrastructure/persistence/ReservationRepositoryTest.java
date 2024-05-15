package roomescape.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.infrastructure.persistence.dynamic.ReservationQueryConditions;
import roomescape.support.IntegrationTestSupport;

class ReservationRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ReservationRepository target;

    @Test
    @DisplayName("모든 예약 데이터를 가져온다.")
    void findAllBy() {
        List<Reservation> reservations = target.findAllBy(ReservationQueryConditions.noneConditions());

        assertThat(reservations).hasSize(3);
    }

    @Test
    @DisplayName("예약 데이터가 존재하지 않으면 빈 리스트를 반환한다.")
    void empty() {
        cleanUp("reservation");

        List<Reservation> reservations = target.findAllBy(ReservationQueryConditions.noneConditions());

        assertThat(reservations).isEmpty();
    }

    @Test
    @DisplayName("특정 테마 id를 사용하고 있는 예약 데이터가 존재하는지 검증한다.")
    void hasByThemeId() {
        ReservationQueryConditions conditions = ReservationQueryConditions.builder()
                .themeId(테마_1번_ID)
                .build();

        boolean result = target.hasBy(conditions);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("특정 테마 id를 사용하고 있는 예약 데이터가 존재하지 않는지 검증한다.")
    void noHasByThemeId() {
        ReservationQueryConditions conditions = ReservationQueryConditions.builder()
                .themeId(테마_2번_ID)
                .build();

        boolean result = target.hasBy(conditions);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("특정 시간 id를 사용하고 있는 예약 데이터가 존재하는지 검증한다.")
    void hasByTimeId() {
        ReservationQueryConditions conditions = ReservationQueryConditions.builder()
                .timeId(예약_시간_1번_ID)
                .build();

        boolean result = target.hasBy(conditions);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("특정 시간 id를 사용하고 있는 예약 데이터가 존재하지 않는지 검증한다.")
    void noHasByTimeId() {
        ReservationQueryConditions conditions = ReservationQueryConditions.builder()
                .timeId(0L)
                .build();

        boolean result = target.hasBy(conditions);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("새로운 예약 데이터를 저장한다.")
    void create() {
        Reservation reservation = createReservation(
                "2023-08-05",
                멤버_1번_어드민_ID,
                예약_시간_1번_ID,
                테마_1번_ID
        );

        target.save(reservation);

        int countRow = countRow("reservation");
        assertThat(countRow).isEqualTo(4);
    }

    @Test
    @DisplayName("특정 id를 가진 예약을 삭제한다.")
    void remove() {
        target.removeById(예약_2번_ID);

        int countRow = countRow("reservation");

        assertThat(countRow).isEqualTo(2);
    }

    @Test
    @DisplayName("동일한 날짜, 시간, 테마의 예약이 있는지 확인한다.")
    void hasDuplicateDateTimeThemeReservation() {
        ReservationQueryConditions conditions = ReservationQueryConditions.builder()
                .date("2023-05-04")
                .timeId(예약_시간_1번_ID)
                .themeId(테마_1번_ID)
                .build();

        boolean result = target.hasBy(conditions);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("동일한 날짜, 시간, 테마의 예약이 없는지 확인한다.")
    void noHasDuplicateDateTimeThemeReservation() {
        ReservationQueryConditions conditions = ReservationQueryConditions.builder()
                .date("2023-06-04")
                .memberId(멤버_1번_어드민_ID)
                .timeId(예약_시간_2번_ID)
                .themeId(테마_1번_ID)
                .build();

        boolean result = target.hasBy(conditions);

        assertThat(result).isFalse();
    }

    private Reservation createReservation(String dateValue, Long memberId, Long timeId, Long themeId) {
        LocalDate date = LocalDate.parse(dateValue);
        Member member = new Member(memberId, null, null, null, null);
        ReservationTime reservationTime = new ReservationTime(timeId, null);
        Theme theme = new Theme(themeId, null, null, null);

        return new Reservation(date, member, reservationTime, theme);
    }
}
