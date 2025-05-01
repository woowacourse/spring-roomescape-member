package roomescape.domain.service;


public class ReservationServiceTest {
/*
    private ReservationService reservationService;
    private ReservationDao reservationDao;
    private ReservationTimeDao reservationTimeDao;

    @BeforeEach
    void init() {
        reservationDao = new FakeReservationDaoImpl();
        reservationTimeDao = new FakeReservationTimeDaoImpl();
        reservationService = new ReservationService(reservationDao, reservationTimeDao);
    }

    @DisplayName("ReservationRequestDto가 주어졌을 때, Fake 객체에 정상적으로 저장되어야 한다.")
    @Test
    void given_reservation_request_dto_then_save() {
        //given
        reservationTimeDao.saveReservationTime(new ReservationTime(1L, LocalTime.of(10, 30)));
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(
            "james", "2025-05-12", 1L);

        //when
        reservationService.saveReservation(reservationRequestDto);

        //then
        assertThat(reservationService.getAllReservations().size()).isEqualTo(1);
    }

    @DisplayName("여러 번 Reservation을 저장할 때, 성공적으로 Fake객체에 저장되고, 읽어올 수 있어야 한다.")
    @Test
    void given_multiple_reservation_request_dto_then_all_save() {
        reservationTimeDao.saveReservationTime(new ReservationTime(1L, LocalTime.of(10, 30)));
        reservationTimeDao.saveReservationTime(new ReservationTime(2L, LocalTime.of(11, 30)));
        reservationTimeDao.saveReservationTime(new ReservationTime(3L, LocalTime.of(12, 30)));

        //given
        ReservationRequestDto reservationRequestDto1 = new ReservationRequestDto(
            "james", "2025-05-12", 1L);
        ReservationRequestDto reservationRequestDto2 = new ReservationRequestDto(
            "james", "2025-05-13", 2L);
        ReservationRequestDto reservationRequestDto3 = new ReservationRequestDto(
            "james", "2025-05-14", 2L);

        //when
        reservationService.saveReservation(reservationRequestDto1);
        reservationService.saveReservation(reservationRequestDto2);
        reservationService.saveReservation(reservationRequestDto3);

        //then
        assertThat(reservationService.getAllReservations().size()).isEqualTo(3);
    }

    @DisplayName("reservationId가 주어졌을 떄, Fake 객체에서 삭제되어야 한다.")
    @Test
    void given_reservation_id_then_remove_db() {
        //given
        reservationTimeDao.saveReservationTime(new ReservationTime(1L, LocalTime.of(10, 30)));
        
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(
            "james", "2025-05-12", 1L);
        reservationService.saveReservation(reservationRequestDto);

        //when
        reservationService.deleteReservation(1L);

        //then
        assertThat(reservationService.getAllReservations().size()).isEqualTo(0);
    }

 */
}
