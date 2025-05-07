package roomescape.support.page;

public class PageRequest {
    private final int pageNo;
    private final int pageSize;
    private final String sortBy;
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
