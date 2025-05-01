package roomescape.fake;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.exceptions.EntityNotFoundException;
import roomescape.repository.ReservationRepository;

public class ReservationFakeRepository implements ReservationRepository {

    private final Map<Long, Reservation> reservations = new HashMap<>();
    private final Map<Long, Theme> themes = new HashMap<>();
    private final Map<Long, ReservationTime> reservationTimes = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ReservationFakeRepository() {
        ReservationTime defaultTime = new ReservationTime(1L, LocalTime.MAX);
        reservationTimes.put(1L, defaultTime);

        Theme defaultTheme = new Theme(
                1L,
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        themes.put(1L, defaultTheme);

        long reservationId = idGenerator.getAndIncrement();
        Reservation defaultReservation = new Reservation(
                reservationId,
                "브라운",
                LocalDate.MAX,
                defaultTime,
                defaultTheme);
        reservations.put(reservationId, defaultReservation);
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }

    @Override
    public Reservation save(Reservation reservation) {
        Long timeId = reservation.time().id();
        if (!reservationTimes.containsKey(timeId)) {
            throw new EntityNotFoundException("예약 시간을 찾을 수 없습니다: " + timeId);
        }

        ReservationTime time = reservationTimes.get(timeId);
        Long themeId = reservation.theme().id();
        Theme theme = themes.get(themeId);

        long newId = idGenerator.getAndIncrement();

        Reservation savedReservation = new Reservation(
                newId,
                reservation.name(),
                reservation.date(),
                time,
                theme);

        reservations.put(newId, savedReservation);

        return savedReservation;
    }

    @Override
    public void deleteById(long id) {
        if (!reservations.containsKey(id)) {
            throw new EntityNotFoundException("예약 데이터를 찾을 수 없습니다:" + id);
        }

        reservations.remove(id);
    }

    @Override
    public List<Long> findBookedTimeIdsByDateAndThemeId(LocalDate date, Long themeId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.date().equals(date))
                .map(Reservation::time)
                .map(ReservationTime::id)
                .filter(id -> id.equals(themeId)).toList();
    }
}
