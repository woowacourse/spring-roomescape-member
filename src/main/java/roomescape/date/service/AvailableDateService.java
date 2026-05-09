package roomescape.date.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.closeddate.domain.ClosedDate;
import roomescape.closeddate.repository.ClosedDateRepository;

@Slf4j
@Service
public class AvailableDateService {
    private final ClosedDateRepository closedDateRepository;

    public AvailableDateService(ClosedDateRepository closedDateRepository) {
        this.closedDateRepository = closedDateRepository;
    }

    @Transactional(readOnly = true)
    public List<LocalDate> readAvailableDates() {
        LocalDate today = LocalDate.now();
        Set<LocalDate> closedDates = closedDateRepository.findAll().stream()
                .map(ClosedDate::date)
                .collect(Collectors.toSet());

        return today.datesUntil(today.plusDays(30))
                .filter(date -> !closedDates.contains(date))
                .toList();
    }
}
