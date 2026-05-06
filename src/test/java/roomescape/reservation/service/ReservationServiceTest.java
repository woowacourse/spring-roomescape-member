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
import roomescape.date.domain.ReservationDate;
import roomescape.date.fixture.FakeReservationDateRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationSaveDto;
import roomescape.reservation.repository.FakeReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.fixture.FakeThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.FakeReservationTimeRepository;

class ReservationServiceTest {

    private final String name = "한다";
    private ReservationTime reservationTime1;
    private ReservationTime reservationTime2;
    private ReservationDate reservationDate1;
    private ReservationDate reservationDate2;
    private Theme theme1;
    private Theme theme2;

    private FakeReservationRepository reservationRepository;
    private FakeReservationTimeRepository reservationTimeRepository;
    private FakeReservationDateRepository reservationDateRepository;
    private FakeThemeRepository themeRepository;

    private ReservationService reservationService;

    @BeforeEach
    void setup() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationDateRepository = new FakeReservationDateRepository();
        themeRepository = new FakeThemeRepository();

        this.reservationService = new ReservationService(reservationRepository, reservationTimeRepository,
                reservationDateRepository, themeRepository);

        LocalTime time1 =  LocalTime.of(15, 40);
        Long savedTimeId1 = reservationTimeRepository.save(ReservationTime.create(time1));
        reservationTime1 = ReservationTime.of(savedTimeId1, time1);

        LocalTime time2 =  LocalTime.of(16, 0);
        Long savedTimeId2 = reservationTimeRepository.save(ReservationTime.create(time2));
        reservationTime2 = ReservationTime.of(savedTimeId2, time2);

        reservationDate1 = reservationDateRepository.save(ReservationDate.create(LocalDate.now().plusWeeks(1)));
        reservationDate2 = reservationDateRepository.save(ReservationDate.create(LocalDate.now().plusWeeks(2)));

        theme1 = themeRepository.save(Theme.create("테마1", "설명1", "썸네일1"));
        theme2 = themeRepository.save(Theme.create("테마2", "설명2", "썸네일2"));
    }

    @Test
    @DisplayName("전체 예약 정보를 가져온다.")
    void findAll() {
        //given & when
        List<Reservation> reservations = List.of(Reservation.create("한다", reservationDate1.date(), reservationTime1.startAt(), theme1),
                Reservation.create("송송", reservationDate2.date(), reservationTime1.startAt(), theme2));
        reservationRepository.saveAll(reservations);
        List<ReservationResponse> reservationsResponse = reservationService.findAll();

        //then
        assertThat(reservationsResponse)
                .hasSize(reservations.size());
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void create() {
        //given & when
        List<Reservation> reservations = List.of();
        reservationService.create(new ReservationSaveDto("브라운", reservationDate1.id(), reservationTime1.id(), theme1.id()));

        //then
        assertThat(reservationService.findAll())
                .hasSize(reservations.size() + 1);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간이면 예외를 발생한다.")
    void create_does_not_exist_reservation_time() {
        // given
        Long wrongTimeId = Long.MIN_VALUE;
        ReservationSaveDto command = new ReservationSaveDto(name, reservationDate1.id(), wrongTimeId, theme1.id());

        // when & then
        assertThatThrownBy(() -> reservationService.create(command))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void delete() {
        //given
        List<ReservationSaveDto> reservations = List.of(new ReservationSaveDto(name, reservationDate1.id(), reservationTime1.id(), theme1.id()));
        ReservationResponse reservationResponse =  reservationService.create(reservations.getFirst());

        //when
        reservationService.delete(reservationResponse.id());

        //then
        assertThat(reservationService.findAll())
                .hasSize(reservations.size() - 1);
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하면 예외를 발생한다.")
    void delete_does_not_exist() {
        // given
        Long wrongReservationId = Long.MIN_VALUE;

        // when & then
        assertThatThrownBy(() -> reservationService.delete(wrongReservationId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }

    @Test
    @DisplayName("예약 생성 시 예약 날짜/시간/테마가 중복되면 예외를 발생한다.")
    void create_duplicate_reservation() {
        // given
        reservationService.create(new ReservationSaveDto("브라운", reservationDate1.id(), reservationTime1.id(), theme1.id()));
        ReservationSaveDto duplicateDateTimeCommand = new ReservationSaveDto("한다", reservationDate1.id(), reservationTime1.id(), theme1.id());

        // when & then
        assertThatThrownBy(() -> reservationService.create(duplicateDateTimeCommand))
                .isInstanceOf(ConflictException.class)
                .hasMessage("이미 존재하는 예약 날짜/시간 입니다.");
    }
}
