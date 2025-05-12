package roomescape.fake;

import roomescape.application.dto.ReservationInfoResponse;
import roomescape.domain.model.Reservation;
import roomescape.infrastructure.dao.ReservationDao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationDaoFake implements ReservationDao {

    private static final AtomicLong IDX = new AtomicLong();
    private static final Map<Long, Reservation> RESERVATIONS = new HashMap<>();

    @Override
    public Reservation save(Reservation reservation) {
        Long id = IDX.getAndIncrement();
        Reservation newReservation = new Reservation(id, reservation.getMemberId(), reservation.getDate(), reservation.getTime(), reservation.getTheme());
        RESERVATIONS.put(id, newReservation);
        return newReservation;
    }

    @Override
    public List<ReservationInfoResponse> findAll() {
        return RESERVATIONS.values().stream()
                .map(reservation -> new ReservationInfoResponse(reservation.getId(), null, reservation.getDate(), null, null))
                .toList();
    }

    @Override
    public List<ReservationInfoResponse> findByThemeIdAndMemberIdAndDate(final Long themeId, final Long memberId, final LocalDate dateFrom, final LocalDate dateTo) {
        return RESERVATIONS.values().stream()
                .filter(reservation -> {
                    boolean isThemeSame = reservation.getTheme().getId().equals(themeId);
                    boolean isMemberSame = reservation.getMemberId().equals(memberId);
                    boolean isBetweenDate = !reservation.getDate().isAfter(dateTo) && !reservation.getDate().isBefore(dateFrom);

                    return isThemeSame && isMemberSame && isBetweenDate;
                })
                .map(reservation -> new ReservationInfoResponse(reservation.getId(), null, reservation.getDate(), null, null))
                .toList();
    }

    @Override
    public int deleteById(Long id) {
        if (RESERVATIONS.containsKey(id)) {
            RESERVATIONS.remove(id);
            return 1;
        }
        return 0;
    }

    @Override
    public boolean existByTimeId(Long timeId) {
        long count = RESERVATIONS.entrySet().stream()
                .filter(e -> e.getValue().getTime().getId().equals(timeId))
                .count();
        return count != 0;
    }

    @Override
    public boolean existByThemeId(Long themeId) {
        long count = RESERVATIONS.entrySet().stream()
                .filter(e -> e.getValue().getTheme().getId().equals(themeId))
                .count();
        return count != 0;
    }

    @Override
    public boolean existByTimeIdAndThemeIdAndDate(Long timeId, Long themeId, LocalDate date) {
        long count = RESERVATIONS.entrySet().stream()
                .filter(e -> e.getValue().getTime().getId().equals(timeId)
                        && e.getValue().getTheme().getId().equals(themeId)
                        && e.getValue().getDate().equals(date))
                .count();
        return count != 0;
    }

    @Override
    public List<Long> findBookedTimes(final Long themeId, final LocalDate date) {
        return RESERVATIONS.values().stream()
                .filter(reservation -> reservation.getTheme().getId().equals(themeId) && reservation.getDate().equals(date))
                .map(reservation -> reservation.getTime().getId())
                .toList();
    }

    public void clear() {
        RESERVATIONS.clear();
        IDX.set(0);
    }
}
