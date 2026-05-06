package roomescape.theme.service.dto;

import roomescape.theme.repository.AvailableTimeQueryResult;

import java.util.List;

public record AvailableTimesResult(List<AvailableTimeQueryResult> availableTimeQueryResults) {
}
