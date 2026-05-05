//package roomescape.service;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//import roomescape.dto.ReservationRequest;
//import roomescape.dto.ReservationResponse;
//import roomescape.dto.ReservationsResponse;
//import roomescape.model.ReservationTime;
//import roomescape.repository.ReservationTimeRepository;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//class ReservationServiceTest {
//
//    @Autowired
//    private ReservationService reservationService;
//
//    @Autowired
//    private ReservationTimeRepository reservationTimeRepository;
//
//    @Test
//    @DisplayName("실제 DB에 예약 시간을 먼저 저장한 후, 예약을 생성한다.")
//    void create() {
//        // given
//        ReservationTime time = new ReservationTime(LocalTime.of(10, 0));
//        Long timeId = reservationTimeRepository.create(time);
//
//        ReservationRequest request = new ReservationRequest("브라운", LocalDate.of(2026, 12, 25), timeId);
//
//        // when
//        ReservationResponse response = reservationService.create(request);
//
//        // then
//        assertThat(response.getId()).isNotNull();
//        assertThat(response.getName()).isEqualTo("브라운");
//
//        ReservationsResponse all = reservationService.findAll();
//        assertThat(all.getReservationsResponse()).anyMatch(res -> res.getName().equals("브라운"));
//    }
//
//    @Test
//    @DisplayName("DB에 저장된 모든 예약을 조회한다.")
//    void findAll() {
//        // given
//        ReservationTime time = new ReservationTime(LocalTime.of(13, 0));
//        Long timeId = reservationTimeRepository.create(time);
//
//        reservationService.create(new ReservationRequest("브라운", LocalDate.of(2026, 12, 25), timeId));
//        reservationService.create(new ReservationRequest("루크", LocalDate.of(2026, 12, 26), timeId));
//
//        // when
//        ReservationsResponse response = reservationService.findAll();
//
//        // then
//        assertThat(response.getReservationsResponse()).hasSize(2);
//        assertThat(response.getReservationsResponse())
//                .extracting("name")
//                .containsExactlyInAnyOrder("브라운", "루크");
//    }
//
//    @Test
//    @DisplayName("ID를 이용해 실제 DB에서 예약을 삭제한다.")
//    void delete() {
//        // given
//        ReservationTime time = new ReservationTime(LocalTime.of(15, 0));
//        Long timeId = reservationTimeRepository.create(time);
//        ReservationResponse saved = reservationService.create(new ReservationRequest("포비", LocalDate.of(2026, 12, 27), timeId));
//
//        long reservationId = saved.getId();
//
//        // when
//        int result = reservationService.delete(reservationId);
//
//        // then
//        assertThat(result).isEqualTo(1);
//
//        ReservationsResponse remaining = reservationService.findAll();
//        assertThat(remaining.getReservationsResponse())
//                .extracting("id")
//                .doesNotContain(reservationId);
//    }
//}
