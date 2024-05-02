package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.RESERVATION_TIME_FIXTURE;
import static roomescape.TestFixture.ROOM_THEME_FIXTURE;
import static roomescape.TestFixture.VALID_STRING_DATE_FIXTURE;
import static roomescape.TestFixture.VALID_STRING_TIME_FIXTURE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.console.dao.InMemoryReservationDao;
import roomescape.console.dao.InMemoryReservationTimeDao;
import roomescape.console.dao.InMemoryRoomThemeDao;
import roomescape.console.db.InMemoryReservationDb;
import roomescape.console.db.InMemoryReservationTimeDb;
import roomescape.console.db.InMemoryRoomThemeDb;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.RoomThemeDao;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;

class ReservationServiceTest {
    private ReservationService reservationService;
    private ReservationTimeDao reservationTimeDao;
    private RoomThemeDao roomThemeDao;

    @BeforeEach
    void setUp() {
        InMemoryReservationDb inMemoryReservationDb = new InMemoryReservationDb();
        InMemoryReservationTimeDb inMemoryReservationTimeDb = new InMemoryReservationTimeDb();
        InMemoryRoomThemeDb inMemoryRoomThemeDb = new InMemoryRoomThemeDb();

        reservationTimeDao = new InMemoryReservationTimeDao(
                inMemoryReservationDb, inMemoryReservationTimeDb);
        ReservationDao reservationDao = new InMemoryReservationDao(inMemoryReservationDb);
        roomThemeDao = new InMemoryRoomThemeDao(inMemoryRoomThemeDb);

        reservationService = new ReservationService(reservationDao, reservationTimeDao,
                roomThemeDao);
    }

    @DisplayName("모든 예약 검색")
    @Test
    void findAll() {
        assertThat(reservationService.findAll()).isEmpty();
    }

    @DisplayName("예약 저장")
    @Test
    void save() {
        // given
        ReservationRequest reservationRequest = createReservationRequest(VALID_STRING_DATE_FIXTURE);
        // when
        ReservationResponse response = reservationService.save(reservationRequest);
        // then
        assertAll(
                () -> assertThat(reservationService.findAll()).hasSize(1),
                () -> assertThat(response.id()).isEqualTo(1),
                () -> assertThat(response.name()).isEqualTo("aa"),
                () -> assertThat(response.date()).isEqualTo(VALID_STRING_DATE_FIXTURE),
                () -> assertThat(response.time().id()).isEqualTo(1),
                () -> assertThat(response.time().startAt()).isEqualTo(VALID_STRING_TIME_FIXTURE)
        );
    }

    @DisplayName("지난 예약을 저장하려 하면 예외가 발생한다.")
    @Test
    void saveWithException() {
        // given
        ReservationRequest reservationRequest = createReservationRequest("2000-11-09");
        // when&then
        assertThatThrownBy(() -> reservationService.save(reservationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지나간 날짜입니다.");
    }

    @DisplayName("삭제 테스트")
    @Test
    void deleteById() {
        // given
        createReservationRequest(VALID_STRING_DATE_FIXTURE);
        // when
        reservationService.deleteById(1);
        // then
        assertThat(reservationService.findAll()).isEmpty();
    }

    private ReservationRequest createReservationRequest(String date) {
        ReservationTime savedReservationTime = reservationTimeDao.save(
                RESERVATION_TIME_FIXTURE);
        RoomTheme savedRoomTheme = roomThemeDao.save(ROOM_THEME_FIXTURE);
        return new ReservationRequest("aa", date,
                savedReservationTime.getId(), savedRoomTheme.getId());
    }
}
