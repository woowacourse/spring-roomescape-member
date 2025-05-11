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
import roomescape.exception.impl.AlreadyReservedException;
import roomescape.exception.impl.ReservationNotFoundException;
import roomescape.repository.JdbcReservationRepository;
import roomescape.repository.JdbcReservationTimeRepository;
import roomescape.repository.JdbcThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationServiceTest {

    @Autowired
    private JdbcReservationRepository reservationRepository;

    @Autowired
    private JdbcAuthRepository authRepository;

    @Autowired
    private JdbcReservationTimeRepository reservationTimeRepository;

    @Autowired
    private JdbcThemeRepository themeRepository;

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
        //예약 생성
        reservationRepository.save(Reservation.afterSave(
                1,
                LocalDate.now(),
                member,
                reservationTime,
                theme
        ));
    }

    @Autowired
    private ReservationService reservationService;

    @Test
    @DisplayName("중복된 날짜,시간,테마에 예약시 예외가 발생한다.")
    void whenCreateDuplicateReservationThrowExceptionTest() {
        // given
        // when
        // then
        Assertions.assertThatThrownBy(() -> reservationService.createMemberReservation(Member.beforeMemberSave("모코", "ywcsuwon@naver.com", "123"), LocalDate.now(), 1, 1))
                .isInstanceOf(AlreadyReservedException.class);

    }

    @Test
    @DisplayName("해당하지 않는 예약을 삭제할 때 예외가 발생한다.")
    void whenDeleteNonExistenceReservationThrowExceptionTest() {
        //given
        //when
        //then
        Assertions.assertThatThrownBy(() -> reservationService.delete(2))
                .isInstanceOf(ReservationNotFoundException.class);
    }
}
