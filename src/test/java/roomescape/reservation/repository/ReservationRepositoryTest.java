package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.reservation.fixture.ReservationFixture.canceledReservation;
import static roomescape.reservation.fixture.ReservationFixture.reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationStatus;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.JdbcThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.fixture.ReservationTimeFixture;
import roomescape.time.repository.JdbcReservationTimeRepository;

@JdbcTest
class ReservationRepositoryTest {

    private final String name = "한다";
    private final LocalDate date1 = LocalDate.of(2099, 1, 1);
    private final LocalDate date2 = LocalDate.of(2099, 9, 1);
    private ReservationDate reservationDate1;
    private ReservationDate reservationDate2;
    private ReservationTime reservationTime1;
    private ReservationTime reservationTime2;
    private Theme theme;

    private JdbcReservationRepository jdbcReservationRepository;
    private JdbcReservationTimeRepository jdbcReservationTimeRepository;
    private JdbcReservationDateRepository jdbcReservationDateRepository;
    private JdbcThemeRepository jdbcThemeRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcReservationRepository = new JdbcReservationRepository(jdbcTemplate);
        jdbcReservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        jdbcReservationDateRepository = new JdbcReservationDateRepository(jdbcTemplate);
        jdbcThemeRepository = new JdbcThemeRepository(jdbcTemplate);

        ReservationTime time1 = jdbcReservationTimeRepository.save(ReservationTimeFixture.time15());
        ReservationTime time2 = jdbcReservationTimeRepository.save(ReservationTimeFixture.time16());
        reservationTime1 = jdbcReservationTimeRepository.findById(time1.id()).get();
        reservationTime2 = jdbcReservationTimeRepository.findById(time2.id()).get();

        reservationDate1 = jdbcReservationDateRepository.save(ReservationDate.create(date1));
        reservationDate2 = jdbcReservationDateRepository.save(ReservationDate.create(date2));
        theme = jdbcThemeRepository.save(Theme.create("테마", "설명", "썸네일"));
    }

    @Test
    @DisplayName("모든 예약 정보를 조회한다.")
    void findAll() {
        // given
        List<Reservation> reservations = List.of(
                Reservation.create(name, reservationDate1.date(), reservationTime1.startAt(), theme),
                Reservation.create(name, reservationDate1.date(), reservationTime2.startAt(), theme)
        );
        saveAll(reservations);

        // when
        List<Reservation> actual = jdbcReservationRepository.findAll();

        // then
        assertThat(actual)
                .hasSize(reservations.size());
    }

    @Test
    @DisplayName("나의 예약들을 조회하면 날짜/시간 오름차순으로 정렬해 모두 조회한다.")
    void findAllByName() {
        // given
        List<Reservation> reservations = saveAll(List.of(
                Reservation.create(name, reservationDate1.date(), reservationTime1.startAt(), theme),
                Reservation.create(name, reservationDate1.date(), reservationTime2.startAt(), theme),
                Reservation.create(name, reservationDate2.date(), reservationTime1.startAt(), theme),
                Reservation.create(name, reservationDate2.date(), reservationTime2.startAt(), theme))
        );
        Collections.sort(reservations, Comparator.comparing(Reservation::date).thenComparing(Reservation::status));

        // when
        List<Reservation> actual = jdbcReservationRepository.findAllByNameOrderByDateAndTime(name);

        // then
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(reservations);
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void save() {
        // given
        List<Reservation> emptyReservations = List.of();

        // when
        jdbcReservationRepository.save(reservation(name, reservationDate1, reservationTime1, theme));

        // then
        assertThat(jdbcReservationRepository.findAll())
                .hasSize(emptyReservations.size() + 1);
    }

    @Test
    @DisplayName("예약 날짜와 시간 ID 정보로 존재하는지 확인한다.")
    void exitsByDateAndTimeId() {
        // given
        save(reservation(name, reservationDate1, reservationTime1, theme));
        LocalDate wrongDate = LocalDate.of(2000, 11, 4);

        // when & then
        assertThat(jdbcReservationRepository.existsByDateAndTimeAndThemeId(reservationDate1.date(), reservationTime1.startAt(), theme.id()))
                .isTrue();
        assertThat(jdbcReservationRepository.existsByDateAndTimeAndThemeId(wrongDate, reservationTime1.startAt(), theme.id()))
                .isFalse();
    }

    @Test
    @DisplayName("예약을 취소하면 상태가 CANCELED가 된다.")
    void updateState_canceled() {
        // given
        Reservation canceledReservation = save(canceledReservation(name, reservationDate1, reservationTime1, theme));
        updateStatus(canceledReservation);

        // when
        Reservation afterReservation = jdbcReservationRepository.findById(canceledReservation.id()).get();

        // then
        Assertions.assertThat(afterReservation.status())
                .isEqualTo(ReservationStatus.CANCELED);
    }

    private List<Reservation> saveAll(List<Reservation> reservations) {
        List<Reservation> savedReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            savedReservations.add(save(reservation));
        }
        return savedReservations;
    }

    private Reservation save(Reservation reservation) {
        return jdbcReservationRepository.save(reservation);
    }

    private void updateStatus(Reservation beforeReservation) {
        beforeReservation.updateStatus(ReservationStatus.CANCELED);
        jdbcReservationRepository.updateStatus(beforeReservation);
    }

}
