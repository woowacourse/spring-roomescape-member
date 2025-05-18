package roomescape.infra;

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
import roomescape.domain.entity.Member;
import roomescape.domain.entity.Reservation;
import roomescape.domain.entity.ReservationTime;
import roomescape.domain.entity.Role;
import roomescape.domain.entity.Theme;
import roomescape.error.NotFoundException;

@JdbcTest
@Import({
        JdbcReservationTimeRepository.class,
        JdbcMemberRepository.class,
        JdbcThemeRepository.class,
        JdbcReservationRepository.class
})
class JdbcReservationTimeRepositoryTest {

    @Autowired
    private JdbcReservationTimeRepository sut;

    @Autowired
    private JdbcReservationRepository reservationRepository;

    @Autowired
    private JdbcMemberRepository memberRepository;

    @Autowired
    private JdbcThemeRepository themeRepository;

    private Member savedMember;
    private Theme savedTheme;

    @BeforeEach
    void setUp() {
        savedMember = memberRepository.save(new Member(null, "홍길동", "hong@example.com", "pw123", Role.USER));
        savedTheme = themeRepository.save(new Theme(null, "이름1", "설명1", "썸네일1"));
    }

    @DisplayName("예약 시간을 올바르게 저장한다")
    @Test
    void save() {
        // given
        var reservationTime = new ReservationTime(LocalTime.of(11, 0));

        // when
        var saved = sut.save(reservationTime);

        // then
        assertSoftly(soft -> {
            soft.assertThat(saved.getId()).isNotNull();
            soft.assertThat(saved.getStartAt()).isEqualTo(reservationTime.getStartAt());
        });
    }

    @DisplayName("모든 예약 시간을 조회한다")
    @Test
    void findAll() {
        // given
        sut.save(new ReservationTime(LocalTime.of(9, 0)));
        sut.save(new ReservationTime(LocalTime.of(10, 0)));

        // when
        var founds = sut.findAll();

        // then
        assertThat(founds).hasSize(2);
    }

    @DisplayName("예약 가능한 모든 시간을 조회한다")
    @Test
    void findAllAvailable() {
        // given
        var time1 = sut.save(new ReservationTime(LocalTime.of(10, 0)));
        var time2 = sut.save(new ReservationTime(LocalTime.of(11, 0)));

        reservationRepository.save(new Reservation(null, savedMember, LocalDate.of(2999, 5, 1), time1, savedTheme));
        reservationRepository.save(new Reservation(null, savedMember, LocalDate.of(2999, 5, 1), time2, savedTheme));

        // when
        var availableTimes = sut.findAllAvailable(LocalDate.of(2999, 5, 1), savedTheme.getId());

        // then
        assertSoftly(soft -> {
            soft.assertThat(availableTimes.get(0).alreadyBooked()).isTrue();
            soft.assertThat(availableTimes.get(1).alreadyBooked()).isTrue();
        });
    }

    @DisplayName("ID에 알맞은 예약 시간을 삭제한다")
    @Test
    void deleteById() {
        // given
        var time = sut.save(new ReservationTime(LocalTime.of(10, 0)));

        // when
        sut.deleteById(time.getId());
        var remaining = sut.findAll();

        // then
        assertThat(remaining).isEmpty();
    }

    @DisplayName("ID에 알맞은 예약 시간을 삭제할 때 존재하지 않는 ID를 전달하면 예외가 발생한다")
    @Test
    void deleteById_not_found() {
        // when & then
        assertThatThrownBy(() -> sut.deleteById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("삭제할 예약 시간이 없습니다. id=999");
    }

    @DisplayName("ID에 알맞은 예약 시간을 가져온다")
    @Test
    void findById() {
        // given
        var time = sut.save(new ReservationTime(LocalTime.of(10, 0)));

        // when
        var found = sut.findById(time.getId()).get();

        // then
        assertThat(found).isEqualTo(time);
    }
}
