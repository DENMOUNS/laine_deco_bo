package cm.dolers.laine_deco.infrastructure.config;

public class PaginationConstants {
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIR = "asc";

    public static int normalizePageSize(int requested, int max) { return Math.min(Math.max(requested, 1), max); }

    public static int normalizePageSize(int requested) { return Math.max(requested, 1); }
}


