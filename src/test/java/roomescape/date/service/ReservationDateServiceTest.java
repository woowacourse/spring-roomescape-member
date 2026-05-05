package roomescape.date.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.date.domain.ReservationDate;
import roomescape.date.fixture.FakeReservationDateRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationDateServiceTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.of(2099, 1, 1);

    private FakeReservationDateRepository reservationDateRepository;
    private ReservationDateService reservationDateService;

    @BeforeEach
    void setUp() {
        reservationDateRepository = new FakeReservationDateRepository();
        reservationDateService = new ReservationDateService(reservationDateRepository);
    }

    @Test
    @DisplayName("등록된 예약날짜가 여러개이면 조회 시 등록된 개수만큼 반환한다.")
    void readDates() {
        // given
        List<ReservationDate> reservationDates = List.of(
                ReservationDate.create(DEFAULT_DATE),
                ReservationDate.create(DEFAULT_DATE.plusDays(1)));
        saveAll(reservationDates);

        // when
        List<ReservationDate> actual = reservationDateService.readDates();

        // then
        Assertions.assertThat(actual)
                .hasSize(reservationDates.size());
    }

    @Test
    @DisplayName("등록된 예약날짜와 조회된 예약날짜의 모든 필드는 일치한다")
    void readDate() {
        // given
        ReservationDate saved = reservationDateRepository.save(ReservationDate.create(DEFAULT_DATE));

        // when
        ReservationDate actual = reservationDateService.readDate(saved.id());

        // then
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    @DisplayName("등록되지 않은 예약날짜를 조회하면 예외가 발생한다.")
    void readDate_deregistered() {
        // given
        Long deregisteredId = Long.MIN_VALUE;

        Assertions.assertThatThrownBy(() -> {
                    reservationDateService.readDate(deregisteredId);
                }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되지 않은 예약날짜입니다.");
    }

    @Test
    @DisplayName("예약날짜를 1개 등록하면 예약날짜 데이터 수가 1 증가한다.")
    void register() {
        // given
        List<ReservationDate> emptyDates = List.of();

        // when
        reservationDateService.register(DEFAULT_DATE);

        // then
        Assertions.assertThat(reservationDateRepository.findAll())
                .hasSize(emptyDates.size() + 1);
    }

    @Test
    @DisplayName("등록한 예약날짜와 다시 조회한 예약날짜의 모든 필드가 일치한다.")
    void register_theme_fields_match() {
        // when
        ReservationDate registered = reservationDateService.register(DEFAULT_DATE);

        // then
        assertThat(registered)
                .usingRecursiveComparison()
                .isEqualTo(reservationDateRepository.findById(registered.id()));
    }

    @Test
    @DisplayName("등록된 예약 날짜 2개 중 한 개를 삭제하면 데이터 수는 1개가 된다.")
    void delete() {
        // given
        List<ReservationDate> reservationDates = saveAll(List.of(
                ReservationDate.create(DEFAULT_DATE),
                ReservationDate.create(DEFAULT_DATE.plusDays(1)))
        );

        // when
        reservationDateService.deregister(reservationDates.get(0).id());

        // then
        Assertions.assertThat(reservationDateRepository.findAll())
                .hasSize(reservationDates.size() - 1);
    }

    @Test
    @DisplayName("등록되지않은 예약날짜를 삭제하면 예외가 발생한다.")
    void delete_deregistered() {
        // given
        Long deregisteredId = Long.MIN_VALUE;

        Assertions.assertThatThrownBy(() -> {
                    reservationDateService.deregister(deregisteredId);
                }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되지 않은 예약날짜입니다.");
    }

    private List<ReservationDate> saveAll(List<ReservationDate> dates) {

        List<ReservationDate> saved = new ArrayList<>();
        for (ReservationDate reservationDate : dates) {
            saved.add(reservationDateRepository.save(reservationDate));
        }
        return saved;
    }

}
