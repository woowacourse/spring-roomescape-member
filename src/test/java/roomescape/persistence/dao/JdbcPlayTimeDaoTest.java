package roomescape.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.business.domain.PlayTime;

@JdbcTest
class JdbcPlayTimeDaoTest {

    private final PlayTimeDao playTimeDao;

    @Autowired
    JdbcPlayTimeDaoTest(final JdbcTemplate jdbcTemplate) {
        this.playTimeDao = new JdbcPlayTimeDao(jdbcTemplate);
    }

    @Test
    @DisplayName("데이터베이스에 방탈출 예약 시간을 저장하고 조회하여 확인한다")
    void saveAndFindById() {
        // given
        PlayTime playTime = new PlayTime(LocalTime.of(10, 10));

        // when
        final Long id = playTimeDao.save(playTime);

        // then
        final Optional<PlayTime> findPlayTime = playTimeDao.find(id);
        assertAll(
                () -> assertThat(findPlayTime).isPresent(),
                () -> assertThat(findPlayTime.get().getId()).isEqualTo(id),
                () -> assertThat(findPlayTime.get().getStartAt()).isEqualTo(playTime.getStartAt())
        );
    }

    @Test
    @DisplayName("데이터베이스에서 id를 통해 테마를 삭제한다")
    void deleteById() {
        // given
        PlayTime playTime = new PlayTime(LocalTime.of(10, 10));
        long id = playTimeDao.save(playTime);

        // when
        boolean isDeleted = playTimeDao.remove(id);

        // then
        assertAll(
                () -> assertThat(isDeleted).isTrue(),
                () -> assertThat(playTimeDao.find(id)).isEmpty()
        );
    }

    @Test
    @DisplayName("데이터베이스의 모든 방탈출 예약 시간을 조회한다")
    void findAll() {
        // given
        PlayTime playTime1 = new PlayTime(LocalTime.of(10, 10));
        PlayTime playTime2 = new PlayTime(LocalTime.of(11, 10));
        playTimeDao.save(playTime1);
        playTimeDao.save(playTime2);

        // when
        final List<PlayTime> playTimes = playTimeDao.findAll();

        // then
        assertThat(playTimes).hasSize(2);
    }

    @Test
    @DisplayName("데이터베이스에서 id를 통해 방탈출 예약 시간을 삭제할 때 대상이 없다면 false 반환한다")
    void deleteByIdWhenNotExist() {
        // when
        final boolean isDeleted = playTimeDao.remove(1L);

        // then
        assertThat(isDeleted).isFalse();
    }

    @Test
    @DisplayName("데이터베이스에서 방탈출 예약 시간을 통해 대상이 존재하는지 확인한다")
    void existsByStartAt() {
        // given
        final LocalTime validStartAt = LocalTime.of(10, 10);
        final LocalTime invalidStartAt = LocalTime.of(11, 10);
        final PlayTime playTime = new PlayTime(validStartAt);
        playTimeDao.save(playTime);

        // when & then
        assertAll(
                () -> assertThat(playTimeDao.existsByStartAt(validStartAt)).isTrue(),
                () -> assertThat(playTimeDao.existsByStartAt(invalidStartAt)).isFalse()
        );
    }
}
