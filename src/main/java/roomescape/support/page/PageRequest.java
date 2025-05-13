package roomescape.support.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class PageRequest {
    @Schema(description = "페이지 번호 (0부터 시작)", defaultValue = "0")
    @JsonProperty(defaultValue = "0")
    private final int pageNo;

    @Schema(description = "페이지 크기", defaultValue = "10")
    @JsonProperty(defaultValue = "10")
    private final int pageSize;

    @Schema(description = "정렬 기준", defaultValue = "id")
    @JsonProperty(defaultValue = "id")
    private final String sortBy;

    @Schema(description = "정렬 방향", defaultValue = "asc")
    @JsonProperty(defaultValue = "asc")
    private final String sortDir;
    
    public PageRequest() {
        this.pageNo = 0;
        this.pageSize = 10;
        this.sortBy = "id";
        this.sortDir = "asc";
    }

    public PageRequest(final int pageNo, final int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.sortBy = "id";
        this.sortDir = "asc";
    }

    public PageRequest(final int pageNo, final int pageSize, final String sortBy, final String sortDir) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
        this.sortDir = sortDir;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getSortDir() {
        return sortDir;
    }
}
