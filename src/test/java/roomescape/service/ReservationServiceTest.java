package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.application.service.ReservationService;
import roomescape.domain.exception.ReservationDuplicatedException;
import roomescape.domain.exception.ResourceNotExistException;
import roomescape.domain.model.Member;
import roomescape.domain.model.ReservationTime;
import roomescape.domain.model.Theme;
import roomescape.domain.repository.MemberRepository;
import roomescape.domain.repository.ReservationRepository;
import roomescape.fake.MemberDaoFake;
import roomescape.fake.ReservationDaoFake;
import roomescape.fake.ReservationTimeDaoFake;
import roomescape.fake.ThemeDaoFake;
import roomescape.infrastructure.dao.ReservationDao;
import roomescape.infrastructure.dao.ReservationTimeDao;
import roomescape.infrastructure.dao.ThemeDao;
import roomescape.infrastructure.repository.MemberRepositoryImpl;
import roomescape.infrastructure.repository.ReservationRepositoryImpl;
import roomescape.infrastructure.repository.ReservationTimeRepositoryImpl;
import roomescape.infrastructure.repository.ThemeRepositoryImpl;
import roomescape.presentation.dto.request.ReservationRequest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static roomescape.fixture.TestFixture.*;

public class ReservationServiceTest {

    ReservationDao reservationDao;
    ReservationTimeDao reservationTimeDao;
    ThemeDao themeDao;
    ReservationRepository reservationRepository;
    MemberRepository memberRepository;
    ReservationService reservationService;

    public ReservationServiceTest() {
        reservationDao = new ReservationDaoFake();
        reservationTimeDao = new ReservationTimeDaoFake();
        themeDao = new ThemeDaoFake();
        reservationRepository = new ReservationRepositoryImpl(reservationDao);
        memberRepository = new MemberRepositoryImpl(new MemberDaoFake());
        reservationService = new ReservationService(reservationRepository, new ReservationTimeRepositoryImpl(reservationTimeDao), new ThemeRepositoryImpl(themeDao), memberRepository);
    }

    @BeforeEach
    void clear() {
        ((ThemeDaoFake) themeDao).clear();
        ((ReservationTimeDaoFake) reservationTimeDao).clear();
        ((ReservationDaoFake) reservationDao).clear();
    }

    @Test
    void 예약을_생성할_수_있다() {
        // given
        ReservationTime time = reservationTimeDao.save(DEFAULT_TIME);
        Theme theme = themeDao.save(DEFAULT_THEME);

        // when
        ReservationRequest reservation = new ReservationRequest(TOMORROW, time.getId(), theme.getId());
        Member member = memberRepository.findById(1L);

        // then
        assertDoesNotThrow(() -> reservationService.save(member, reservation));
    }

    @Test
    void 중복_예약을_시도하는_경우_예외를_발생시킨다() {
        // given
        ReservationTime time = reservationTimeDao.save(DEFAULT_TIME);
        Theme theme = themeDao.save(DEFAULT_THEME);

        // when
        ReservationRequest reservation = new ReservationRequest(TOMORROW, time.getId(), theme.getId());
        Member member = memberRepository.findById(1L);
        reservationService.save(member, reservation);

        // then
        assertThatThrownBy(() -> reservationService.save(member, reservation))
                .isInstanceOf(ReservationDuplicatedException.class);
    }

    @Test
    void 과거_시간대를_예약하려는_경우_예외를_발생시킨다() {
        // given
        ReservationTime time = reservationTimeDao.save(DEFAULT_TIME);
        Theme theme = themeDao.save(DEFAULT_THEME);

        // when
        ReservationRequest reservation = new ReservationRequest(YESTERDAY, time.getId(), theme.getId());
        Member member = memberRepository.findById(1L);

        // then
        assertThatThrownBy(() -> reservationService.save(member, reservation))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 현재보다 과거 시간에는 예약이 불가능합니다.");
    }

    @Test
    void 존재하지_않는_예약을_삭제하려는_예외를_발생시킨다() {
        // when & then
        assertThatThrownBy(() -> reservationService.deleteReservation(-1L))
                .isInstanceOf(ResourceNotExistException.class);
    }
}
