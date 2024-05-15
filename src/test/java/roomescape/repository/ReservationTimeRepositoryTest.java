package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Import(H2ReservationTimeRepository.class)
@JdbcTest
class ReservationTimeRepositoryTest {

    final List<ReservationTime> sampleTimes = List.of(
            new ReservationTime(null, "08:00"),
            new ReservationTime(null, "09:10"),
            new ReservationTime(null, "11:30"),
            new ReservationTime(null, "10:20")
    );

    @Autowired
    ReservationTimeRepository timeRepository;

    @Test
    @DisplayName("모든 예약 시간 목록을 조회한다.")
    void findAllByOrderByStartAt() {
        // given
        sampleTimes.forEach(timeRepository::save);

        // when
        List<ReservationTime> actual = timeRepository.findAllByOrderByStartAt();
        List<ReservationTime> expected = sampleTimes.stream()
                .map(time -> time.assignId(
                        actual.stream()
                                .filter(t -> t.getStartAt().equals(time.getStartAt()))
                                .findAny()
                                .orElseThrow()
                                .getId()
                )).sorted(Comparator.comparing(ReservationTime::getStartAt))
                .toList();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("특정 id를 통해 예약 시간 데이터를 조회한다.")
    void findByIdPresent() {
        // given
        ReservationTime time = sampleTimes.get(0);
        ReservationTime savedTime = timeRepository.save(time);
        Long savedId = savedTime.getId();

        // when
        Optional<ReservationTime> actual = timeRepository.findById(savedId);
        ReservationTime expected = time.assignId(savedId);

        // then
        assertThat(actual).hasValue(expected);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간 데이터를 조회할 경우 빈 값을 반환한다.")
    void findByIdNotExist() {
        // given
        long notExistId = 1L;

        // when
        Optional<ReservationTime> actual = timeRepository.findById(notExistId);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("시간이 이미 존재하는지 확인한다.")
    void existByStartAt() {
        // given
        ReservationTime time = sampleTimes.get(0);
        timeRepository.save(time);

        // when
        boolean actual = timeRepository.existByStartAt(time.getStartAt());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("예약 시간을 저장한다.")
    void save() {
        // given
        ReservationTime time = new ReservationTime(null, LocalTime.of(13, 30));

        // when
        ReservationTime actual = timeRepository.save(time);
        ReservationTime expected = time.assignId(actual.getId());

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("등록된 예약 시간 번호로 삭제한다.")
    void deletePresent() {
        // given
        ReservationTime savedTime = timeRepository.save(sampleTimes.get(0));
        Long existId = savedTime.getId();

        // when & then
        assertThat(timeRepository.findById(existId)).isPresent();
        assertThat(timeRepository.delete(existId)).isNotZero();
        assertThat(timeRepository.findById(existId)).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간 번호로 삭제할 경우 아무런 영향이 없다.")
    void deleteNotExist() {
        // given
        long notExistId = 1L;

        // when & then
        assertThat(timeRepository.findById(notExistId)).isEmpty();
        assertThat(timeRepository.delete(notExistId)).isZero();
    }
}
