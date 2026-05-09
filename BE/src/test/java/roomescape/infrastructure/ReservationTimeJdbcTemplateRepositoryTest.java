package roomescape.infrastructure;


import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.ReservationTime;

@JdbcTest
@Import(ReservationTimeJdbcTemplateRepository.class)
class ReservationTimeJdbcTemplateRepositoryTest {

    private final ReservationTimeJdbcTemplateRepository timeRepository;

    @Autowired
    ReservationTimeJdbcTemplateRepositoryTest(ReservationTimeJdbcTemplateRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    @Test
    @DisplayName("시간 저장을 잘 한다")
    void save_success() {
        //given
        ReservationTime testReservationTime = ReservationTime.createWithNullId(
                LocalTime.now()
        );

        //when
        ReservationTime result = timeRepository.save(testReservationTime);

        //then
        Assertions.assertNotNull(result.id());
    }

    @Test
    @DisplayName("id에 맞는 시간이 존재하면 id 로 잘 찾아온다.")
    void findById_success() {
        //given
        ReservationTime time1 = ReservationTime.createWithNullId(LocalTime.of(12, 0));
        ReservationTime time2 = ReservationTime.createWithNullId(LocalTime.of(13, 0));
        timeRepository.save(time1);
        ReservationTime saved2 = timeRepository.save(time2);

        //when
        Optional<ReservationTime> result = timeRepository.findById(saved2.id());

        //then
        Assertions.assertTrue(result.isPresent());

        LocalTime foundTime = result.get().startAt();
        Assertions.assertEquals(foundTime, saved2.startAt());
    }

    @Test
    @DisplayName("id에 맞는 시간이 존재하지 않으면, Optional empty를 반환한다.")
    void findById_success_but_return_empty_value() {
        //when
        long notExistTimeId = 999L;
        Optional<ReservationTime> result = timeRepository.findById(notExistTimeId);

        //then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("저장되어 있는 모든 시간을 잘 가져온다")
    void findAll_success() {
        //given
        ReservationTime time1 = ReservationTime.createWithNullId(LocalTime.of(11, 0));
        ReservationTime time2 = ReservationTime.createWithNullId(LocalTime.of(12, 0));
        ReservationTime time3 = ReservationTime.createWithNullId(LocalTime.of(13, 0));

        timeRepository.save(time1);
        timeRepository.save(time2);
        timeRepository.save(time3);

        //when
        List<ReservationTime> result = timeRepository.findAll();

        //then
        Assertions.assertEquals(3, result.size());
    }

    @Test
    @DisplayName("삭제를 id 기반으로 잘 한다")
    void deleteById_success() {
        //given
        ReservationTime time = ReservationTime.createWithNullId(LocalTime.of(11, 0));
        ReservationTime saved = timeRepository.save(time);

        //when
        Long deleteTargetId = saved.id();
        timeRepository.deleteById(deleteTargetId);

        //then
        Optional<ReservationTime> deleteTargetFindResult = timeRepository.findAll()
                .stream()
                .filter(reservationTime -> reservationTime.id().equals(deleteTargetId))
                .findAny();
        Assertions.assertTrue(deleteTargetFindResult.isEmpty());
    }
}
