package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.business.domain.Member;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Reservation;
import roomescape.business.domain.Theme;
import roomescape.exception.DuplicateException;
import roomescape.exception.InvalidDateAndTimeException;
import roomescape.exception.NotFoundException;
import roomescape.persistence.dao.JdbcMemberDao;
import roomescape.persistence.dao.JdbcPlayTimeDao;
import roomescape.persistence.dao.JdbcReservationDao;
import roomescape.persistence.dao.JdbcThemeDao;
import roomescape.persistence.dao.MemberDao;
import roomescape.persistence.dao.PlayTimeDao;
import roomescape.persistence.dao.ReservationDao;
import roomescape.persistence.dao.ThemeDao;
import roomescape.presentation.dto.ReservationResponse;

@JdbcTest
public class ReservationServiceTest {

    private final ReservationService reservationService;
    private final ReservationDao reservationDao;
    private final MemberDao memberDao;
    private final PlayTimeDao playTimeDao;
    private final ThemeDao themeDao;

    @Autowired
    public ReservationServiceTest(final JdbcTemplate jdbcTemplate) {
        this.reservationDao = new JdbcReservationDao(jdbcTemplate);
        this.memberDao = new JdbcMemberDao(jdbcTemplate);
        this.playTimeDao = new JdbcPlayTimeDao(jdbcTemplate);
        this.themeDao = new JdbcThemeDao(jdbcTemplate);
        this.reservationService = new ReservationService(reservationDao, memberDao, playTimeDao, themeDao);
    }

