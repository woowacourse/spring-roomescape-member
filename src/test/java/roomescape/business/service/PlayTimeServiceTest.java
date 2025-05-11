package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.domain.PlayTime;
import roomescape.exception.DuplicatePlayTimeException;
import roomescape.fake.FakePlayTimeDao;
import roomescape.presentation.dto.playtime.PlayTimeRequest;
import roomescape.presentation.dto.playtime.PlayTimeResponse;

class PlayTimeServiceTest {

    private static final LocalTime FORMATTED_MAX_LOCAL_TIME = LocalTime.of(23, 59);

    private PlayTimeService playTimeService;

    @BeforeEach
    void setUp() {
        playTimeService = new PlayTimeService(new FakePlayTimeDao());
    }

    @DisplayName("방탈출 시간을 조회한다.")
    @Test
    void find() {
        // given
        playTimeService.create(new PlayTimeRequest(FORMATTED_MAX_LOCAL_TIME));

        final Long id = 1L;
        final PlayTime expected = PlayTime.createWithId(1L, FORMATTED_MAX_LOCAL_TIME);

        // when & then
        assertThat(playTimeService.find(id))
                .isEqualTo(expected);
    }

    @DisplayName("조회하려는 방탈출 시간 id가 없다면 예외가 발생한다.")
    @Test
    void findOrThrowIfIdNotExists() {
        // given
        final Long id = 1L;

        // when & then
        assertThatThrownBy(() -> playTimeService.find(id))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("방탈출 시간을 저장한다.")
    @Test
    void create() {
        // given
        final PlayTimeRequest playTimeRequest = new PlayTimeRequest(FORMATTED_MAX_LOCAL_TIME);
        final PlayTimeResponse expected = new PlayTimeResponse(1L, FORMATTED_MAX_LOCAL_TIME);

        // when & then
        assertThat(playTimeService.create(playTimeRequest))
                .isEqualTo(expected);
    }

    @DisplayName("저장하려는 방탈출 시간이 이미 존재한다면 예외가 발생한다.")
    @Test
    void createOrThrowIfStartAtDuplicate() {
        // given
        final PlayTimeRequest playTimeRequest = new PlayTimeRequest(FORMATTED_MAX_LOCAL_TIME);
        playTimeService.create(playTimeRequest);

        // when & then
        assertThatThrownBy(() -> playTimeService.create(playTimeRequest))
                .isInstanceOf(DuplicatePlayTimeException.class);
    }

    @DisplayName("모든 방탈출 시간을 조회한다.")
    @Test
    void findAll() {
        // given
        playTimeService.create(new PlayTimeRequest(LocalTime.of(10, 0)));
        playTimeService.create(new PlayTimeRequest(LocalTime.of(20, 15)));

        // when & then
        assertThat(playTimeService.findAll())
                .containsExactly(
                        new PlayTimeResponse(1L, LocalTime.of(10, 0)),
                        new PlayTimeResponse(2L, LocalTime.of(20, 15))
                );
    }

    @DisplayName("방탈출 시간을 삭제한다.")
    @Test
    void remove() {
        // given
        playTimeService.create(new PlayTimeRequest(FORMATTED_MAX_LOCAL_TIME));
        playTimeService.remove(1L);

        // when & then
        assertThat(playTimeService.findAll()).isEmpty();
    }

    @DisplayName("삭제하려는 방탈출 시간 id가 없다면 예외가 발생한다.")
    @Test
    void removeOrThrowIfIdNotExists() {
        // given & when & then
        assertThatThrownBy(() -> playTimeService.remove(1L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
