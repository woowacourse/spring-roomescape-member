package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.date.fixture.FakeReservationDateRepository;
import roomescape.reservation.dto.ReservationSaveDto;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.FakeReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.fixture.FakeThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.FakeReservationTimeRepository;

class ReservationServiceTest {

    private final String name = "한다";
    private final LocalDate date = LocalDate.now().plusMonths(1);
    private final ReservationTime time = ReservationTime.of(1L, LocalTime.of(15, 40));
    private Theme theme;

    private ReservationService reservationService;
    private Long timeId;

    @BeforeEach
    void setup() {
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        FakeReservationDateRepository reservationDateRepository = new FakeReservationDateRepository();
        FakeThemeRepository themeRepository = new FakeThemeRepository();
        this.reservationService = new ReservationService(reservationRepository, reservationTimeRepository, reservationDateRepository, themeRepository);

        timeId = reservationTimeRepository.save(time);
        theme = themeRepository.save(Theme.create("테마", "설명", "썸네일"));

        reservationService.create(new ReservationSaveDto(name, date, timeId, theme.id()));
        reservationService.create(new ReservationSaveDto("판다", LocalDate.now().plusWeeks(2), timeId, theme.id()));
    }

    @Test
    @DisplayName("전체 예약 정보를 가져온다.")
    void findAll() {
        //given & when
        List<ReservationResponse> reservationsResponse = reservationService.findAll();

        //then
        assertThat(reservationsResponse)
                .hasSize(2);
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void create() {
        //given & when
        reservationService.create(new ReservationSaveDto("브라운", LocalDate.now().plusWeeks(4), timeId, theme.id()));

        //then
        assertThat(reservationService.findAll())
                .hasSize(3);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간이면 예외를 발생한다.")
    void create_does_not_exist_reservation_time() {
        Long wrongTimeId = Long.MIN_VALUE;
        ReservationSaveDto command = new ReservationSaveDto(name, date, wrongTimeId, theme.id());

        assertThatThrownBy(() -> reservationService.create(command))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void delete() {
        //given
        LocalDate uniqueDate = LocalDate.of(2099, 1, 1);
        ReservationResponse reservationResponse = reservationService.create(new ReservationSaveDto(name, uniqueDate, timeId, theme.id()));
        Long id = reservationResponse.id();

        //when
        reservationService.delete(id);

        //then
        assertThat(reservationService.findAll())
                .hasSize(2);
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하면 예외를 발생한다.")
    void delete_does_not_exist() {
        Long wrongReservationId = Long.MIN_VALUE;

        assertThatThrownBy(() -> reservationService.delete(wrongReservationId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }

    @Test
    @DisplayName("이미 존재하는 예약 생성 시 예외를 발생한다.")
    void create_duplicate_reservation() {
        // given
        LocalDate duplicatedDate = LocalDate.now().plusWeeks(4);
        reservationService.create(new ReservationSaveDto("브라운", duplicatedDate, timeId, theme.id()));
        ReservationSaveDto duplicatedDateTimeCommand = new ReservationSaveDto("브라운", duplicatedDate, timeId, theme.id());

        // when & then
        assertThatThrownBy(() -> reservationService.create(duplicatedDateTimeCommand))
                .isInstanceOf(ConflictException.class)
                .hasMessage("이미 존재하는 예약 날짜/시간 입니다.");
    }
}
