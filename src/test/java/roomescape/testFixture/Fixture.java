package roomescape.testFixture;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public class Fixture {
    public static final Theme THEME_1 = Theme.of(1L, "테마1", "테마 1입니다.", "썸네일1");
    public static final Theme THEME_2 = Theme.of(2L, "테마2", "테마 2입니다.", "썸네일2");
    public static final Theme THEME_3 = Theme.of(3L, "테마3", "테마 3입니다.", "썸네일3");
    public static final ReservationTime RESERVATION_TIME_1 = ReservationTime.of(1L, LocalTime.of(10, 0));
    public static final ReservationTime RESERVATION_TIME_2 = ReservationTime.of(2L, LocalTime.of(11, 0));
    public static final ReservationTime RESERVATION_TIME_3 = ReservationTime.of(3L, LocalTime.of(12, 0));
    public static final Reservation RESERVATION_1 = Reservation.of(
            1L, 1L, THEME_1, LocalDate.now().plusDays(1), RESERVATION_TIME_1);
    public static final Reservation RESERVATION_2 = Reservation.of(
            2L, 1L, THEME_2, LocalDate.now().plusDays(1), RESERVATION_TIME_2);
    public static final Reservation RESERVATION_3 = Reservation.of(
            3L, 1L, THEME_3, LocalDate.now().plusDays(1), RESERVATION_TIME_3);
    public static final Member MEMBER_1 = new Member(1L, "test@email.com", "password", "멍구", Role.ADMIN);
    public static final Member MEMBER_2 = new Member(2L, "test2@email.com", "password2", "멍구2", Role.ADMIN);

    public static final String createTokenByMemberId(JwtTokenProvider jwtTokenProvider, Long memberId) {
        String payload = String.valueOf(memberId);
        return jwtTokenProvider.createToken(payload, Role.USER);
    }

    public static final Map<String, Object> RESERVATION_BODY = createReservationBody();
    public static final Map<String, Object> ADMIN_RESERVATION_BODY = createReservationBody();

    public static Reservation createReservation(LocalDate date, Long memberId, Long timeId, Long themeId) {
        Theme theme = Theme.of(themeId, "테마", "테마 설명입니다.", "썸네일");
        ReservationTime reservationTime = ReservationTime.of(timeId, LocalTime.of(10, 0));
        return Reservation.of(1L, memberId, theme, date, reservationTime);
    }

    public static Reservation createReservationById(Long id) {
        return Reservation.of(
                id, 1L, THEME_1, LocalDate.now().plusDays(1), RESERVATION_TIME_1);
    }

    public static ReservationTime createTimeById(Long timeId) {
        return ReservationTime.of(timeId, LocalTime.of(10, 0));
    }

    public static Theme createThemeById(Long themeId) {
        return Theme.of(themeId, "테마", "테마 설명입니다.", "썸네일");
    }

    public static Map<String, Object> createReservationBody() {
        Map<String, Object> params = new HashMap<>();

        String date = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        params.put("date", date);
        params.put("timeId", 1);
        params.put("themeId", 1);

        return params;
    }

    public static Member createMemberByIdAndName(Long id, String name) {
        return new Member(id, "email" + id, "password", name, Role.ADMIN);
    }
}
