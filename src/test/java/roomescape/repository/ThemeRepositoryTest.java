package roomescape.repository;

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Import({H2ReservationRepository.class, H2ReservationTimeRepository.class, H2ThemeRepository.class})
@JdbcTest
class ThemeRepositoryTest {

    final List<Theme> sampleThemes = IntStream.range(1, 9)
            .mapToObj(i -> new Theme(
                    null,
                    "Theme " + i,
                    "Description " + i,
                    "Thumbnail " + i
            )).toList();

    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ReservationTimeRepository reservationTimeRepository;
    @Autowired
    ThemeRepository themeRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("모든 테마 목록을 조회한다.")
    void findAll() {
        // given
        sampleThemes.forEach(themeRepository::save);

        // when
        final List<Theme> actual = themeRepository.findAll();
        final List<Theme> expected = IntStream.range(0, sampleThemes.size())
                .mapToObj(i -> sampleThemes.get(i).assignId(actual.get(i).getId()))
                .toList();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("특정 id를 통해 테마를 조회한다.")
    void findByIdPresent() {
        // given
        final Theme theme = sampleThemes.get(0);
        final Theme savedTheme = themeRepository.save(theme);
        final Long saveId = savedTheme.getId();

        // when
        final Optional<Theme> actual = themeRepository.findById(saveId);
        final Theme expected = theme.assignId(saveId);

        // then
        assertThat(actual).hasValue(expected);
    }

    @Test
    @DisplayName("존재하지 않는 테마를 조회할 경우 빈 값을 반환한다.")
    void findByIdNotExist() {
        // given
        final long notExistId = 1L;

        // when
        final Optional<Theme> actual = themeRepository.findById(notExistId);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("인기 테마 목록을 조회한다.")
    void findPopularThemes() {
        // given
        final ReservationTime time = reservationTimeRepository.save(new ReservationTime(null, "08:00"));
        final Member member = memberRepository.save(new Member(null, "a@b.c", "pw", "User", Role.USER));
        final List<Theme> themes = sampleThemes.stream()
                .map(themeRepository::save)
                .toList();

        Random random = new Random();
        List<Reservation> reservations = new ArrayList<>();
        final int days = 7;
        for (int day = 1; day < days * 2; day++) {
            LocalDate date = LocalDate.now().minusDays(day);
            for (Theme theme : themes) {
                if (random.nextBoolean()) {
                    Reservation reservation = new Reservation(
                            null,
                            date,
                            time,
                            theme,
                            member
                    );
                    reservations.add(reservationRepository.save(reservation));
                }
            }
        }

        final LocalDate from = LocalDate.now().minusDays(days);
        final LocalDate to = LocalDate.now().minusDays(1);
        final int limit = 2;

        // when
        final List<Theme> actual = themeRepository.findPopularThemes(from, to, limit);
        final List<Theme> themesContainExpected = reservations.stream()
                .filter(r -> r.getDate().isAfter(from))
                .map(Reservation::getTheme)
                .distinct()
                .toList();

        // then
        assertThat(actual.size()).isLessThanOrEqualTo(limit);
        assertThat(themesContainExpected.containsAll(actual)).isTrue();
    }

    @Test
    @DisplayName("테마를 저장한다.")
    void save() {
        // given
        final Theme theme = sampleThemes.get(0);

        // when
        final Theme actual = themeRepository.save(theme);
        final Theme expected = theme.assignId(actual.getId());

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("등록된 테마 번호로 삭제한다.")
    void deletePresent() {
        // given
        final Theme theme = sampleThemes.get(0);
        final Theme savedTheme = themeRepository.save(theme);
        final Long existId = savedTheme.getId();

        // when & then
        assertThat(themeRepository.findById(existId)).isPresent();
        assertThat(themeRepository.delete(existId)).isNotZero();
        assertThat(themeRepository.findById(existId)).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 테마를 삭제할 경우 아무런 영향이 없다.")
    void deleteNotExist() {
        // given
        final long notExistId = 1L;

        // when & then
        assertThat(themeRepository.findById(notExistId)).isEmpty();
        assertThat(themeRepository.delete(notExistId)).isZero();
    }
}
