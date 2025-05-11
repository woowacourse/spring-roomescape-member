package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.test.fixture.ReservationTimeFixture.addReservationTimeInRepository;
import static roomescape.test.fixture.ThemeFixture.addThemeInRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationTimeCreationRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.test.fake.FakeH2ReservationRepository;
import roomescape.test.fake.FakeH2ReservationTimeRepository;
import roomescape.test.fake.FakeH2ThemeRepository;

class ReservationTimeServiceTest {

    private final ReservationRepository reservationRepository = new FakeH2ReservationRepository();
    private final ReservationTimeRepository timeRepository = new FakeH2ReservationTimeRepository();
    private final ThemeRepository themeRepository = new FakeH2ThemeRepository();
    private final ReservationTimeService reservationTimeService =
            new ReservationTimeService(reservationRepository, timeRepository);

    @DisplayName("등록된 모든 예약 가능 시간을 조회활 수 있다")
    @Test
    void canGetReservationTimes() {
        addReservationTimeInRepository(timeRepository, LocalTime.of(10, 0));
        addReservationTimeInRepository(timeRepository, LocalTime.of(11, 0));
        addReservationTimeInRepository(timeRepository, LocalTime.of(12, 0));

        List<ReservationTimeResponse> reservationTimes = reservationTimeService.getAllReservationTime();

        assertThat(reservationTimes).hasSize(3);
    }

    @DisplayName("ID를 통해 예약 시간을 조회할 수 있다")
    @Test
    void canGetReservationTimeById() {
        ReservationTime expectedTime = addReservationTimeInRepository(timeRepository, LocalTime.of(10, 0));

        ReservationTimeResponse actualTimeResponse = reservationTimeService.getReservationTimeById(1L);
        ReservationTime actualTime = new ReservationTime(actualTimeResponse.id(), actualTimeResponse.startAt());
        assertThat(actualTime).isEqualTo(expectedTime);
    }

    @DisplayName("ID를 통해 예약을 조회할 때 데이터가 없으면 예외를 발생시킨다")
    @Test
    void cannotGetReservationTimeByIdWhenEmptyTime() {
        assertThatThrownBy(() -> reservationTimeService.getReservationTimeById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 예약 시간이 존재하지 않습니다.");
    }

    @DisplayName("예약 가능 시간을 추가할 수 있다")
    @Test
    void canCreateReservationTime() {
        ReservationTimeCreationRequest request = new ReservationTimeCreationRequest(LocalTime.of(10, 0));

        long savedId = reservationTimeService.saveReservationTime(request);

        ReservationTime savedTime = timeRepository.findAll().getFirst();
        ReservationTime expectedSavedTime = new ReservationTime(1L, request.startAt());

        assertAll(
                () -> assertThat(savedId).isEqualTo(1L),
                () -> assertThat(savedTime).isEqualTo(expectedSavedTime)
        );
    }

    @DisplayName("이미 추가한 시간의 경우 추가할 수 없다")
    @Test
    void canCreateSameReservationTime() {
        LocalTime duplicatedTime = LocalTime.of(10, 0);
        addReservationTimeInRepository(timeRepository, duplicatedTime);
        ReservationTimeCreationRequest request = new ReservationTimeCreationRequest(duplicatedTime);

        assertThatThrownBy(() -> reservationTimeService.saveReservationTime(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[Error] 이미 추가가 완료된 예약 가능 시간입니다.");
    }

    @DisplayName("ID를 통해 예약 가능 시간을 삭제할 수 있다")
    @Test
    void canDeleteReservationTime() {
        addReservationTimeInRepository(timeRepository, LocalTime.of(10, 0));
        addReservationTimeInRepository(timeRepository, LocalTime.of(11, 0));
        addReservationTimeInRepository(timeRepository, LocalTime.of(12, 0));
        Long deletedId = timeRepository.findAll().getFirst().getId();

        reservationTimeService.deleteReservationTime(deletedId);

        assertThat(timeRepository.findAll())
                .extracting(ReservationTime::getId)
                .doesNotContain(deletedId);
    }

    @DisplayName("존재하지 않는 예약 가능 시간을 삭제하려고 할 경우 예외 응답을 보낸다")
    @Test
    void canNotDeleteWithInvalidId() {
        long noneExistentId = 1L;
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(noneExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 예약 시간이 존재하지 않습니다.");
    }

    @DisplayName("이미 해당 시간에 예약이 존재하는 경우 예약을 제거할 수 없다")
    @Test
    void canNotDeleteBecauseReservations() {
        ReservationTime savedTime = addReservationTimeInRepository(timeRepository, LocalTime.of(10, 0));
        Theme theme = addThemeInRepository(themeRepository, "이름", "설명", "썸네일");
        Member member = new Member(1L, "사람", "email", "비번", "member");
        reservationRepository.add(Reservation.createWithoutId(member, LocalDate.now(), savedTime, theme));

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(savedTime.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 이미 해당 시간에 대한 예약 데이터들이 존재합니다.");
    }
}
