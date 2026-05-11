package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.dto.reservationTime.CreateReservationTimeRequest;
import roomescape.dto.reservationTime.ReservationTimeResponses;
import roomescape.repository.fake.FakeReservationTimeRepository;

class ReservationTimeServiceTest {

    private FakeReservationTimeRepository repository;
    private ReservationTimeService service;

    @BeforeEach
    void setUp() {
        repository = new FakeReservationTimeRepository();
        service = new ReservationTimeService(repository);
    }

    @Test
    void createReservationTime_저장된_시간을_id와_함께_반환한다() {
        CreateReservationTimeRequest request = new CreateReservationTimeRequest(LocalTime.of(10, 0));

        ReservationTime created = service.createReservationTime(request);

        assertThat(created.getId()).isPositive();
        assertThat(created.getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void getReservationTime_id로_단건을_조회한다() {
        Long id = repository.save(new ReservationTime(null, LocalTime.of(10, 30)));

        ReservationTime found = service.getReservationTime(id);

        assertThat(found.getId()).isEqualTo(id);
        assertThat(found.getStartAt()).isEqualTo(LocalTime.of(10, 30));
    }

    @Test
    void getReservationTimes_다음_페이지가_있으면_hasNext가_true() {
        repository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        repository.save(new ReservationTime(null, LocalTime.of(11, 0)));
        repository.save(new ReservationTime(null, LocalTime.of(12, 0)));

        ReservationTimeResponses responses = service.getReservationTimes(0, 2);

        assertThat(responses.times()).hasSize(2);
        assertThat(responses.hasNext()).isTrue();
    }

    @Test
    void getReservationTimes_다음_페이지가_없으면_hasNext가_false() {
        repository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        repository.save(new ReservationTime(null, LocalTime.of(11, 0)));

        ReservationTimeResponses responses = service.getReservationTimes(0, 2);

        assertThat(responses.times()).hasSize(2);
        assertThat(responses.hasNext()).isFalse();
    }

    @Test
    void deleteReservationTime_삭제후_조회되지_않는다() {
        Long id = repository.save(new ReservationTime(null, LocalTime.of(10, 0)));

        service.deleteReservationTime(id);

        ReservationTimeResponses responses = service.getReservationTimes(0, 10);
        assertThat(responses.times()).extracting("id").doesNotContain(id);
    }
}