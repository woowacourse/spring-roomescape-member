package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.error.NotFoundException;

@JdbcTest
@Import({
        JdbcReservationRepository.class,
        JdbcMemberRepository.class,
        JdbcThemeRepository.class,
        JdbcReservationTimeRepository.class
})
class JdbcReservationRepositoryTest {

    @Autowired
    private JdbcReservationRepository sut;

    @Autowired
    private JdbcMemberRepository memberRepository;

    @Autowired
    private JdbcThemeRepository themeRepository;

    @Autowired
    private JdbcReservationTimeRepository timeRepository;

    private Member savedMember;
    private ReservationTime savedTime;
    private Theme savedTheme;

    @BeforeEach
    void setUp() {
        savedMember = memberRepository.save(new Member(null, "홍길동", "hong@example.com", "pw123", Role.USER));
        savedTime = timeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        savedTheme = themeRepository.save(new Theme(null, "미스터리 방", "공포 테마", "thumb.jpg"));
    }

    @DisplayName("예약이 올바르게 생성된다")
    @Test
    void save() {
        // given
        var reservation = new Reservation(null, savedMember, LocalDate.of(2025, 7, 1), savedTime, savedTheme);

        // when
        var saved = sut.save(reservation);

        // then
        assertSoftly(soft -> {
            soft.assertThat(saved.getId()).isNotNull();
            soft.assertThat(saved.getMember()).isEqualTo(savedMember);
            soft.assertThat(saved.getDate()).isEqualTo(LocalDate.of(2025, 7, 1));
            soft.assertThat(saved.getTime()).isEqualTo(savedTime);
            soft.assertThat(saved.getTheme()).isEqualTo(savedTheme);
        });
    }

    @DisplayName("동일한 날짜, 시간, 테마의 예약이 존재하는지 확인할 수 있다")
    @Test
    void existsByDateAndTimeAndTheme() {
        // given
        sut.save(new Reservation(null, savedMember, LocalDate.of(2025, 7, 1), savedTime, savedTheme));

        // when // then
        assertSoftly(soft -> {
            soft.assertThat(sut.existsByDateAndTimeAndTheme(
                    LocalDate.of(2025, 7, 1), savedTime.getStartAt(), savedTheme.getId())).isTrue();
            soft.assertThat(sut.existsByDateAndTimeAndTheme(
                    LocalDate.of(2026, 1, 1), LocalTime.of(14, 0), savedTheme.getId())).isFalse();
        });
    }

    @DisplayName("모든 예약을 조회할 수 있다")
    @Test
    void findAll() {
        // given
        sut.save(new Reservation(null, savedMember, LocalDate.of(2025, 7, 1), savedTime, savedTheme));
        sut.save(new Reservation(null, savedMember, LocalDate.of(2025, 7, 2), savedTime, savedTheme));

        // when
        var found = sut.findAll();

        // then
        assertThat(found).hasSize(2);
    }

    @DisplayName("예약 시간 ID로 예약 존재 여부를 확인할 수 있다")
    @Test
    void existsByReservationTimeId() {
        // given
        sut.save(new Reservation(null, savedMember, LocalDate.of(2025, 8, 15), savedTime, savedTheme));

        // when
        var exists = sut.existsByReservationTimeId(savedTime.getId());

        // then
        assertThat(exists).isTrue();
    }

    @DisplayName("예약 테마 ID로 예약 존재 여부를 확인할 수 있다")
    @Test
    void existsByThemeId() {
        // given
        sut.save(new Reservation(null, savedMember, LocalDate.of(2025, 8, 15), savedTime, savedTheme));

        // when
        var exists = sut.existsByThemeId(savedTheme.getId());

        // then
        assertThat(exists).isTrue();
    }

    @DisplayName("예약 ID로 예약을 가져온다")
    @Test
    void findById() {
        // given
        var savedReservation = sut.save(new Reservation(null, savedMember, LocalDate.of(2025, 8, 15), savedTime, savedTheme));

        // when
        var foundReservation = sut.findById(savedReservation.getId()).get();

        // then
        assertThat(foundReservation).isEqualTo(savedReservation);
    }

    @DisplayName("예약 ID로 예약을 제거한다")
    @Test
    void deleteById() {
        // given
        var savedReservation = sut.save(new Reservation(null, savedMember, LocalDate.of(2025, 8, 15), savedTime, savedTheme));

        // when
        sut.deleteById(savedReservation.getId());

        // then
        assertThat(sut.findById(savedReservation.getId())).isEmpty();
    }

    @DisplayName("예약 ID로 예약을 제거할 때 예약이 존재하지 않으면 예외를 던진다")
    @Test
    void deleteById_not_found() {
        // when // then
        assertThatThrownBy(() -> sut.deleteById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("삭제하려고 하는 예약이 존재하지 않습니다. 999");
    }

    @DisplayName("조건에 따라 예약을 조회할 수 있다")
    @Test
    void search() {
        // given
        var otherMember = memberRepository.save(new Member(null, "김철수", "kim@example.com", "pw456", Role.USER));
        var otherTime = timeRepository.save(new ReservationTime(null, LocalTime.of(11, 0)));
        var otherTheme = themeRepository.save(new Theme(null, "공포의 방", "무서운 테마", "scary.jpg"));

        sut.save(new Reservation(null, savedMember, LocalDate.of(2025, 7, 1), savedTime, savedTheme));
        sut.save(new Reservation(null, otherMember, LocalDate.of(2025, 7, 2), savedTime, savedTheme));
        sut.save(new Reservation(null, savedMember, LocalDate.of(2025, 7, 3), otherTime, otherTheme));

        // when
        var resultsByTheme = sut.search(savedTheme.getId(), null, null, null);
        var resultsByMember = sut.search(null, savedMember.getId(), null, null);
        var resultsByDate = sut.search(null, null, LocalDate.of(2025, 7, 2), LocalDate.of(2025, 7, 3));
        var resultByThemeAndMemberAndDate = sut.search(savedTheme.getId(), savedMember.getId(),
                LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 3));
        var resultsByNull = sut.search(null, null, null, null);

        // then
        assertSoftly(soft -> {
            soft.assertThat(resultsByTheme).hasSize(2);
            soft.assertThat(resultsByMember).hasSize(2);
            soft.assertThat(resultsByDate).hasSize(2);
            soft.assertThat(resultByThemeAndMemberAndDate).hasSize(1);
            soft.assertThat(resultsByNull).hasSize(3);
        });
    }
}
