package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.exception.reservation.ReservationAlreadyExistsException;
import roomescape.exception.reservation.ReservationNotFoundException;
import roomescape.fixture.ReservationRepositoryStub;
import roomescape.fixture.ReservationTimeServiceStub;

class ReservationServiceTest {
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(
                new ReservationRepositoryStub(),
                new ReservationTimeServiceStub()
        );
    }

    @DisplayName("예약을 생성한다")
    @Test
    void create() {
        ReservationRequest request = new ReservationRequest("브라운", LocalDate.now(), 1L);

        ReservationResponse response = reservationService.create(request);

        assertThat(response.getName()).isEqualTo("브라운");
        assertThat(response.getId()).isEqualTo(1L);
    }

    @DisplayName("전체 예약 목록을 조회한다")
    @Test
    void getAll() {
        reservationService.create(new ReservationRequest("A", LocalDate.now(), 1L));
        reservationService.create(new ReservationRequest("B", LocalDate.now(), 1L));

        List<ReservationResponse> responses = reservationService.getAll();

        assertThat(responses).hasSize(2);
    }

    @DisplayName("존재하지 않는 id의 예약 삭제 시 예외가 발생한다")
    @Test
    void deleteById() {
        assertThatThrownBy(() -> reservationService.deleteById(99L))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @DisplayName("이미 같은 날짜, 시간에 예약이 존재할 시 예약을 생성하면 예외가 발생한다")
    @Test
    void alreadyExists() {
        // given
        LocalDate now = LocalDate.now();
        ReservationRequest request = new ReservationRequest("브라운", now, 1L);
        reservationService.create(request);

        // when // then
        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(ReservationAlreadyExistsException.class);
    }
}
