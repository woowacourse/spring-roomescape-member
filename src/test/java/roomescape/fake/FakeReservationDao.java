package roomescape.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Reservation;
import roomescape.business.domain.Theme;
import roomescape.persistence.dao.ReservationDao;
import roomescape.persistence.entity.PlayTimeEntity;
import roomescape.persistence.entity.ReservationEntity;
import roomescape.persistence.entity.ThemeEntity;
import roomescape.presentation.dto.reservation.ReservationAvailableTimeResponse;
import roomescape.presentation.dto.reservation.ReservationFilterDto;

public class FakeReservationDao implements ReservationDao {

    private final List<ReservationEntity> reservations = new ArrayList<>();
    private final List<PlayTimeEntity> times;

    private int index = 1;

    public FakeReservationDao(final List<PlayTimeEntity> times) {
        this.times = times;
        final ReservationEntity dummy = new ReservationEntity(null, null, null, null, null);
        reservations.add(dummy);
    }

    @Override
    public Long save(final Reservation reservation) {
        final ReservationEntity temp = ReservationEntity.from(reservation);
        final ReservationEntity reservationEntity = new ReservationEntity(
                (long) index,
                temp.userEntity(), temp.date(), temp.playTimeEntity(), temp.themeEntity()
        );
        reservations.add(index, reservationEntity);

        return (long) index++;
    }

    @Override
    public List<Reservation> findAll() {
        return reservations.stream()
                .filter(reservationEntity -> reservationEntity.id() != null)
                .filter(reservationEntity -> times.stream()
                        .filter(timeEntity -> timeEntity.id() != null)
                        .anyMatch(
                                timeEntity -> Objects.equals(reservationEntity.playTimeEntity().id(), timeEntity.id()))
                )
                .map(ReservationEntity::toDomain)
                .toList();
    }

    @Override
    public boolean remove(final Long id) {
        try {
            reservations.remove(reservations.get(Math.toIntExact(id)));
            index--;
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean existsByDateAndTimeAndTheme(
            final LocalDate date,
            final PlayTime time,
            final Theme theme
    ) {
        final String formattedDate = ReservationEntity.formatDate(date);
        final Long timeId = time.getId();
        final Long themeId = theme.getId();

        return reservations.stream()
                .filter(reservationEntity -> reservationEntity.id() != null)
                .anyMatch(reservationEntity ->
                        reservationEntity.date().equals(formattedDate) &&
                        reservationEntity.playTimeEntity().id().equals(timeId) &&
                        reservationEntity.themeEntity().id().equals(themeId)
                );
    }

    @Override
    public List<ReservationAvailableTimeResponse> findAvailableTimesByDateAndTheme(
            final LocalDate date,
            final Theme theme
    ) {
        final ThemeEntity themeEntity = ThemeEntity.from(theme);
        final String formatDate = ReservationEntity.formatDate(date);

        final List<ReservationAvailableTimeResponse> result = new ArrayList<>();
        int timeIndex = 0;
        for (final PlayTimeEntity time : times) {
            if (time.id() == null) {
                timeIndex ++;
                continue;
            }
            boolean alreadyBooked = false;

            for (final ReservationEntity reservation : reservations) {
                if (reservation.id() == null) {
                    continue;
                }
                if (reservation.playTimeEntity().equals(time)
                    && reservation.date().equals(formatDate)
                    && reservation.themeEntity().equals(themeEntity)) {
                    alreadyBooked = true;
                    break;
                }
            }
            result.add(new ReservationAvailableTimeResponse(time.startAt(), (long) timeIndex, alreadyBooked));
            timeIndex++;
        }

        return result;
    }

    @Override
    public List<Reservation> findByFilter(final ReservationFilterDto filter) {
        return List.of();
    }
}
