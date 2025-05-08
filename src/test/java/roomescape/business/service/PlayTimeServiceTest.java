package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.DuplicateException;
import roomescape.exception.NotFoundException;
import roomescape.fake.FakePlayTimeDao;
import roomescape.presentation.dto.PlayTimeRequest;
import roomescape.presentation.dto.PlayTimeResponse;

class PlayTimeServiceTest {

    private PlayTimeService playTimeService;

    @BeforeEach
    void setUp() {
        playTimeService = new PlayTimeService(new FakePlayTimeDao());
    }

    @Test
    @DisplayName("방탈출 예약 시간 요청 객체로 방탈출 예약 시간을 저장한다")
    void insert() {
        // given
        final LocalTime startAt = LocalTime.of(10, 10);
        final PlayTimeRequest playTimeRequest = new PlayTimeRequest(startAt);

        // when
        final PlayTimeResponse playTimeResponse = playTimeService.insert(playTimeRequest);

        // then
        assertThat(playTimeResponse.startAt()).isEqualTo(startAt);
    }

    @Test
    @DisplayName("저장하려는 방탈출 예약 시간과 동일한 방탈출 예약 시간이 이미 존재한다면 예외가 발생한다")
    void insertWhenStartAtIsDuplicate() {
        // given
        final LocalTime startAt = LocalTime.of(10, 10);
        final PlayTimeRequest playTimeRequest = new PlayTimeRequest(startAt);
        playTimeService.insert(playTimeRequest);

        // when & then
        assertThatThrownBy(() -> playTimeService.insert(playTimeRequest))
                .isInstanceOf(DuplicateException.class);
    }

    @Test
    @DisplayName("모든 방탈출 예약 시간을 조회한다")
    void findAll() {
        // given
        final PlayTimeRequest playTimeRequest1 = new PlayTimeRequest(LocalTime.of(10, 0));
        final PlayTimeRequest playTimeRequest2 = new PlayTimeRequest(LocalTime.of(20, 15));
        playTimeService.insert(playTimeRequest1);
        playTimeService.insert(playTimeRequest2);

        // when
        final List<PlayTimeResponse> playTimeResponses = playTimeService.findAll();

        // then
        assertThat(playTimeResponses).hasSize(2);
    }

    @Test
    @DisplayName("id를 통해 방탈출 예약 시간을 조회한다")
    void findByIdById() {
        // given
        final LocalTime startAt = LocalTime.of(10, 10);
        final PlayTimeRequest playTimeRequest = new PlayTimeRequest(startAt);
        final PlayTimeResponse playTimeResponse = playTimeService.insert(playTimeRequest);
        final Long id = playTimeResponse.id();

        // when
        final PlayTimeResponse findPlayTimeResponse = playTimeService.findById(id);

        // then
        assertAll(
                () -> assertThat(findPlayTimeResponse.id()).isEqualTo(id),
                () -> assertThat(findPlayTimeResponse.startAt()).isEqualTo(startAt)
        );
    }

    @Test
    @DisplayName("id를 통해 방탈출 예약 시간을 조회할 때 대상이 없다면 예외가 발생한다")
    void findByIdWhenNotExists() {
        // given
        final Long notExistsId = 999L;

        // when & then
        assertThatThrownBy(() -> playTimeService.findById(notExistsId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("id를 통해 방탈출 예약 시간을 삭제한다")
    void deleteById() {
        // given
        final LocalTime startAt = LocalTime.of(10, 10);
        final PlayTimeRequest playTimeRequest = new PlayTimeRequest(startAt);
        final PlayTimeResponse playTimeResponse = playTimeService.insert(playTimeRequest);
        final Long id = playTimeResponse.id();

        // when
        playTimeService.deleteById(id);

        // then
        final List<PlayTimeResponse> playTimeResponses = playTimeService.findAll();
        assertThat(playTimeResponses).isEmpty();
    }

    @Test
    @DisplayName("id를 통해 방탈출 예약 시간을 삭제할 때 대상이 없다면 예외가 발생한다")
    void deleteByIdWhenNotExists() {
        // given
        final Long notExistsId = 999L;

        // when & then
        assertThatThrownBy(() -> playTimeService.deleteById(notExistsId))
                .isInstanceOf(NotFoundException.class);
    }
}
