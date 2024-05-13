package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.repository.rowmapper.MemberRowMapper;
import roomescape.repository.rowmapper.ReservationRowMapper;
import roomescape.repository.rowmapper.ReservationTimeRowMapper;
import roomescape.repository.rowmapper.ThemeRowMapper;

@TestExecutionListeners(value = {
        DatabaseCleanupListener.class,
        DependencyInjectionTestExecutionListener.class
})
@JdbcTest
class JdbcReservationRepositoryTest {

    @Autowired
    private DataSource dataSource;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;

    private final ReservationTime time1 = new ReservationTime(1L, "15:30");
    private final ReservationTime time2 = new ReservationTime(2L, "17:30");
    private final Theme theme1 = new Theme(1L, "테마이름", "테마내용", "테마썸네일");
    private final Theme theme2 = new Theme(2L, "다른테마", "재밌음", "테마.png");
    private final Member member1 = new Member(1L, "안돌", "admin", "andolemail", "andoleword");
    private final Member member2 = new Member(2L, "아토", "user", "attomail", "attoword");

    private final Reservation reservation1 = new Reservation(
            null, new Member(1L, (Name) null, null, null, null),
            new Theme(1L, null, null, null),
            new ReservationDate("2023-09-08"),
            new ReservationTime(1L, (LocalTime) null));
    private final Reservation reservation2 = new Reservation(
            null, new Member(2L, (Name) null, null, null, null),
            new Theme(2L, null, null, null),
            new ReservationDate("2024-04-22"),
            new ReservationTime(2L, (LocalTime) null));

    @BeforeEach
    void setUp() {
        reservationRepository = new JdbcReservationRepository(dataSource, new ReservationRowMapper());
        reservationTimeRepository = new JdbcReservationTimeRepository(dataSource, new ReservationTimeRowMapper());
        themeRepository = new JdbcThemeRepository(dataSource, new ThemeRowMapper());
        memberRepository = new JdbcMemberRepository(dataSource, new MemberRowMapper());
        initializeTimesAndThemeData();
    }

    private void initializeTimesAndThemeData() {
        reservationTimeRepository.insertReservationTime(time1);
        reservationTimeRepository.insertReservationTime(time2);
        themeRepository.insertTheme(theme1);
        themeRepository.insertTheme(theme2);
        memberRepository.insertMember(member1);
        memberRepository.insertMember(member2);
    }

    @Test
    @DisplayName("저장된 모든 예약 정보를 가져온다")
    void find_all_reservations() {
        reservationRepository.insertReservation(reservation1);
        reservationRepository.insertReservation(reservation2);

        List<Reservation> allReservations = reservationRepository.findAllReservations();

        assertThat(allReservations.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("예약을 저장한다.")
    void save_reservation() {
        Reservation reservation = reservationRepository.insertReservation(reservation2);

        assertAll(
                () -> assertThat(reservation.getName()).isEqualTo("아토"),
                () -> assertThat(reservation.getTheme().getId()).isEqualTo(2),
                () -> assertThat(reservation.getTheme().getName()).isEqualTo("다른테마"),
                () -> assertThat(reservation.getTheme().getDescription()).isEqualTo("재밌음"),
                () -> assertThat(reservation.getTheme().getThumbnail()).isEqualTo("테마.png"),
                () -> assertThat(reservation.getDate()).isEqualTo("2024-04-22"),
                () -> assertThat(reservation.getTimeId()).isEqualTo(2),
                () -> assertThat(reservation.getTimeStartAt()).isEqualTo("17:30")
        );
    }

    @Test
    @DisplayName("예약을 id로 삭제한다.")
    void delete_reservation_by_id() {
        reservationRepository.insertReservation(reservation1);
        int beforeSize = reservationRepository.findAllReservations().size();

        reservationRepository.deleteReservationById(1L);
        int afterSize = reservationRepository.findAllReservations().size();

        assertAll(
                () -> assertThat(beforeSize).isEqualTo(1),
                () -> assertThat(afterSize).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("테이블에 예약 존재 여부를 판단한다.")
    void is_exist_reservation() {
        reservationRepository.insertReservation(reservation1);

        boolean exist = reservationRepository.isExistReservationOf(1L);
        boolean notExist = reservationRepository.isExistReservationOf(2L);

        assertAll(
                () -> assertThat(exist).isTrue(),
                () -> assertThat(notExist).isFalse()
        );
    }

    @Test
    @DisplayName("예약 테이블에 테마, 시간, 날짜가 동일한 예약이 존재하는지 판단한다.")
    void is_exist_reservation_for_theme_at_date_time() {
        Reservation allSameReservation = new Reservation(
                3L, new Member("atto"), theme1, new ReservationDate("2023-09-08"), time1);

        reservationRepository.insertReservation(reservation1);

        boolean allSame = reservationRepository.hasSameReservationForThemeAtDateTime(allSameReservation);

        assertThat(allSame).isTrue();
    }
}
