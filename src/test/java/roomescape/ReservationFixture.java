package roomescape;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.domain.member.domain.ReservationMember;
import roomescape.domain.reservation.domain.ReservationTime;
import roomescape.domain.theme.domain.Theme;

import java.time.LocalTime;

public class ReservationFixture {

    public static final ReservationTime RESERVATION_TIME_NOW = new ReservationTime(1L, LocalTime.now());
    public static final Theme THEME = new Theme(1L, "테마1", "설명", "썸네일");
    public static final ReservationMember RESERVATION_MEMBER = new ReservationMember(1L, "테니");
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    public static final String TOKEN = Jwts.builder()
            .subject("1")
            .claim("name", "테니")
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .compact();
}
