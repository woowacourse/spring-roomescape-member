//package roomescape.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import roomescape.domain.Reservation;
//import roomescape.domain.ReservationTime;
//import roomescape.dto.reservationtime.ReservationTimeRequest;
//import roomescape.dto.reservationtime.ReservationTimeResponse;
//import roomescape.exception.reservationtime.ReservationTimeAlreadyExistsException;
//import roomescape.exception.reservationtime.ReservationTimeNotFoundException;
//import roomescape.exception.reservationtime.UsingReservationTimeException;
//import roomescape.fixture.ReservationRepositoryStub;
//import roomescape.fixture.ReservationTimeRepositoryStub;
//import roomescape.repository.reservation.ReservationRepository;
//
//class ReservationTimeServiceTest {
//    private ReservationTimeService timeService;
//    private ReservationRepository reservationRepository;
//
//    @BeforeEach
//    void setUp() {
//        reservationRepository = new ReservationRepositoryStub();
//        this.timeService = new ReservationTimeService(
//                new ReservationTimeRepositoryStub(),
//                reservationRepository);
//    }
//
//    @DisplayName("예약 시간을 생성한다")
//    @Test
//    void
//    create() {
//        // given
//        LocalTime now = LocalTime.now();
//        ReservationTimeRequest request = new ReservationTimeRequest(now);
//        Long fakeId = 1L;
//        ReservationTime savedTime = new ReservationTime(fakeId, now);
//
//        // when
//        ReservationTimeResponse result = timeService.create(request);
//
//        // then
//        assertThat(result.getId()).isEqualTo(savedTime.getId());
//        assertThat(result.getStartAt()).isEqualTo(savedTime.getStartAt());
//    }
//
//    @DisplayName("전체 예약 시간을 조회한다")
//    @Test
//    void getAll() {
//        // given
//        LocalTime time1 = LocalTime.now();
//        LocalTime time2 = LocalTime.now().plusHours(1);
//
//        ReservationTimeRequest request1 = new ReservationTimeRequest(time1);
//        ReservationTimeRequest request2 = new ReservationTimeRequest(time2);
//
//        timeService.create(request1);
//        timeService.create(request2);
//
//        // when
//        List<ReservationTimeResponse> responses = timeService.getAll();
//
//        // then
//        assertThat(responses).hasSize(2);
//    }
//
//    @DisplayName("존재하지 않는 id의 예약시간 삭제시 예외를 발생시킨다")
//    @Test
//    void deleteById() {
//        // given
//        Long id = 99L;
//
//        // when // then
//        assertThatThrownBy(() -> timeService.deleteById(id))
//                .isInstanceOf(ReservationTimeNotFoundException.class);
//    }
//
//    @DisplayName("이미 있는 예약시간 추가시 예외를 발생시킨다")
//    @Test
//    void alreadyExists() {
//        //given
//        LocalTime now = LocalTime.parse("10:10");
//        timeService.create(new ReservationTimeRequest(now));
//
//        //when
//        ReservationTimeRequest request = new ReservationTimeRequest(now);
//
//        //then
//        assertThatThrownBy(() -> timeService.create(request))
//                .isInstanceOf(ReservationTimeAlreadyExistsException.class);
//    }
//
//    @DisplayName("특정 시간에 대한 예약이 존재한다면 그 시간은 삭제할 수 없다")
//    @Test
//    void deleteUsingReservationTime() {
//        // given
//        LocalTime now = LocalTime.now();
//        ReservationTimeRequest request = new ReservationTimeRequest(now);
//        Long fakeId = 1L;
//        ReservationTime savedTime = new ReservationTime(fakeId, now);
//        timeService.create(request);
//
//        reservationRepository.add(new Reservation("에드", LocalDate.now().plusDays(1), savedTime));
//
//        // when
//        assertThatThrownBy(() -> timeService.deleteById(fakeId))
//                .isInstanceOf(UsingReservationTimeException.class);
//    }
//
//}
