package roomescape.config;

import static roomescape.domain.Role.ADMIN;
import static roomescape.domain.Role.MEMBER;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Component
public class DataLoader implements ApplicationRunner {
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public DataLoader(
            final MemberRepository memberRepository,
            final ReservationRepository reservationRepository,
            final ReservationTimeRepository reservationTimeRepository,
            final ThemeRepository themeRepository
    ) {
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        final Member jojo = memberRepository.save(new Member("조조", "imjojo@gmail.com", "qwer", MEMBER));
        final Member mia = memberRepository.save(new Member("미아", "mia@gmail.com", "1234", MEMBER));
        final Member planet = memberRepository.save(new Member("행성이", "planet@gmail.com", "1111", ADMIN));
        final Member admin = memberRepository.save(new Member("어드민", "admin@gmail.com", "12345", ADMIN));

        final ReservationTime hour13 = reservationTimeRepository.save(new ReservationTime("13:00"));
        final ReservationTime hour14 = reservationTimeRepository.save(new ReservationTime("14:00"));
        final ReservationTime hour15 = reservationTimeRepository.save(new ReservationTime("15:00"));

        final Theme theme1 = themeRepository.save(new Theme("테마1", "테마1 설명", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        final Theme theme2 = themeRepository.save(new Theme("테마2", "테마2 설명", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        final Theme theme3 = themeRepository.save(new Theme("테마3", "테마3 설명", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        final Theme theme4 = themeRepository.save(new Theme("테마4", "테마4 설명", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        final Theme theme5 = themeRepository.save(new Theme("테마5", "테마5 설명", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        final Theme theme6 = themeRepository.save(new Theme("테마6", "테마6 설명", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        final Theme theme7 = themeRepository.save(new Theme("테마7", "테마7 설명", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        final Theme theme8 = themeRepository.save(new Theme("테마8", "테마8 설명", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        final Theme theme9 = themeRepository.save(new Theme("테마9", "테마9 설명", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        final Theme theme10 = themeRepository.save(new Theme("테마10", "테마10 설명", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        final Theme theme11 = themeRepository.save(new Theme("테마11", "테마11 설명", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        reservationRepository.save(new Reservation(jojo, "2024-05-01", hour13, theme1));
        reservationRepository.save(new Reservation(jojo, "2024-05-02", hour14, theme1));
        reservationRepository.save(new Reservation(mia, "2024-05-03", hour15, theme1));
        reservationRepository.save(new Reservation(mia, "2024-05-04", hour13, theme2));
        reservationRepository.save(new Reservation(jojo, "2024-05-05", hour14, theme2));
        reservationRepository.save(new Reservation(jojo, "2024-05-06", hour13, theme3));
        reservationRepository.save(new Reservation(jojo, "2024-05-07", hour13, theme4));
        reservationRepository.save(new Reservation(jojo, "2024-05-08", hour13, theme5));
        reservationRepository.save(new Reservation(mia, "2024-05-09", hour13, theme6));
    }
}
