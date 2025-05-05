package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;
import roomescape.dto.BookedReservationTimeResponseDto;

public interface ReservationTimeDao {

    List<ReservationTime> findAllReservationTimes();

    long saveReservationTime(ReservationTime reservationTime);

    void deleteReservationTime(Long id);

    Optional<ReservationTime> findById(Long id);

    List<BookedReservationTimeResponseDto> findBookedReservationTime(String date, Long themeId);
}
