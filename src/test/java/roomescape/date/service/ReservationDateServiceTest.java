package roomescape.date.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import roomescape.date.domain.ReservationDate;
import roomescape.date.repository.JdbcReservationDateRepository;

@JdbcTest
class ReservationDateServiceTest {
    private static final LocalDate DEFAULT_DATE = LocalDate.of(2099, 1, 1);

    private JdbcReservationDateRepository reservationDateRepository;
    private ReservationDateService reservationDateService;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        reservationDateRepository = new JdbcReservationDateRepository(jdbcTemplate);
        reservationDateService = new ReservationDateService(reservationDateRepository);
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

        assertThatThrownBy(() -> {
            reservationDateService.readDate(deregisteredId);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되지 않은 예약날짜입니다.");
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
    @DisplayName("오늘 이후 예약 날짜 데이터를 조회할 수 있다.")
    void readDatesAfterToday() {
        // given
        LocalDate pastDate = LocalDate.of(2000, 1, 1);

        reservationDateRepository.save(ReservationDate.load(1L, pastDate));
        ReservationDate future1 = reservationDateRepository.save(ReservationDate.create(DEFAULT_DATE));
        ReservationDate future2 = reservationDateRepository.save(ReservationDate.create(DEFAULT_DATE.plusDays(1)));

        // when
        List<ReservationDate> actual = reservationDateService.readDatesAfterToday();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(List.of(future1, future2));
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
                .isEqualTo(reservationDateRepository.findById(registered.id()).get());
    }

    @Test
    @DisplayName("등록된 예약 날짜 2개 중 한 개를 비활성화하면 예약 날짜 데이터 수는 1개가 된다.")
    void deregister() {
        // given
        List<ReservationDate> reservationDates = saveAll(List.of(
                ReservationDate.create(DEFAULT_DATE),
                ReservationDate.create(DEFAULT_DATE.plusDays(1)))
        );

        // when
        reservationDateService.deregister(reservationDates.get(0).id());

        // then
        assertThat(reservationDateRepository.findAll())
                .hasSize(reservationDates.size() - 1);
    }

    @Test
    @DisplayName("등록되지않은 예약날짜를 삭제하면 예외가 발생한다.")
    void deregister_not_exists() {
        // given
        Long deregisteredId = Long.MIN_VALUE;

        // when & then
        assertThatThrownBy(() -> reservationDateService.deregister(deregisteredId))
                .isInstanceOf(IllegalArgumentException.class)
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
