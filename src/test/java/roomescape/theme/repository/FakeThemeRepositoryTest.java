package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.fixture.Fixture;
import roomescape.reservation.fake.FakeReservationRepository;
import roomescape.reservation.model.Reservation;
import roomescape.theme.fake.FakeThemeRepository;
import roomescape.theme.model.Theme;

class FakeThemeRepositoryTest {

    private static FakeThemeRepository fakeThemeRepository;
    private static FakeReservationRepository fakeReservationRepository;

    @BeforeAll
    static void setUpAll() {
        fakeReservationRepository = new FakeReservationRepository();
        fakeThemeRepository = new FakeThemeRepository(fakeReservationRepository);
    }

    @BeforeEach
    void setUp() {
        fakeReservationRepository.clear();
        fakeThemeRepository.clear();
    }

    @Test
    @DisplayName("Theme 저장한 후 저장한 테마 값을 반환한다.")
    void save() {
        Theme theme = new Theme(null, "마크", "노력함", "https://asd.cmom");

        assertThat(fakeThemeRepository.save(theme))
                .isEqualTo(new Theme(1L, "마크", "노력함", "https://asd.cmom"));
    }

    @Test
    @DisplayName("Theme 테이블의 있는 모든 데이터를 조회한다.")
    void findAll() {
        fakeThemeRepository.save(Fixture.THEME_1);
        fakeThemeRepository.save(Fixture.THEME_2);
        fakeThemeRepository.save(Fixture.THEME_3);

        assertThat(fakeThemeRepository.findAll())
                .containsExactly(
                        Fixture.THEME_1,
                        Fixture.THEME_2,
                        Fixture.THEME_3);
    }

    @Test
    @DisplayName("Theme 테이블의 주어진 id와 동일한 데이터를 조회한다.")
    void findById() {
        fakeThemeRepository.save(Fixture.THEME_1);

        assertThat(fakeThemeRepository.findById(1L))
                .isEqualTo(Optional.of(Fixture.THEME_1));
    }

    @Test
    @DisplayName("Theme 테이블에 주어진 id와 없는 경우 빈 옵셔널을 반환한다.")
    void findById_Return_EmptyOptional() {
        assertThat(fakeThemeRepository.findById(99999L))
                .isNotPresent();
    }

    @Test
    @DisplayName("많이 예약된 테마 순서대로 가져온다.")
    void findOrderByReservation1() {
        fakeReservationRepository.save(Fixture.RESERVATION_1); // THEME_1
        fakeReservationRepository.save(Fixture.RESERVATION_2); // THEME_2
        fakeReservationRepository.save(Fixture.RESERVATION_3); // THEME_2

        assertThat(fakeThemeRepository.findOrderByReservation())
                .containsExactlyInAnyOrderElementsOf(List.of(
                        Fixture.THEME_2,
                        Fixture.THEME_1));
    }

    @Test
    @DisplayName("예약되지 않은 테마는 가져오지 않는다.")
    void findOrderByReservation2() {
        fakeThemeRepository.save(Fixture.THEME_1);
        fakeThemeRepository.save(Fixture.THEME_2);
        fakeThemeRepository.save(Fixture.THEME_3);
        fakeReservationRepository.save(Fixture.RESERVATION_1); // THEME_1

        List<Theme> orderByReservation = fakeThemeRepository.findOrderByReservation();

        assertThat(orderByReservation)
                .containsExactlyInAnyOrderElementsOf(List.of(Fixture.THEME_1));
    }

    @Test
    @DisplayName("예약이 많은 순서대로 테마 10개만 가져온다.")
    void findOrderByReservation3() {
        // 1 ~ 11번 테마를 사용하는 예약 생성
        generateReservationBy11Theme();

        assertThat(fakeThemeRepository.findOrderByReservation())
                .hasSize(10);
    }

    private void generateReservationBy11Theme() {
        IntStream.range(1, 12)
                .forEach(i -> fakeReservationRepository.save(
                                new Reservation(Integer.toUnsignedLong(i), "마크",
                                        LocalDate.of(3000 + i, 1, 1),
                                        Fixture.RESERVATION_TIME_1,
                                        new Theme(
                                                Integer.toUnsignedLong(i),
                                                "테마" + i,
                                                "설명",
                                                "URL"
                                        ))));
    }

    @Test
    @DisplayName("주어진 id와 동일한 데이터를 삭제한다.")
    void deleteById() {
        fakeThemeRepository.save(Fixture.THEME_1);

        fakeThemeRepository.deleteById(1L);

        assertThat(fakeThemeRepository.findById(1L))
                .isNotPresent();
    }
}
