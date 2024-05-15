package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;

@Import({H2ReservationRepository.class,
        H2ReservationTimeRepository.class,
        H2ThemeRepository.class,
        H2MemberRepository.class})
@JdbcTest
class ReservationRepositoryTest {

    List<Reservation> sampleReservations = List.of(
            new Reservation(null, LocalDate.now().minusDays(1), null, null, null),
            new Reservation(null, LocalDate.now(), null, null, null),
            new Reservation(null, LocalDate.now().plusDays(1), null, null, null)
    );
    List<ReservationTime> sampleTimes = List.of(
            new ReservationTime(null, "08:00"),
            new ReservationTime(null, "09:10")
    );
    List<Theme> sampleThemes = List.of(
            new Theme(null, "Theme 1", "Description 1", "Thumbnail 1"),
            new Theme(null, "Theme 2", "Description 2", "Thumbnail 2")
    );
    List<Member> sampleMembers = List.of(
            new Member(null, "User", "user@test.com", "user", Role.USER),
            new Member(null, "Admin", "admin@test.com", "admin", Role.ADMIN)
    );

    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ReservationTimeRepository reservationTimeRepository;
    @Autowired
    ThemeRepository themeRepository;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        sampleTimes = sampleTimes.stream()
                .map(reservationTimeRepository::save)
                .toList();
        sampleThemes = sampleThemes.stream()
                .map(themeRepository::save)
                .toList();
        sampleMembers = sampleMembers.stream()
                .map(memberRepository::save)
                .toList();
        sampleReservations = IntStream.range(0, sampleReservations.size())
                .mapToObj(i -> sampleReservations.get(i)
                        .assignTime(sampleTimes.get(i % sampleTimes.size()))
                        .assignTheme(sampleThemes.get(i % sampleThemes.size()))
                        .assignMember(sampleMembers.get(i % sampleMembers.size()))
                ).toList();
    }

    @Test
    @DisplayName("모든 예약 목록을 조회한다.")
    void findAll() {
        // given
        sampleReservations.forEach(reservationRepository::save);

        // when
        List<Reservation> actual = reservationRepository.findAll();
        List<Reservation> expected = IntStream.range(0, sampleReservations.size())
                .mapToObj(i -> sampleReservations.get(i).assignId(actual.get(i).getId()))
                .toList();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("날짜와 테마 아이디로 모든 예약을 조회한다.")
    void findAllByDateAndThemeId() {
        // given
        List<Reservation> savedReservations = sampleReservations.stream()
                .map(reservationRepository::save)
                .toList();
        LocalDate date = savedReservations.get(0).getDate();
        Long themeId = savedReservations.get(0).getTheme().getId();

        // when
        List<Reservation> actual = reservationRepository.findAllByDateAndThemeId(date, themeId);
        List<Reservation> expected = savedReservations.stream()
                .filter(reservation -> reservation.getDate().equals(date) &&
                        reservation.getTheme().getId().equals(themeId))
                .map(reservation -> reservation
                        .assignTheme(new Theme(reservation.getTheme().getId()))
                        .assignMember(new Member(reservation.getMember().getId())))
                .toList();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("특정 id를 통해 예약을 조회한다.")
    void findByIdPresent() {
        // given
        Reservation reservation = sampleReservations.get(0);
        Reservation savedReservation = reservationRepository.save(reservation);
        Long savedId = savedReservation.getId();

        // when
        Optional<Reservation> actual = reservationRepository.findById(savedId);
        Reservation expected = reservation
                .assignId(savedId)
                .assignTime(new ReservationTime(reservation.getTime().getId()))
                .assignTheme(new Theme(reservation.getTheme().getId()))
                .assignMember(new Member(reservation.getMember().getId()));

        // then
        assertThat(actual).hasValue(expected);
    }

    @Test
    @DisplayName("존재하지 않는 예약을 조회할 경우 빈 값을 반환한다.")
    void findByIdNotExist() {
        // given
        long notExistId = 1L;

        // when
        Optional<Reservation> actual = reservationRepository.findById(notExistId);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("테마와 멤버와 날짜 범위로 예약을 조회한다.")
    void findAllByThemeIdAndMemberIdAndDateRange() {
        // given
        sampleReservations.forEach(reservationRepository::save);
        long themeId = 1L;
        long memberId = 1L;
        LocalDate dateFrom = sampleReservations.get(0).getDate();
        LocalDate dateTo = sampleReservations.get(sampleReservations.size() - 1).getDate();

        // when
        List<Reservation> actual = reservationRepository.findAllByThemeIdAndMemberIdAndDateRange(
                themeId, memberId, dateFrom.toString(), dateTo.toString());
        List<Reservation> expected = sampleReservations.stream()
                .filter(r -> r.getTheme().getId() == themeId)
                .filter(r -> r.getMember().getId() == memberId)
                .filter(r -> r.getDate().isAfter(dateFrom) || r.getDate().isEqual(dateFrom))
                .filter(r -> r.getDate().isBefore(dateTo) || r.getDate().isEqual(dateTo))
                .collect(Collectors.toList());

        for (int i = 0; i < expected.size(); i++) {
            expected.set(i, expected.get(i).assignId(actual.get(i).getId()));
        }

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("등록된 시간 아이디로 예약 존재 여부를 확인한다.")
    void existsByTimeIdPresent() {
        // given
        Reservation reservation = sampleReservations.get(0);
        reservationRepository.save(reservation);
        long existTimeId = reservation.getTime().getId();

        // when & then
        assertThat(reservationRepository.existsByTimeId(existTimeId)).isTrue();
    }

    @Test
    @DisplayName("등록되지 않은 시간 아이디로 예약 존재 여부를 확인한다.")
    void existsByTimeIdNotExist() {
        // given
        long notExistTimeId = 0L;

        // when & then
        assertThat(reservationRepository.existsByTimeId(notExistTimeId)).isFalse();
    }

    @Test
    @DisplayName("등록된 테마 아이디로 예약 존재 여부를 확인한다.")
    void existsByThemeIdPresent() {
        // given
        Reservation reservation = sampleReservations.get(0);
        reservationRepository.save(reservation);
        long existThemeId = reservation.getTheme().getId();

        // when & then
        assertThat(reservationRepository.existsByThemeId(existThemeId)).isTrue();
    }

    @Test
    @DisplayName("등록되지 않은 테마 아이디로 예약 존재 여부를 확인한다.")
    void existsByThemeIdNotExist() {
        // given
        long notExistThemeId = 0L;

        // when & then
        assertThat(reservationRepository.existsByThemeId(notExistThemeId)).isFalse();
    }

    @Test
    @DisplayName("해당 날짜와 시간 그리고 테마에 예약이 되어 있는지 확인한다.")
    void existsByDateAndTimeIdAndThemeId() {
        // given
        sampleReservations.forEach(reservationRepository::save);
        Reservation reservation = sampleReservations.get(0);

        // when
        boolean actual = reservationRepository.existsByDateAndTimeIdAndThemeId(
                reservation.getDate(),
                reservation.getTime().getId(),
                reservation.getTheme().getId()
        );

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("예약 정보를 저장한다.")
    void save() {
        // given
        Reservation reservation = sampleReservations.get(0);

        // when
        Reservation actual = reservationRepository.save(reservation);
        Reservation expected = reservation.assignId(actual.getId());

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간으로 예약을 저장하면 예외가 발생한다.")
    void exceptionOnSavingWithNotExistTime() {
        // given
        long notExistTimeId = sampleTimes.stream()
                .map(ReservationTime::getId)
                .max(Long::compare)
                .orElse(0L) + 1;
        Reservation reservation = sampleReservations.get(0)
                .assignTime(new ReservationTime(notExistTimeId));

        // when & then
        assertThatCode(() -> reservationRepository.save(reservation))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("존재하지 않는 테마 정보로 예약을 저장하면 예외가 발생한다.")
    void exceptionOnSavingWithNotExistTheme() {
        // given
        long notExistTimeId = sampleThemes.stream()
                .map(Theme::getId)
                .max(Long::compare)
                .orElse(0L) + 1;
        Reservation reservation = sampleReservations.get(0)
                .assignTheme(new Theme(notExistTimeId));

        // when & then
        assertThatCode(() -> reservationRepository.save(reservation))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("등록된 예약 번호로 삭제한다.")
    void deletePresent() {
        // given
        Reservation reservation = sampleReservations.get(0);
        Reservation savedReservation = reservationRepository.save(reservation);
        Long existId = savedReservation.getId();

        // when & then
        assertThat(reservationRepository.findById(existId)).isPresent();
        assertThat(reservationRepository.delete(existId)).isNotZero();
        assertThat(reservationRepository.findById(existId)).isEmpty();
    }

    @Test
    @DisplayName("없는 예약 번호로 삭제할 경우 아무런 영향이 없다.")
    void deleteNotExist() {
        // given
        long nonExistId = 1L;

        // when & then
        assertThat(reservationRepository.findById(nonExistId)).isEmpty();
        assertThat(reservationRepository.delete(nonExistId)).isZero();
    }

    @Test
    @DisplayName("등록된 예약 번호와 멤버 아이디로 삭제한다.")
    void deleteByMemberIdPresent() {
        // given
        Reservation reservation = sampleReservations.get(0);
        Reservation savedReservation = reservationRepository.save(reservation);
        Long existId = savedReservation.getId();
        Long memberId = reservation.getMember().getId();

        // when & then
        assertThat(reservationRepository.findById(existId)).isPresent();
        assertThat(reservationRepository.deleteByMemberId(existId, memberId)).isNotZero();
        assertThat(reservationRepository.findById(existId)).isEmpty();
    }
}
