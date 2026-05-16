package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.fixture.ReservationFixture;
import roomescape.domain.fixture.ReservationTimeFixture;
import roomescape.domain.fixture.ThemeFixture;
import roomescape.global.exception.DuplicateEntityException;
import roomescape.global.exception.ForbiddenException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.fake.FakeReservationRepository;
import roomescape.repository.fake.FakeReservationTimeRepository;
import roomescape.web.dto.reservationTime.ReservationTimeRequest;
import roomescape.web.dto.reservationTime.ReservationTimeResponse;

class ReservationTimeServiceTest {

    private ReservationTimeRepository reservationTimeRepository;
    private ReservationRepository reservationRepository;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        this.reservationRepository = new FakeReservationRepository();
        this.reservationTimeRepository = new FakeReservationTimeRepository();
        this.reservationTimeService = new ReservationTimeService(this.reservationTimeRepository,
                this.reservationRepository);
    }

    @Test
    void 새로운_예약_시간을_정상적으로_등록한다() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);
        ReservationTimeRequest request = new ReservationTimeRequest(startAt);

        // when
        ReservationTimeResponse response = reservationTimeService.register(request);

        // then
        assertThat(response).extracting(ReservationTimeResponse::id, ReservationTimeResponse::startAt)
                .containsExactly(1L, startAt);
    }

    @Test
    void 이미_등록된_시간으로_등록을_시도하면_예외가_발생한다() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);
        reservationTimeRepository.save(ReservationTime.create(startAt));

        ReservationTimeRequest request = new ReservationTimeRequest(startAt);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.register(request)).isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("이미 등록된 예약 시간 입니다.");
    }

    @Test
    void 모든_예약_시간_목록을_조회한다() {
        // given
        reservationTimeRepository.save(ReservationTimeFixture.createDefaultReservationTime());
        reservationTimeRepository.save(ReservationTime.create(LocalTime.of(11, 0)));

        // when
        List<ReservationTimeResponse> responses = reservationTimeService.getAllReservationTimesByPaging(0, 10);

        // then
        assertThat(responses).hasSize(2);
    }

    @Test
    void 예약_시간을_비활성화한다() {
        // given
        ReservationTime time = reservationTimeRepository.save(ReservationTimeFixture.createDefaultReservationTime());

        // when
        reservationTimeService.deactivate(time.getId());
        ReservationTime deactivateTime = reservationTimeRepository.findById(time.getId()).get();

        // then
        assertThat(deactivateTime.isActive()).isFalse();
    }

    @Test
    void 비활성화하려는_시간에_예약이_존재하면_예외가_발생한다() {
        // given
        ReservationTime time = reservationTimeRepository.save(ReservationTimeFixture.createDefaultReservationTime());
        Theme theme = ThemeFixture.createThemeWithId();
        reservationRepository.save(ReservationFixture.createDefaultReservationWithName("바니", theme, time));

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deactivate(time.getId())).isInstanceOf(
                ForbiddenException.class);

    }
}
