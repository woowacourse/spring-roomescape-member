package roomescape.reservation.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.error.ConflictException;
import roomescape.member.domain.Member;
import roomescape.member.domain.enums.Role;
import roomescape.reservation.controller.dto.ThemeRankingResponse;
import roomescape.reservation.controller.dto.ThemeRequest;
import roomescape.reservation.controller.dto.ThemeResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.reservation.repository.ThemeRepository;
import roomescape.reservation.repository.fake.MemberFakeRepository;
import roomescape.reservation.repository.fake.ReservationFakeRepository;
import roomescape.reservation.repository.fake.ReservationTimeFakeRepository;
import roomescape.reservation.repository.fake.ThemeFakeRepository;

class ThemeServiceTest {

    private ThemeService themeService;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setup() {
        ReservationRepository reservationRepository = new ReservationFakeRepository();
        ReservationTimeRepository reservationTimeRepository = new ReservationTimeFakeRepository();
        themeRepository = new ThemeFakeRepository(reservationRepository);
        MemberFakeRepository memberRepository = new MemberFakeRepository();

        reservationTimeRepository.saveAndReturnId(new ReservationTime(null, LocalTime.of(3, 12)));
        reservationTimeRepository.saveAndReturnId(new ReservationTime(null, LocalTime.of(11, 33)));
        reservationTimeRepository.saveAndReturnId(new ReservationTime(null, LocalTime.of(16, 54)));
        reservationTimeRepository.saveAndReturnId(new ReservationTime(null, LocalTime.of(23, 53)));

        themeRepository.saveAndReturnId(new Theme(null, "레벨1 탈출", "우테코 레벨1를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        themeRepository.saveAndReturnId(new Theme(null, "레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        themeRepository.saveAndReturnId(new Theme(null, "레벨3 탈출", "우테코 레벨3를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        memberRepository.save(new Member(null, "루키", "rookie123@woowa.com", "rookierookie123", Role.USER));
        memberRepository.save(new Member(null, "하루", "haru123@woowa.com", "haruharu123", Role.USER));
        memberRepository.save(new Member(null, "베루스", "verus@woowa.com", "verusverus123", Role.ADMIN));

        reservationRepository.saveAndReturnId(
                new Reservation(null, LocalDate.now().minusDays(3), reservationTimeRepository.findById(1L).get(),
                        themeRepository.findById(1L).get(), memberRepository.findById(1L).get()));
        reservationRepository.saveAndReturnId(
                new Reservation(null, LocalDate.now().minusDays(1), reservationTimeRepository.findById(2L).get(),
                        themeRepository.findById(2L).get(), memberRepository.findById(2L).get()));
        reservationRepository.saveAndReturnId(
                new Reservation(null, LocalDate.now().plusDays(3), reservationTimeRepository.findById(3L).get(),
                        themeRepository.findById(3L).get(), memberRepository.findById(3L).get()));

        themeService = new ThemeService(themeRepository, reservationRepository);
    }

    @DisplayName("테마 정보를 추가한다")
    @Test
    void add_theme() {
        // given
        ThemeRequest request = new ThemeRequest("레벨4 탈출", "우테코 레벨4를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        // when
        ThemeResponse response = themeService.add(request);

        // then
        Theme savedTheme = themeRepository.findById(4L).get();
        assertAll(
                () -> assertThat(response.id()).isEqualTo(savedTheme.getId()),
                () -> assertThat(response.name()).isEqualTo(savedTheme.getName()),
                () -> assertThat(response.description()).isEqualTo(savedTheme.getDescription()),
                () -> assertThat(response.thumbnail()).isEqualTo(savedTheme.getThumbnail())
        );
    }

    @DisplayName("테마 정보를 조회한다")
    @Test
    void get_themes() {
        // when
        List<ThemeResponse> themes = themeService.getThemes();

        // then
        assertAll(
                () -> assertThat(themes).hasSize(3),
                () -> assertThat(themes).extracting(ThemeResponse::name)
                        .containsExactlyInAnyOrder("레벨1 탈출", "레벨2 탈출", "레벨3 탈출"),
                () -> assertThat(themes).extracting(ThemeResponse::id)
                        .containsExactlyInAnyOrder(1L, 2L, 3L)
        );
    }

    @DisplayName("테마 정보를 삭제한다")
    @Test
    void delete_theme() {
        // given
        Long removeId = 4L;

        // when
        themeRepository.deleteById(removeId);

        // then
        assertAll(
                () -> assertThat(themeRepository.findAll()).hasSize(3),
                () -> assertThat(themeRepository.findById(removeId).isEmpty()).isTrue()
        );

    }

    @DisplayName("특정 테마에 대한 예약이 존재하는데, 그 테마를 삭제하면 예외가 발생한다")
    @Test
    void delete_exception() {
        // given
        Long deleteId = 1L;

        // when & then
        assertThatThrownBy(() -> themeService.remove(deleteId))
                .isInstanceOf(ConflictException.class)
                .hasMessage("해당 테마와 연관된 예약이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("인기 테마 정보를 조회한다")
    @Test
    void get_theme_rankings_test() {
        // when
        List<ThemeRankingResponse> actual = themeService.getThemeRankings();

        // then
        List<ThemeRankingResponse> expected = List.of(
                new ThemeRankingResponse("레벨1 탈출", "우테코 레벨1를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                new ThemeRankingResponse("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
        );
        assertThat(actual).containsExactlyElementsOf(expected);
    }

}
