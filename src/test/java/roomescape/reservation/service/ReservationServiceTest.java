package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.reservation.fixture.ReservationFixture.reservation;
import static roomescape.reservation.fixture.ReservationFixture.saveDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.date.domain.ReservationDate;
import roomescape.date.fixture.FakeReservationDateRepository;
import roomescape.date.fixture.ReservationDateFixture;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationStatus;
import roomescape.reservation.dto.request.ReservationSaveDto;
import roomescape.reservation.fixture.FakeReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.fixture.FakeThemeRepository;
import roomescape.theme.fixture.ThemeFixture;
import roomescape.time.domain.ReservationTime;
import roomescape.time.fixture.FakeReservationTimeRepository;
import roomescape.time.fixture.ReservationTimeFixture;

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

        reservationTime1 = reservationTimeRepository.save(ReservationTimeFixture.time15());
        reservationTime2 = reservationTimeRepository.save(ReservationTimeFixture.time16());

        reservationDate1 = reservationDateRepository.save(ReservationDateFixture.oneWeekLater());
        reservationDate2 = reservationDateRepository.save(ReservationDateFixture.twoWeeksLater());

        theme1 = themeRepository.save(ThemeFixture.theme("테마1"));
        theme2 = themeRepository.save(ThemeFixture.theme("테마2"));
    }

    @Test
    @DisplayName("전체 예약 정보를 가져온다.")
    void readAll() {
        //given & when
        List<Reservation> reservations = List.of(
                reservation(name, reservationDate1, reservationTime1, theme1),
                reservation(name, reservationDate2, reservationTime1, theme2)
        );
        reservationRepository.saveAll(reservations);
        List<Reservation> actual = reservationService.readAll();

        //then
        assertThat(actual)
                .hasSize(reservations.size());
    }

    @Test
    @DisplayName("나의 예약들을 조회하면 날짜/시간 오름차순으로 정렬해 모두 조회한다.")
    void readAllByName() {
        // given
        List<Reservation> reservations = reservationRepository.saveAll(
                List.of(reservation(name, reservationDate1, reservationTime1, theme1),
                        reservation(name, reservationDate1, reservationTime2, theme1),
                        reservation(name, reservationDate2, reservationTime1, theme1),
                        reservation(name, reservationDate2, reservationTime2, theme1))
        );

        // when
        List<Reservation> actual = reservationService.readAllByName(name);

        // then
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(reservations);
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void reserve() {
        //given & when
        List<Reservation> reservations = List.of();
        reservationService.reserve(saveDto(name, reservationDate1, reservationTime1, theme1));

        //then
        assertThat(reservationService.readAll())
                .hasSize(reservations.size() + 1);
    }

    @Test
    @DisplayName("예약시, 등록되지 않은 예약 시간이면 예외를 발생한다.")
    void reserve_does_not_exist_reservation_time() {
        // given
        Long wrongTimeId = Long.MIN_VALUE;
        ReservationSaveDto command = saveDto(name, reservationDate1, wrongTimeId, theme1);

        // when & then
        assertThatThrownBy(() -> reservationService.reserve(command))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");
    }


    @Test
    @DisplayName("예약시, 등록되지 않은 테마이면 예외를 발생한다.")
    void reserve_does_not_exist_theme() {
        // given
        Long wrongThemeId = Long.MIN_VALUE;
        ReservationSaveDto command = saveDto(name, reservationDate1, reservationTime1, wrongThemeId);

        // when & then
        assertThatThrownBy(() -> reservationService.reserve(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테마가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("예약시 예약 날짜/시간/테마가 중복되면 예외를 발생한다.")
    void reserve_duplicate_reservation() {
        // given
        ReservationSaveDto command = saveDto("브라운", reservationDate1, reservationTime1, theme1);
        ReservationSaveDto duplicated = saveDto("한다", reservationDate1, reservationTime1, theme1);
        reservationService.reserve(command);

        // when & then
        assertThatThrownBy(() -> reservationService.reserve(duplicated))
                .isInstanceOf(ConflictException.class)
                .hasMessage("해당 날짜/시간/테마는 이미 예약되었습니다.");
    }

    @Test
    @DisplayName("예약을 취소하면 CANCELED 상태가 된다.")
    void updateStatus_canceled() {
        // given
        Reservation savedReservation = save(reservation(name, reservationDate1, reservationTime1, theme1));

        // when
        Reservation actual = reservationService.cancel(savedReservation.id());

        // then
        Assertions.assertThat(actual.status())
                .isEqualTo(ReservationStatus.CANCELED);
    }

    private Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

}
