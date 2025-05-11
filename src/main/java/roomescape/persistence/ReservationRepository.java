package roomescape.persistence;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.business.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    List<Reservation> findAllByThemeAndMemberAndDate(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo);

    Long add(Reservation reservation);

    void deleteById(Long id);

    boolean existsByReservation(Reservation reservation);

    boolean existsByTimeId(Long timeId);

    boolean existByThemeId(Long id);

}
