package roomescape.service;


import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

public class ReservationServiceTest {

    private ReservationDao reservationDao;
    private ReservationRepository reservationRepository;
    private ThemeDao themeDao;
    private ThemeRepository themeRepository;
    private ReservationTimeDao reservationTimeDao;
    private ReservationTimeRepository reservationTimeRepository;
    private ReservationService reservationService;
/*

    @BeforeEach
    void init() {
        reservationDao = new FakeReservationDaoImpl();
        reservationRepository = new ReservationRepositoryImpl(reservationDao);
        themeDao = new FakeThemeDaoImpl();
        themeRepository = new ThemeRepositoryImpl(themeDao, reservationDao);
        reservationTimeDao = new FakeReservationTimeDaoImpl();
        reservationTimeRepository = new ReservationTimeRepositoryImpl(reservationTimeDao,
            reservationDao);
        reservationService = new ReservationService(
            reservationRepository, reservationTimeRepository, themeRepository, new TestTime());
    }

    @DisplayName("이미 존재하는 날짜와 시간에 예약하려고 하면, 예외가 발생해야 한다.")
    @Test
    void already_exist_date_time_save_reservation_then_throw_exception() {
        //given
        ReservationDate reservationDate = new ReservationDate(LocalDate.of(2025, 5, 5));
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포", "공포테마입니다", "http://aaa");

        long savedThemeId = themeDao.save(theme);
        theme.setId(savedThemeId);

        long savedReservationTimeId = reservationTimeDao.save(reservationTime);
        reservationTime.setId(savedReservationTimeId);

        Reservation reservation = new Reservation(
            new Member("젠슨"),
            reservationDate,
            reservationTime,
            theme);
        reservationRepository.save(reservation);
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(
            "젠슨",
            "2025-05-05",
            savedThemeId,
            savedThemeId
        );

        //when, then
        assertThatThrownBy(() -> reservationService.saveReservation(reservationRequestDto))
            .isInstanceOf(InvalidReservationException.class)
            .hasMessage("중복된 날짜와 시간을 예약할 수 없습니다.");
    }

 */
}
