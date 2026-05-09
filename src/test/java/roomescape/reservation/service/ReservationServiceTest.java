package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import roomescape.closeddate.repository.JdbcClosedDateRepository;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationStatus;
import roomescape.reservation.dto.request.ReservationSaveDto;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.JdbcThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.JdbcReservationTimeRepository;

@JdbcTest
class ReservationServiceTest {

    private final String name = "한다";
    private final LocalDate date1 = LocalDate.now().plusWeeks(1);
    private final LocalDate date2 = LocalDate.now().plusWeeks(2);
    private ReservationTime reservationTime1;
    private ReservationTime reservationTime2;
    private Theme theme1;
    private Theme theme2;

    private JdbcReservationRepository reservationRepository;
    private JdbcReservationTimeRepository reservationTimeRepository;
    private JdbcClosedDateRepository closedDateRepository;
    private JdbcThemeRepository themeRepository;
    private ReservationService reservationService;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        reservationRepository = new JdbcReservationRepository(jdbcTemplate);
        reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        closedDateRepository = new JdbcClosedDateRepository(jdbcTemplate);
        themeRepository = new JdbcThemeRepository(jdbcTemplate);

        reservationService = new ReservationService(
                reservationRepository, reservationTimeRepository, closedDateRepository, themeRepository);

        Long timeId1 = reservationTimeRepository.save(ReservationTime.create(LocalTime.of(15, 40)));
        reservationTime1 = reservationTimeRepository.findById(timeId1).get();

        Long timeId2 = reservationTimeRepository.save(ReservationTime.create(LocalTime.of(16, 0)));
        reservationTime2 = reservationTimeRepository.findById(timeId2).get();

        theme1 = themeRepository.save(Theme.create("테마1", "설명1", "썸네일1"));
        theme2 = themeRepository.save(Theme.create("테마2", "설명2", "썸네일2"));
    }

    @Test
    @DisplayName("전체 예약 정보를 가져온다.")
    void readAll() {
        // given
        saveAll(List.of(
                Reservation.create("한다", date1, reservationTime1.startAt(), theme1),
                Reservation.create("송송", date2, reservationTime1.startAt(), theme2)));

        // when
        List<ReservationResponse> actual = reservationService.readAll();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    @DisplayName("나의 예약들을 조회하면 날짜/시간 오름차순으로 정렬해 모두 조회한다.")
    void readAllByName() {
        // given
        List<ReservationResponse> expected = saveAll(
                List.of(
                        Reservation.create(name, date1, reservationTime1.startAt(), theme1),
                        Reservation.create(name, date1, reservationTime2.startAt(), theme1),
                        Reservation.create(name, date2, reservationTime1.startAt(), theme1),
                        Reservation.create(name, date2, reservationTime2.startAt(), theme1))
        ).stream()
                .sorted(Comparator.comparing(ReservationResponse::date).thenComparing(ReservationResponse::time))
                .toList();

        // when
        List<ReservationResponse> actual = reservationService.readAllByName(name);

        // then
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void create() {
        // given & when
        reservationService.create(new ReservationSaveDto("브라운", date1, reservationTime1.id(), theme1.id()));

        // then
        assertThat(reservationService.readAll()).hasSize(1);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간이면 예외를 발생한다.")
    void create_does_not_exist_reservation_time() {
        // given
        Long wrongTimeId = Long.MIN_VALUE;
        ReservationSaveDto command = new ReservationSaveDto(name, date1, wrongTimeId, theme1.id());

        // when & then
        assertThatThrownBy(() -> reservationService.create(command))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");
    }

    @Test
    @DisplayName("예약 생성 시 예약 날짜/시간/테마가 중복되면 예외를 발생한다.")
    void create_duplicate_reservation() {
        // given
        reservationService.create(new ReservationSaveDto("브라운", date1, reservationTime1.id(), theme1.id()));
        ReservationSaveDto duplicateCommand = new ReservationSaveDto("한다", date1, reservationTime1.id(), theme1.id());

        // when & then
        assertThatThrownBy(() -> reservationService.create(duplicateCommand))
                .isInstanceOf(ConflictException.class)
                .hasMessage("해당 날짜/시간/테마는 이미 예약되었습니다.");
    }

    @Test
    @DisplayName("예약을 취소하면 CANCELED 상태가 된다.")
    void updateStatus_canceled() {
        // given
        Long savedId = reservationRepository.save(
                Reservation.create(name, date1, reservationTime1.startAt(), theme1));

        // when
        ReservationResponse actual = reservationService.cancel(savedId);

        // then
        Assertions.assertThat(actual.status()).isEqualTo(ReservationStatus.CANCELED);
    }

    private List<ReservationResponse> saveAll(List<Reservation> reservations) {
        List<ReservationResponse> savedReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            Long savedId = reservationRepository.save(reservation);
            Reservation saved = Reservation.load(savedId, reservation.name(), reservation.date(),
                    reservation.time(), reservation.theme(), reservation.status());
            savedReservations.add(ReservationResponse.from(saved));
        }
        return savedReservations;
    }
}
