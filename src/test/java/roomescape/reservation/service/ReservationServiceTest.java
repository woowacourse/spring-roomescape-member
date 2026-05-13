package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.reservation.domain.ReservationStatus.RESERVED;
import static roomescape.reservation.fixture.ReservationFixture.reservation;
import static roomescape.reservation.fixture.ReservationFixture.saveDto;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
                .isInstanceOf(IllegalArgumentException.class)
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
    @DisplayName("예약된 날짜/시간/테마를 중복 예약하면 예외가 발생한다.")
    void reserved_duplicated() {
        // given
        Reservation reservation = reservation(name, reservationDate1, reservationTime1, theme1);
        ReservationSaveDto duplicated = saveDto(name, reservationDate1, reservationTime1, theme1);
        save(reservation);

        // when & then
        Assertions.assertThatThrownBy(() -> reservationService.reserve(duplicated))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 날짜/시간/테마는 이미 예약되었습니다.");
    }

    @Test
    @DisplayName("취소된 예약을 동일한 사람이 새롭게 예약할 수 있다.")
    void reserved_when_cancel_same_name() {
        // given
        Reservation reservation = save(reservation(name, reservationDate1, reservationTime1, theme1));
        ReservationSaveDto duplicated = saveDto(name, reservationDate1, reservationTime1, theme1);
        cancelByManager(reservation);

        // when
        Reservation actual = reservationService.reserve(duplicated);

        // then
        Assertions.assertThat(actual.status())
                .isEqualTo(ReservationStatus.RESERVED);
    }

    @Test
    @DisplayName("취소된 예약을 다른 사람이 새롭게 예약할 수 있다.")
    void reserved_when_cancel_another_name() {
        // given
        String anotherName = "다른사람";
        Reservation reservation = save(reservation(name, reservationDate1, reservationTime1, theme1));
        ReservationSaveDto duplicated = saveDto(anotherName, reservationDate1, reservationTime1, theme1);
        cancelByManager(reservation);

        // when
        Reservation actual = reservationService.reserve(duplicated);

        // then
        Assertions.assertThat(actual.status())
                .isEqualTo(ReservationStatus.RESERVED);
    }

    @Test
    @DisplayName("관리자 전용으로 예약을 취소하면 CANCELED 상태가 된다.")
    void cancelByManager() {
        // given
        Reservation savedReservation = save(reservation(name, reservationDate1, reservationTime1, theme1));

        // when
        Reservation actual = reservationService.cancelByManager(savedReservation.id());

        // then
        Assertions.assertThat(actual.status())
                .isEqualTo(ReservationStatus.CANCELED);
    }

    @Test
    @DisplayName("아직 지나지 않은 본인의 예약은 취소할 수 있다.")
    void cancel() {
        // given
        Reservation savedReservation = save(reservation(name, reservationDate1, reservationTime1, theme1));

        // when
        Reservation actual = reservationService.cancel(savedReservation.id(), name);

        // then
        Assertions.assertThat(actual.status())
                .isEqualTo(ReservationStatus.CANCELED);
    }

    @Test
    @DisplayName("본인의 예약이 아닌데 취소를하면 예외가 발생한다.")
    void cancel_not_owner() {
        // given
        Reservation saved = save(reservation(name, reservationDate1, reservationTime1, theme1));
        String anotherName = "다른사람";
        Long savedId = saved.id();

        // when & then
        Assertions.assertThatThrownBy(() -> reservationService.cancel(savedId, anotherName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("본인의 예약만 취소할 수 있습니다.");
    }

    @Test
    @DisplayName("이미 취소된 예약을 취소하면 예외가 발생한다.")
    void cancel_already_canceled() {
        // given
        Reservation saved = save(reservation(name, reservationDate1, reservationTime1, theme1));
        saved.updateStatus(ReservationStatus.CANCELED);
        reservationRepository.updateStatus(saved);
        Long savedId = saved.id();

        // when & then
        Assertions.assertThatThrownBy(() -> reservationService.cancel(savedId, name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 취소된 예약입니다.");
    }

    @Test
    @DisplayName("이미 지난 예약을 취소하면 예외가 발생한다.")
    void cancel_not_past() {
        // given
        ReservationDate pastDate = ReservationDate.load(1L, LocalDate.now().minusDays(1), true);
        Reservation saved =
                save(Reservation.load(1L, name, pastDate, reservationTime1, theme1, ReservationStatus.RESERVED));
        Long savedId = saved.id();

        // when & then
        Assertions.assertThatThrownBy(() -> reservationService.cancel(savedId, name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 지난 예약입니다.");
    }

    @Test
    @DisplayName("예약 가능한 날짜로 변경할 수 있다.")
    void changeSchedule() {
        // given
        Reservation saved = save(reservation(name, reservationDate1, reservationTime1, theme1));

        // when
        reservationService.changeSchedule(saved.id(), name, reservationDate2.id(), reservationTime2.id());

        // then
        Assertions.assertThat(reservationRepository.findById(saved.id()).get())
                .isEqualTo(saved);
    }

    @Test
    @DisplayName("본인의 예약이 아닌데 변경을 시도하면 예외가 발생한다.")
    void changeSchedule_not_owner() {
        // given
        Reservation saved = save(reservation(name, reservationDate1, reservationTime1, theme1));
        String notOwerName = "다른사람";

        // when & then
        Assertions.assertThatThrownBy(() -> {
                    reservationService.changeSchedule(saved.id(), notOwerName, reservationDate2.id(), reservationTime2.id());
                }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("본인의 예약만 취소할 수 있습니다.");
    }

    @Test
    @DisplayName("이미 취소된 예약을 변경하면 예외가 발생한다.")
    void changeSchedule_already_canceled() {
        // given
        Reservation saved = save(reservation(name, reservationDate1, reservationTime1, theme1));
        saved.updateStatus(ReservationStatus.CANCELED);
        reservationRepository.updateStatus(saved);

        // when
        Assertions.assertThatThrownBy(() -> {
                    reservationService.changeSchedule(saved.id(), name, reservationDate2.id(), reservationTime2.id());
                }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 취소된 예약입니다.");
    }

    @Test
    @DisplayName("이미 지난 예약을 변경하면 예외가 발생한다.")
    void changeSchedule_past() {
        // given
        ReservationDate pastDate = ReservationDate.load(1L, LocalDate.now().minusDays(1), true);
        Reservation saved =
                save(Reservation.load(1L, name, pastDate, reservationTime1, theme1, ReservationStatus.RESERVED));

        // when
        Assertions.assertThatThrownBy(() -> {
                    reservationService.changeSchedule(saved.id(), name, reservationDate2.id(), reservationTime2.id());
                }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 지난 예약입니다.");
    }

    @Test
    @DisplayName("지난 날짜/시간으로 예약을 변경하면 예외가 발생한다.")
    void changeSchedule_new_datetime_is_past() {
        // given
        ReservationDate pastDate = reservationDateRepository.save(ReservationDate.load(20L, LocalDate.now().minusDays(1), true));
        Reservation saved = save(reservation(name, reservationDate1, reservationTime1, theme1));

        // when
        Assertions.assertThatThrownBy(() -> {
                    reservationService.changeSchedule(saved.id(), name, pastDate.id(), reservationTime2.id());
                }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 지난 날짜/시간을 예약할 수 없습니다.");
    }

    private Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    private void cancelByManager(Reservation reservation) {
        reservationService.cancelByManager(reservation.id());
    }

}
