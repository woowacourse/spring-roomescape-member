package roomescape.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.repository.JdbcAuthRepository;
import roomescape.entity.Member;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.exception.impl.ConnectedReservationExistException;
import roomescape.exception.impl.HasDuplicatedTimeException;
import roomescape.exception.impl.ReservationTimeIntervalException;
import roomescape.repository.JdbcReservationRepository;
import roomescape.repository.JdbcReservationTimeRepository;
import roomescape.repository.JdbcThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeServiceTest {

    @Autowired
    private JdbcReservationTimeRepository reservationTimeRepository;

    @Autowired
    private JdbcAuthRepository authRepository;

    @Autowired
    private JdbcReservationRepository reservationRepository;

    @Autowired
    private JdbcThemeRepository themeRepository;

    @Autowired
    private ReservationTimeService reservationTimeService;


    @BeforeEach
    void setUP() {
        //사용자 생성
        Member member = authRepository.save(Member.beforeMemberSave(
                "레몬",
                "ywcsuwon@naver.com",
                "123")
        );
        //테마 생성
        Theme theme = themeRepository.save(Theme.afterSave(
                1,
                "홍길동",
                "홍길동의 모험",
                "썸네일"
        ));
        //예약 시간 생성
        ReservationTime reservationTime = reservationTimeRepository.save(ReservationTime.afterSave(
                1,
                LocalTime.of(19, 0)
        ));

        Reservation reservation = reservationRepository.save(Reservation.afterSave(
                1,
                LocalDate.now(),
                member,
                reservationTime,
                theme)
        );
    }

    @Test
    @DisplayName("중복된 시간을 저장할 시 예외가 발생한다.")
    void whenCreateDuplicatedTimeThrowExceptionTest() {
        // given
        // when
        // then
        Assertions.assertThatThrownBy(() -> reservationTimeService.createReservationTime(LocalTime.of(19, 0)))
                .isInstanceOf(HasDuplicatedTimeException.class);
    }

    @Test
    @DisplayName("기존 시간과 30분 이내의 신규 시간 추가시 예외가 발생한다.")
    void whenCreateUnderIntervalTimeThrowExceptionTest() {
        // given
        // when
        // then
        Assertions.assertThatThrownBy(() -> reservationTimeService.createReservationTime(LocalTime.of(19, 20)))
                .isInstanceOf(ReservationTimeIntervalException.class);
    }

    @Test
    @DisplayName("예약된 시간이 존재하는 시간을 삭제할 시 예외가 발생한다.")
    void whenDeleteReservedTimeThrowExceptionTest() {
        // given
        // when
        // then
        Assertions.assertThatThrownBy(() -> reservationTimeService.delete(1))
                .isInstanceOf(ConnectedReservationExistException.class);
    }
}