    @Test
    @DisplayName("방탈출 예약 요청 객체로 방탈출 예약을 저장한다")
    void insert() {
        // given
        final Member member = new Member("kim", "USER", "email@test.com", "pass");
        final Member insertMember = memberDao.insert(member);
        final PlayTime playTime = new PlayTime(LocalTime.of(14, 0));
        final PlayTime insertPlayTime = playTimeDao.insert(playTime);
        final Theme theme = new Theme("이름", "설명", "썸네일");
        final Theme insertTheme = themeDao.insert(theme);

        // when
        final ReservationResponse reservationResponse = reservationService.insert(LocalDate.MAX,
                insertMember.getId(), insertPlayTime.getId(), insertTheme.getId());

        // then
        assertAll(
                () -> assertThat(reservationResponse.date()).isEqualTo(LocalDate.MAX),
                // member
                () -> assertThat(reservationResponse.member()
                        .id()).isEqualTo(insertMember.getId()),
                () -> assertThat(reservationResponse.member()
                        .name()).isEqualTo("kim"),
                () -> assertThat(reservationResponse.member()
                        .email()).isEqualTo("email@test.com"),
                // reservation_time
                () -> assertThat(reservationResponse.time()
                        .id()).isEqualTo(insertPlayTime.getId()),
                () -> assertThat(reservationResponse.time()
                        .startAt()).isEqualTo("14:00"),
                // theme
                () -> assertThat(reservationResponse.theme()
                        .id()).isEqualTo(insertTheme.getId()),
                () -> assertThat(reservationResponse.theme()
                        .name()).isEqualTo("이름"),
                () -> assertThat(reservationResponse.theme()
                        .description()).isEqualTo("설명")
        );
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 예약하면 예외가 발생한다")
    void insertWhenNotExistMember() {
        // given
        final Member member = new Member("kim", "USER", "email@test.com", "pass");
        final Member insertMember = memberDao.insert(member);
        final PlayTime playTime = new PlayTime(LocalTime.of(14, 0));
        final PlayTime insertPlayTime = playTimeDao.insert(playTime);
        final Theme theme = new Theme("이름", "설명", "썸네일");
        final Theme insertTheme = themeDao.insert(theme);
        final Long notExistMemberId = 999L;

        // when & then
        assertThatThrownBy(() -> reservationService.insert(LocalDate.MAX, notExistMemberId, insertPlayTime.getId(),
                insertTheme.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 방탈출 예약 시간으로 예약하면 예외가 발생한다")
    void insertWhenNotExistReservationTime() {
        // given
        final Member member = new Member("kim", "USER", "email@test.com", "pass");
        final Member insertMember = memberDao.insert(member);
        final PlayTime playTime = new PlayTime(LocalTime.of(14, 0));
        final PlayTime insertPlayTime = playTimeDao.insert(playTime);
        final Theme theme = new Theme("이름", "설명", "썸네일");
        final Theme insertTheme = themeDao.insert(theme);
        final Long notExistTimeId = 999L;

        // when & then
        assertThatThrownBy(() -> reservationService.insert(LocalDate.MAX, insertMember.getId(), notExistTimeId,
                insertTheme.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 테마로 예약하면 예외가 발생한다")
    void insertWhenNotExistTheme() {
        // given
        final Member member = new Member("kim", "USER", "email@test.com", "pass");
        final Member insertMember = memberDao.insert(member);
        final PlayTime playTime = new PlayTime(LocalTime.of(14, 0));
        final PlayTime insertPlayTime = playTimeDao.insert(playTime);
        final Theme theme = new Theme("이름", "설명", "썸네일");
        final Theme insertTheme = themeDao.insert(theme);
        final Long notExistThemeId = 999L;

        // when & then
        assertThatThrownBy(() -> reservationService.insert(LocalDate.MAX, insertMember.getId()
                , insertPlayTime.getId(), notExistThemeId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("예약하려는 방탈출 예약과 동일한 날짜, 시간, 테마가 이미 존재한다면 예외가 발생한다")
    void insertWhenDuplicateDateAndTimeAndTheme() {
        // given
        final Member member = new Member("kim", "USER", "email@test.com", "pass");
        final Member insertMember = memberDao.insert(member);
        final PlayTime playTime = new PlayTime(LocalTime.of(14, 0));
        final PlayTime insertPlayTime = playTimeDao.insert(playTime);
        final Theme theme = new Theme("이름", "설명", "썸네일");
        final Theme insertTheme = themeDao.insert(theme);
        reservationService.insert(LocalDate.MAX, insertMember.getId(), insertPlayTime.getId(),
                insertTheme.getId());

        // when & then
        assertThatThrownBy(() -> reservationService.insert(LocalDate.MAX, insertMember.getId()
                , insertPlayTime.getId(), insertTheme.getId()))
                .isInstanceOf(DuplicateException.class);
    }

    @Test
    @DisplayName("예약 시간이 현재를 기준으로 과거라면 예외가 발생한다")
    void insertWhenDateAndTimeIsPast() {
        // given
        final Member member = new Member("kim", "USER", "email@test.com", "pass");
        final Member insertMember = memberDao.insert(member);
        final PlayTime playTime = new PlayTime(LocalTime.of(14, 0));
        final PlayTime insertPlayTime = playTimeDao.insert(playTime);
        final Theme theme = new Theme("이름", "설명", "썸네일");
        final Theme insertTheme = themeDao.insert(theme);
        final LocalDate pastDate = LocalDate.MIN;

        // when & then
        assertThatThrownBy(() -> reservationService.insert(pastDate, insertMember.getId(), insertPlayTime.getId(),
                insertTheme.getId()))
                .isInstanceOf(InvalidDateAndTimeException.class);
    }

    @Test
    @DisplayName("모든 방탈출 예약을 조회한다")
    void findAll() {
        // given
        final Member member = new Member("kim", "USER", "email@test.com", "pass");
        final Member insertMember = memberDao.insert(member);
        final PlayTime playTime = new PlayTime(LocalTime.of(14, 0));
        final PlayTime insertPlayTime = playTimeDao.insert(playTime);
        final Theme theme = new Theme("이름", "설명", "썸네일");
        final Theme insertTheme = themeDao.insert(theme);

        reservationService.insert(LocalDate.MAX, insertMember.getId(), insertPlayTime.getId(),
                insertTheme.getId());
        reservationService.insert(LocalDate.MAX.minusDays(1), insertMember.getId(), insertPlayTime.getId(),
                insertTheme.getId());

        // when
        final List<ReservationResponse> reservationResponses = reservationService.findAll();

        // then
        assertThat(reservationResponses).hasSize(2);
    }

    @Test
    @DisplayName("모든 방탈출 예약을 필터링하여 조회한다")
    void findAllFilter() {
        // given
        final Member member = new Member("kim", "USER", "email@test.com", "pass");
        final Member insertMember = memberDao.insert(member);
        final PlayTime playTime = new PlayTime(LocalTime.of(14, 0));
        final PlayTime insertPlayTime = playTimeDao.insert(playTime);
        final Theme theme = new Theme("이름", "설명", "썸네일");
        final Theme insertTheme = themeDao.insert(theme);

        reservationService.insert(LocalDate.MAX, insertMember.getId(), insertPlayTime.getId(),
                insertTheme.getId());
        reservationService.insert(LocalDate.MAX.minusDays(1), insertMember.getId(), insertPlayTime.getId(),
                insertTheme.getId());

        // when
        final List<ReservationResponse> reservationResponses = reservationService.findAllFilter(null, null,
                LocalDate.MAX, LocalDate.MAX);

        // then
        assertThat(reservationResponses).hasSize(1);
    }

    @Test
    @DisplayName("id를 통해 방탈출 예약을 삭제한다")
    void deleteById() {
        // given
        final Member member = new Member("kim", "USER", "email@test.com", "pass");
        final Member insertMember = memberDao.insert(member);
        final PlayTime playTime = new PlayTime(LocalTime.of(14, 0));
        final PlayTime insertPlayTime = playTimeDao.insert(playTime);
        final Theme theme = new Theme("이름", "설명", "썸네일");
        final Theme insertTheme = themeDao.insert(theme);
        final Reservation reservation = new Reservation(LocalDate.MAX, insertMember, insertPlayTime, insertTheme);
        final Reservation insertReservation = reservationDao.insert(reservation);

        // when
        reservationService.deleteById(insertReservation.getId());

        // then
        final Optional<Reservation> findReservation = reservationDao.findById(insertReservation.getId());
        assertThat(findReservation).isEmpty();
    }

    @Test
    @DisplayName("id를 통해 예약을 삭제할 때 대상이 없다면 예외가 발생한다")
    void deleteByIdWhenNotExistReservation() {
        // given
        final Long notExistId = 999L;

        // when & then
        assertThatThrownBy(() -> reservationService.deleteById(notExistId))
                .isInstanceOf(NotFoundException.class);
    }
}
