package roomescape.theme.dto;

import roomescape.globalexception.RequestInvalidException;

public record ThemeRequest(
        String name,
        String description,
        String thumbnail
) {
    public ThemeRequest {
        if(name == null || description == null || thumbnail == null){
            throw new RequestInvalidException();
        }
    }
}
