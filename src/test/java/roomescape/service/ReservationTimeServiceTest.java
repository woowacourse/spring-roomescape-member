package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.domain.ReservationTime;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.service.dto.request.CreateReservationTimeServiceRequest;
import roomescape.service.dto.response.ReservationTimeServiceResponse;

class ReservationTimeServiceTest {

    private FakeReservationTimeRepository fakeRepository = new FakeReservationTimeRepository();
    private ReservationTimeService service = new ReservationTimeService(fakeRepository);

    @BeforeEach
    void setUp() {
        FakeReservationTimeRepository.clear();
    }

    @DisplayName("예약 시간을 저장소에 추가하고 저장된 정보를 가진 객체를 반환한다.")
    @Test
    void create() {
        // given
        LocalTime startAt = LocalTime.now().plusHours(1);
        CreateReservationTimeServiceRequest command = new CreateReservationTimeServiceRequest(startAt);
        // when
        ReservationTimeServiceResponse reservationTimeServiceResponse = service.create(command);

        // then
        assertSoftly(softly -> {
            assertThat(reservationTimeServiceResponse.id()).isEqualTo(1);
            assertThat(reservationTimeServiceResponse.startAt()).isEqualTo(startAt);
            assertThat(fakeRepository.getAll()).hasSize(1);
        });
    }

    @DisplayName("저장된 모든 객체의 정보를 가진 객체를 반환한다.")
    @Test
    void getAll() {
        // given
        LocalTime startAt = LocalTime.now().plusHours(1);
        fakeRepository.save(new ReservationTime(startAt));

        // when
        List<ReservationTimeServiceResponse> queries = service.getAll();

        // then
        assertThat(queries).hasSize(1);
    }

    @DisplayName("예약 시간을 저장소에서 삭제한다")
    @Test
    void delete() {
        // given
        LocalTime startAt = LocalTime.now().plusHours(1);
        ReservationTime savedReservationTime = fakeRepository.save(new ReservationTime(startAt));
        // when
        service.delete(savedReservationTime.getId());
        // then
        assertThat(fakeRepository.getAll()).hasSize(0);
    }

    @DisplayName("예약 시간이 없는 id로 삭제를 시도하면 예외를 발생시킨다")
    @Test
    void deleteException() {
        // given
        LocalTime startAt = LocalTime.now().plusHours(1);
        ReservationTime savedReservationTime = fakeRepository.save(new ReservationTime(startAt));

        // when & then
        assertThatThrownBy(() -> service.delete(2L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
