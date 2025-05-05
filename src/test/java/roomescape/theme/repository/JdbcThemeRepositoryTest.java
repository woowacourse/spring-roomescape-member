package roomescape.theme.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.reservation.fixture.ReservationFixture;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.fixture.ReservationTimeFixture;
import roomescape.reservationTime.repository.JdbcReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.dto.PopularThemeRequestDto;

@JdbcTest
@Import({JdbcThemeRepository.class, JdbcReservationRepository.class, JdbcReservationTimeRepository.class})
class JdbcThemeRepositoryTest {

    @Autowired
    private JdbcThemeRepository repository;
    @Autowired
    private JdbcReservationTimeRepository reservationTimeRepository;
    @Autowired
    private JdbcReservationRepository reservationRepository;

    @BeforeEach
    public void beforeEach() {
        Theme savedTheme1 = repository.add(new Theme("name1", "dd1", "tt1"));
        Theme savedTheme2 = repository.add(new Theme("name2", "dd2", "tt2"));
        Theme savedTheme3 = repository.add(new Theme("name3", "dd3", "tt3"));
        Theme savedTheme4 = repository.add(new Theme("name4", "dd4", "tt4"));
        Theme savedTheme5 = repository.add(new Theme("name5", "dd5", "tt5"));
        Theme savedTheme6 = repository.add(new Theme("name6", "dd6", "tt6"));
        Theme savedTheme7 = repository.add(new Theme("name7", "dd7", "tt7"));
        Theme savedTheme8 = repository.add(new Theme("name8", "dd8", "tt8"));
        Theme savedTheme9 = repository.add(new Theme("name9", "dd9", "tt9"));
        Theme savedTheme10 = repository.add(new Theme("name10", "dd10", "tt10"));
        Theme savedTheme11 = repository.add(new Theme("name11", "dd11", "tt11"));

        ReservationTime savedTime1 = reservationTimeRepository.add(ReservationTimeFixture.create(LocalTime.of(11, 0)));
        ReservationTime savedTime2 = reservationTimeRepository.add(ReservationTimeFixture.create(LocalTime.of(11, 30)));

        // theme1을 사용한 예약 9개
        reservationRepository.add(
                ReservationFixture.create("r1", LocalDate.now().plusDays(8), savedTime1, savedTheme1));
        reservationRepository.add(
                ReservationFixture.create("r2", LocalDate.now().plusDays(7), savedTime1, savedTheme1));
        reservationRepository.add(
                ReservationFixture.create("r3", LocalDate.now().plusDays(6), savedTime1, savedTheme1));
        reservationRepository.add(
                ReservationFixture.create("r4", LocalDate.now().plusDays(5), savedTime1, savedTheme1));
        reservationRepository.add(
                ReservationFixture.create("r5", LocalDate.now().plusDays(4), savedTime1, savedTheme1));
        reservationRepository.add(
                ReservationFixture.create("r6", LocalDate.now().plusDays(3), savedTime1, savedTheme1));
        reservationRepository.add(
                ReservationFixture.create("r7", LocalDate.now().plusDays(2), savedTime1, savedTheme1));
        reservationRepository.add(
                ReservationFixture.create("r8", LocalDate.now().plusDays(1), savedTime1, savedTheme1));
        reservationRepository.add(
                ReservationFixture.create("r10", LocalDate.now().plusDays(1), savedTime1, savedTheme1));

        // theme2를 사용한 예약 8개
        reservationRepository.add(
                ReservationFixture.create("r11", LocalDate.now().plusDays(8), savedTime2, savedTheme2));
        reservationRepository.add(
                ReservationFixture.create("r12", LocalDate.now().plusDays(7), savedTime2, savedTheme2));
        reservationRepository.add(
                ReservationFixture.create("r13", LocalDate.now().plusDays(6), savedTime2, savedTheme2));
        reservationRepository.add(
                ReservationFixture.create("r14", LocalDate.now().plusDays(5), savedTime2, savedTheme2));
        reservationRepository.add(
                ReservationFixture.create("r15", LocalDate.now().plusDays(4), savedTime2, savedTheme2));
        reservationRepository.add(
                ReservationFixture.create("r16", LocalDate.now().plusDays(3), savedTime2, savedTheme2));
        reservationRepository.add(
                ReservationFixture.create("r17", LocalDate.now().plusDays(2), savedTime2, savedTheme2));
        reservationRepository.add(
                ReservationFixture.create("r18", LocalDate.now().plusDays(1), savedTime2, savedTheme2));

        // theme3: 예약 8개
        reservationRepository.add(
                ReservationFixture.create("r20", LocalDate.now().plusDays(8), savedTime1, savedTheme3));
        reservationRepository.add(
                ReservationFixture.create("r21", LocalDate.now().plusDays(7), savedTime1, savedTheme3));
        reservationRepository.add(
                ReservationFixture.create("r22", LocalDate.now().plusDays(6), savedTime1, savedTheme3));
        reservationRepository.add(
                ReservationFixture.create("r23", LocalDate.now().plusDays(5), savedTime1, savedTheme3));
        reservationRepository.add(
                ReservationFixture.create("r24", LocalDate.now().plusDays(4), savedTime1, savedTheme3));
        reservationRepository.add(
                ReservationFixture.create("r25", LocalDate.now().plusDays(3), savedTime1, savedTheme3));
        reservationRepository.add(
                ReservationFixture.create("r26", LocalDate.now().plusDays(2), savedTime1, savedTheme3));
        reservationRepository.add(
                ReservationFixture.create("r27", LocalDate.now().plusDays(1), savedTime1, savedTheme3));

        // theme4: 예약 7개
        reservationRepository.add(
                ReservationFixture.create("r28", LocalDate.now().plusDays(8), savedTime2, savedTheme4));
        reservationRepository.add(
                ReservationFixture.create("r29", LocalDate.now().plusDays(7), savedTime2, savedTheme4));
        reservationRepository.add(
                ReservationFixture.create("r30", LocalDate.now().plusDays(6), savedTime2, savedTheme4));
        reservationRepository.add(
                ReservationFixture.create("r31", LocalDate.now().plusDays(5), savedTime2, savedTheme4));
        reservationRepository.add(
                ReservationFixture.create("r32", LocalDate.now().plusDays(4), savedTime2, savedTheme4));
        reservationRepository.add(
                ReservationFixture.create("r33", LocalDate.now().plusDays(3), savedTime2, savedTheme4));
        reservationRepository.add(
                ReservationFixture.create("r34", LocalDate.now().plusDays(2), savedTime2, savedTheme4));

        // theme5: 예약 6개
        reservationRepository.add(
                ReservationFixture.create("r35", LocalDate.now().plusDays(8), savedTime1, savedTheme5));
        reservationRepository.add(
                ReservationFixture.create("r36", LocalDate.now().plusDays(7), savedTime1, savedTheme5));
        reservationRepository.add(
                ReservationFixture.create("r37", LocalDate.now().plusDays(6), savedTime1, savedTheme5));
        reservationRepository.add(
                ReservationFixture.create("r38", LocalDate.now().plusDays(5), savedTime1, savedTheme5));
        reservationRepository.add(
                ReservationFixture.create("r39", LocalDate.now().plusDays(4), savedTime1, savedTheme5));
        reservationRepository.add(
                ReservationFixture.create("r40", LocalDate.now().plusDays(3), savedTime1, savedTheme5));

        // theme6: 예약 5개
        reservationRepository.add(
                ReservationFixture.create("r41", LocalDate.now().plusDays(8), savedTime2, savedTheme6));
        reservationRepository.add(
                ReservationFixture.create("r42", LocalDate.now().plusDays(7), savedTime2, savedTheme6));
        reservationRepository.add(
                ReservationFixture.create("r43", LocalDate.now().plusDays(6), savedTime2, savedTheme6));
        reservationRepository.add(
                ReservationFixture.create("r44", LocalDate.now().plusDays(5), savedTime2, savedTheme6));
        reservationRepository.add(
                ReservationFixture.create("r45", LocalDate.now().plusDays(4), savedTime2, savedTheme6));

        // theme7: 예약 4개
        reservationRepository.add(
                ReservationFixture.create("r46", LocalDate.now().plusDays(8), savedTime1, savedTheme7));
        reservationRepository.add(
                ReservationFixture.create("r47", LocalDate.now().plusDays(7), savedTime1, savedTheme7));
        reservationRepository.add(
                ReservationFixture.create("r48", LocalDate.now().plusDays(6), savedTime1, savedTheme7));
        reservationRepository.add(
                ReservationFixture.create("r49", LocalDate.now().plusDays(5), savedTime1, savedTheme7));

        // theme8: 예약 3개
        reservationRepository.add(
                ReservationFixture.create("r50", LocalDate.now().plusDays(8), savedTime2, savedTheme8));
        reservationRepository.add(
                ReservationFixture.create("r51", LocalDate.now().plusDays(7), savedTime2, savedTheme8));
        reservationRepository.add(
                ReservationFixture.create("r52", LocalDate.now().plusDays(6), savedTime2, savedTheme8));

        // theme9: 예약 2개
        reservationRepository.add(
                ReservationFixture.create("r53", LocalDate.now().plusDays(8), savedTime1, savedTheme9));
        reservationRepository.add(
                ReservationFixture.create("r54", LocalDate.now().plusDays(7), savedTime1, savedTheme9));

        // theme10: 예약 1개
        reservationRepository.add(
                ReservationFixture.create("r55", LocalDate.now().plusDays(8), savedTime2, savedTheme10));
    }

    @Nested
    @DisplayName("인기 있는 테마를 갯수, 정렬 방식에 따라 처리할 수 있다.")
    class findThemesOrderByReservationTime {

        @DisplayName("dto 기본 값을 사용하여 조회한다.")
        @Test
        void findThemesOrderByReservationTime_expected() {

            // given
            LocalDate now = LocalDate.now();
            LocalDate from = now.plusDays(1);
            LocalDate to = now.plusDays(8);

            // when
            List<Theme> actual = repository.findThemesOrderByReservationCount(from, to, new PopularThemeRequestDto());

            // then
            SoftAssertions.assertSoftly(s -> {
                        s.assertThat(actual).isNotNull();
                        s.assertThat(actual).hasSize(10);
                    }
            );
        }
    }
}
