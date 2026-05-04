package roomescape.domain.time.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.time.dto.request.TimeCreateRequestDTO;
import roomescape.domain.time.dto.response.TimeResponseDTO;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.FakeTimeRepository;
import roomescape.domain.time.repository.TimeRepository;

class TimeServiceTest {

    private final TimeService timeService;
    private final TimeRepository timeRepository;

    TimeServiceTest() {
        this.timeRepository = new FakeTimeRepository();
        this.timeService = new TimeService(timeRepository);
    }

    @Nested
    class GetTimesTest {

        @Test
        void 성공() {

            // given
            LocalTime startAt = LocalTime.of(10, 0);

            timeRepository.save(Time.create(startAt));
            timeRepository.save(Time.create(startAt.plusHours(1)));
            timeRepository.save(Time.create(startAt.plusHours(2)));

            // when
            List<TimeResponseDTO> actual = timeService.getTimes();

            // then
            assertAll(
                () -> assertEquals(3, actual.size()),
                () -> assertEquals(new TimeResponseDTO(1L, startAt), actual.get(0)),
                () -> assertEquals(new TimeResponseDTO(2L, startAt.plusHours(1)), actual.get(1)),
                () -> assertEquals(new TimeResponseDTO(3L, startAt.plusHours(2)), actual.get(2))
            );
        }
    }

    @Nested
    class SaveTimeTest {

        @Test
        void 성공() {

            // given
            TimeCreateRequestDTO request = new TimeCreateRequestDTO(LocalTime.of(15, 30));

            // when
            TimeResponseDTO actual = timeService.saveTime(request);

            // then
            assertAll(
                () -> assertEquals(1L, actual.id()),
                () -> assertEquals(LocalTime.of(15, 30), actual.startAt()),
                () -> assertEquals(List.of(actual), timeService.getTimes())
            );
        }
    }

    @Nested
    class DeleteTimeByIdTest {

        @Test
        void 성공() {

            // given
            Time savedTime = timeRepository.save(Time.create(LocalTime.of(12, 0)));
            timeRepository.save(Time.create(LocalTime.of(13, 0)));

            // when
            timeService.deleteTimeById(savedTime.getId());

            // then
            List<TimeResponseDTO> actual = timeService.getTimes();
            assertAll(
                () -> assertEquals(1, actual.size()),
                () -> assertEquals(LocalTime.of(13, 0), actual.getFirst().startAt())
            );
        }
    }
}
