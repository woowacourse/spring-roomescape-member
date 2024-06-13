package roomescape.domain.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberRole;
import roomescape.domain.member.repository.JdbcMemberRepository;
import roomescape.domain.member.repository.MemberRepository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.repository.JdbcThemeRepository;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.ReservationTime;
import roomescape.domain.time.repository.JdbcReservationTimeRepository;
import roomescape.fixture.ThemeFixture;

@JdbcTest
@Sql("/clear.sql")
class JdbcReservationRepositoryTest {
    private final JdbcReservationRepository reservationRepository;
    private final JdbcReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private ReservationTime time;
    private Theme theme;
    private Member member;

    @Autowired
    JdbcReservationRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.reservationRepository = new JdbcReservationRepository(jdbcTemplate);
        this.reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        this.themeRepository = new JdbcThemeRepository(jdbcTemplate);
        this.memberRepository = new JdbcMemberRepository(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        time = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("12:00")));
        theme = themeRepository.save(ThemeFixture.theme());
        member = memberRepository.save(new Member(new MemberName("레모네"),
                "lemone@wooteco.com", "lemone1234", MemberRole.ADMIN));
    }

    @Test
    void 예약을_저장한다() {
        LocalDate date = LocalDate.now().plusDays(2);
        Reservation reservation = new Reservation(
                member,
                date,
                time,
                theme
        );

        Reservation savedReservation = reservationRepository.save(reservation);
        Reservation findReservation = reservationRepository.findById(savedReservation.getId()).get();

        assertAll(
                () -> assertThat(reservationRepository.findAll()).hasSize(1),
                () -> assertThat(findReservation.getId()).isEqualTo(savedReservation.getId()),
                () -> assertThat(findReservation.getMember().getName().getValue())
                        .isEqualTo(savedReservation.getMember().getName().getValue()),
                () -> assertThat(findReservation.getDate()).isEqualTo(date),
                () -> assertThat(findReservation.getTimeId()).isEqualTo(time.getId()),
                () -> assertThat(findReservation.getStartAtTime()).isEqualTo(time.getStartAt()),
                () -> assertThat(findReservation.getTheme().getId()).isEqualTo(theme.getId()),
                () -> assertThat(findReservation.getTheme().getName()).isEqualTo(theme.getName())
        );
    }

    @Test
    void 모든_예약을_조회한다() {
        LocalDate lemoneDate = LocalDate.now().plusDays(2);
        LocalDate prinDate = LocalDate.now().plusDays(3);
        Reservation reservationLemone = new Reservation(
                member,
                lemoneDate,
                time,
                theme
        );
        Member prin = memberRepository.save(new Member(new MemberName("프린"),
                "prin@wooteco.com", "prin1234", MemberRole.ADMIN));
        Reservation reservationPrin = new Reservation(
                prin,
                prinDate,
                time,
                theme);
        Reservation savedLemoneReservation = reservationRepository.save(reservationLemone);
        Reservation savedPrinReservation = reservationRepository.save(reservationPrin);

        List<Reservation> reservations = reservationRepository.findAll();

        assertThat(reservations).hasSize(2);

        Reservation foundLemoneReservation = reservations.get(0);
        Reservation foundPrinReservation = reservations.get(1);
        assertAll(
                () -> assertThat(foundLemoneReservation.getMember().getName().getValue())
                        .isEqualTo(savedLemoneReservation.getMember().getName().getValue()),

                () -> assertThat(foundLemoneReservation.getTimeId())
                        .isEqualTo(savedLemoneReservation.getId()),

                () -> assertThat(foundPrinReservation.getMember().getName().getValue())
                        .isEqualTo(savedPrinReservation.getMember().getName().getValue()),

                () -> assertThat(foundPrinReservation.getTimeId())
                        .isEqualTo(savedPrinReservation.getTime().getId())
        );
    }

    @Test
    void 예약날짜와_시간이_이미_존재하면_true를_반환한다() {
        LocalDate date = LocalDate.now().plusDays(2);
        Reservation reservation = new Reservation(
                member,
                date,
                time,
                theme
        );
        reservationRepository.save(reservation);

        boolean exists = reservationRepository.existsByReservationDateTimeAndTheme(date, time.getId(),
                theme.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void 예약날짜와_시간이_존재하지_않으면_false를_반환한다() {
        LocalDate date = LocalDate.now().plusDays(2);
        Reservation reservation = new Reservation(
                member,
                date,
                time,
                theme
        );
        reservationRepository.save(reservation);

        boolean exists = reservationRepository.existsByReservationDateTimeAndTheme(date.plusDays(2),
                time.getId(), theme.getId());

        assertThat(exists).isFalse();
    }

    @Test
    void 예약을_삭제한다() {
        LocalDate date = LocalDate.now().plusDays(2);
        Reservation reservation = new Reservation(
                member,
                date,
                time,
                theme
        );
        reservation = reservationRepository.save(reservation);

        reservationRepository.deleteById(reservation.getId());

        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).isEmpty();
    }

    @Test
    void 특정_날짜의_특정_테마의_특정_시간이_예약되어_있는지_확인한다() {
        LocalDate date = LocalDate.now().plusDays(2);
        Reservation reservation = new Reservation(
                member,
                date,
                time,
                theme
        );
        LocalDate notBookedDate = LocalDate.now().plusDays(3);
        Member notBookedMember = memberRepository.save(
                new Member(new MemberName("프린"), "prin@wooteco.com", "prin1234", MemberRole.ADMIN));
        Reservation notBookedReservation = new Reservation(
                notBookedMember,
                notBookedDate,
                reservationTimeRepository.save(new ReservationTime(LocalTime.parse("19:12"))),
                themeRepository.save(new Theme("예약안함", "설명: 노 예약입니다.", "http://test.jpg")));

        reservationRepository.save(reservation);

        boolean alreadyBooked = reservationRepository.existsByReservationDateTimeAndTheme(date,
                time.getId(), theme.getId());
        boolean alreadyBooked_2 = reservationRepository.existsByReservationDateTimeAndTheme(notBookedDate,
                notBookedReservation.getTimeId(), notBookedReservation.getTheme().getId());

        assertThat(alreadyBooked).isTrue();
        assertThat(alreadyBooked_2).isFalse();
    }
}
