package roomescape.domain;

import java.util.List;

public record QueryWithParams(String query, List<String> params) {
}
