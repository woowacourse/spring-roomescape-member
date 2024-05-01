package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
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
        reservationService = new ReservationService(reservationDao, reservationTimeDao, roomThemeDao);
    }

    @DisplayName("모든 예약 검색")
    @Test
    void findAll() {
        assertThat(reservationService.findAll()).isEmpty();
    }

    @DisplayName("예약 저장")
    @Test
    void save() {
        //given
        ReservationTime savedReservationTime = reservationTimeDao.save(new ReservationTime(LocalTime.parse("10:00")));
        RoomTheme savedRoomTheme = roomThemeDao.save(new RoomTheme("레벨 2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        ReservationRequest reservationRequest = new ReservationRequest("aa", "2024-10-10", savedReservationTime.getId(), savedRoomTheme.getId());
        //when
        ReservationResponse response = reservationService.save(reservationRequest);
        //then
        assertAll(
                () -> assertThat(reservationService.findAll()).hasSize(1),
                () -> assertThat(response.id()).isEqualTo(1),
                () -> assertThat(response.name()).isEqualTo("aa"),
                () -> assertThat(response.date()).isEqualTo("2024-10-10"),
                () -> assertThat(response.time().id()).isEqualTo(1),
                () -> assertThat(response.time().startAt()).isEqualTo("10:00")
        );
    }

    @DisplayName("삭제 테스트")
    @Test
    void deleteById() {
        //given
        ReservationTime savedReservationTime = reservationTimeDao.save(new ReservationTime(LocalTime.parse("10:00")));
        RoomTheme savedRoomTheme = roomThemeDao.save(new RoomTheme("레벨 2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        ReservationRequest reservationRequest = new ReservationRequest("aa", "2024-10-10", savedReservationTime.getId(), savedRoomTheme.getId());
        //when
        reservationService.deleteById(1);
        //then
        assertThat(reservationService.findAll()).isEmpty();
    }
}
