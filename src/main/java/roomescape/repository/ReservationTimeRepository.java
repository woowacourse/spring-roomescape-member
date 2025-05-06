package roomescape.repository;

import java.util.List;
import roomescape.domain.ReservationTime;
import roomescape.dto.BookedReservationTimeResponseDto;

public interface ReservationTimeRepository {

    List<ReservationTime> findAllReservationTimes();

    void saveReservationTime(ReservationTime reservationTime);

    void deleteReservationTime(Long id);

    ReservationTime findById(Long id);

    List<BookedReservationTimeResponseDto> findBookedReservationTime(String date, Long themeId);

}
