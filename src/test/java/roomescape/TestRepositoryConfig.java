package roomescape;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.persistence.fakerepository.FakeMemberRepository;
import roomescape.persistence.fakerepository.FakeReservationRepository;
import roomescape.persistence.fakerepository.FakeReservationThemeRepository;
import roomescape.persistence.fakerepository.FakeReservationTimeRepository;

/**
 * 테스트 환경에서 사용할 레포지토리 설정 클래스입니다.
 */
@TestConfiguration
@Profile("test")
public class TestRepositoryConfig {

    @Bean
    public ReservationRepository reservationRepository() {
        return new FakeReservationRepository();
    }

    @Bean
    public ReservationTimeRepository reservationTimeRepository() {
        return new FakeReservationTimeRepository();
    }

    @Bean
    public ReservationThemeRepository reservationThemeRepository() {
        return new FakeReservationThemeRepository();
    }

    @Bean
    public FakeMemberRepository memberRepository() {
        return new FakeMemberRepository();
    }
}
