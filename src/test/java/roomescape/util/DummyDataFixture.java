package roomescape.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import roomescape.reservation.model.Reservation;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.theme.model.Theme;

public class DummyDataFixture {
    private List<ReservationTime> preparedReservationTimes = List.of(
            new ReservationTime(1L, LocalTime.of(10, 00)),
            new ReservationTime(2L, LocalTime.of(12, 00)),
            new ReservationTime(3L, LocalTime.of(14, 00)),
            new ReservationTime(4L, LocalTime.of(16, 00)),
            new ReservationTime(5L, LocalTime.of(18, 00)),
            new ReservationTime(6L, LocalTime.of(20, 00)));

    private List<Theme> preparedThemes = List.of(
            new Theme(1L, "공포", "무서워", "https://zerolotteworld.com/storage/WAYH10LvyaCuAb9ndj1apZIpzEAdpjeAhPR7Gb7J.jpg"),
            new Theme(2L, "액션", "신나", "https://sherlock-holmes.co.kr/attach/theme/17000394031.jpg"),
            new Theme(3L, "SF", "신기해", "https://sherlock-holmes.co.kr/attach/theme/16941579841.jpg"),
            new Theme(4L, "로맨스", "달달해", "https://i.postimg.cc/vDFKqct1/theme.jpg"),
            new Theme(5L, "코미디", "웃기다", "https://sherlock-holmes.co.kr/attach/theme/16956118601.jpg"),
            new Theme(6L, "드라마", "반전", "https://sherlock-holmes.co.kr/attach/theme/16941579841.jpg"),
            new Theme(7L, "잠입", "스릴있어",
                    "https://search.pstatic.net/sunny/?src=https%3A%2F%2Ffile.miricanvas.com%2Ftemplate_thumb%2F2022%2F05%2F15%2F13%2F50%2Fk2nje40j0jwztqza%2Fthumb.jpg&type=sc960_832"),
            new Theme(8L, "오락", "재밌어", "http://jamsil.cubeescape.co.kr/theme/basic_room2/img/rain/room15.jpg"),
            new Theme(9L, "판타지", "말이 안돼", "https://i.postimg.cc/8k2PQ4yv/theme.jpg"),
            new Theme(10L, "감성", "감동적", "https://sherlock-holmes.co.kr/attach/theme/16788523411.jpg"));

    private List<Reservation> preparedReservations = List.of(
            Reservation.of(1L, "아서", LocalDate.parse("2024-04-23"),
                    getReservationTimeById(1L), getThemeById(1L)),
            Reservation.of(2L, "몰리", LocalDate.parse("2024-04-24"),
                    getReservationTimeById(2L), getThemeById(1L)),
            Reservation.of(3L, "마크", LocalDate.parse("2024-04-25"),
                    getReservationTimeById(3L), getThemeById(1L)),
            Reservation.of(4L, "비밥", LocalDate.parse("2024-04-26"),
                    getReservationTimeById(4L), getThemeById(1L)),
            Reservation.of(5L, "러너덕", LocalDate.parse("2024-04-27"),
                    getReservationTimeById(5L), getThemeById(2L)),
            Reservation.of(6L, "현구막", LocalDate.parse("2024-04-28"),
                    getReservationTimeById(1L), getThemeById(2L)),
            Reservation.of(7L, "찰리", LocalDate.parse("2024-04-29"),
                    getReservationTimeById(2L), getThemeById(2L)),
            Reservation.of(8L, "네오", LocalDate.parse("2024-04-30"),
                    getReservationTimeById(3L), getThemeById(3L)),
            Reservation.of(9L, "포비", LocalDate.parse("2024-05-01"),
                    getReservationTimeById(4L), getThemeById(3L)),
            Reservation.of(10L, "솔라", LocalDate.parse("2024-05-02"),
                    getReservationTimeById(5L), getThemeById(4L)),
            Reservation.of(11L, "브리", LocalDate.parse("2024-05-03"),
                    getReservationTimeById(1L), getThemeById(4L)),
            Reservation.of(12L, "리사", LocalDate.parse("2024-05-04"),
                    getReservationTimeById(2L), getThemeById(5L)),
            Reservation.of(13L, "왼손", LocalDate.parse("2024-05-05"),
                    getReservationTimeById(3L), getThemeById(6L)));

    public List<ReservationTime> getPreparedReservationTimes() {
        return preparedReservationTimes;
    }

    public List<Theme> getPreparedThemes() {
        return preparedThemes;
    }

    public List<Reservation> getPreparedReservations() {
        return preparedReservations;
    }

    public Reservation getReservationById(final Long id) {
        return preparedReservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findAny().orElseThrow(() -> new NoSuchElementException("해당하는 더미데이터가 없습니다."));
    }

    public ReservationTime getReservationTimeById(final Long id) {
        return preparedReservationTimes.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findAny().orElseThrow(() -> new NoSuchElementException("해당하는 더미데이터가 없습니다."));
    }

    public Theme getThemeById(final Long id) {
        return preparedThemes.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findAny().orElseThrow(() -> new NoSuchElementException("해당하는 더미데이터가 없습니다."));
    }

}
