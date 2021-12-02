package hr.fitbit.demo.fitbitconnect.pagination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaginationSetup {

    private static final String FIRST = "first";
    private static final String LAST = "last";
    private static final String NEXT = "next";
    private static final String PREV = "prev";
    private static final String PAGE = "page";
    private static final String SIZE = "size";
    private static final String SORT = "sort";
    // headers
    private static final String X_TOTAL_COUNT = "X-Total-Count";
    private static final String LINK = "Link";

    public static <T> ResponseEntity<List<T>> returnContentAndHeadersWithPagination(
            final Page<T> page, final HttpServletRequest request) {

        Objects.requireNonNull(page, "Page<T> object should not be null");
        return ResponseEntity
                .ok()
                .headers(createHeadersWithPagination(ServletUriComponentsBuilder.fromRequest(request), page))
                .body(page.getContent());
    }

    /**
     * Creates Link representation to be something like:
     * Link=<http://localhost:8080/rest/api/admin/foo?page=0&size=2&sort=asc>; rel=”first”,
     * <http://localhost:8080/rest/api/admin/foo?page=103&size=2&sort=asc>; rel=”last”
     */
    public static <T> HttpHeaders createHeadersWithPagination(
            final UriComponentsBuilder uriComponentsBuilder, final Page<T> page) {

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(X_TOTAL_COUNT, String.valueOf(page.getTotalElements()));

        if (page.getTotalElements() > 0) {
            final Pageable pageable = page.getPageable();
            final List<Link> links = new ArrayList<>();

            if (!page.isLast()) {
                links.add(createPageLink(
                        uriComponentsBuilder, pageable.next(), NEXT));
                links.add(createPageLink(
                        uriComponentsBuilder, PageRequest.of(page.getTotalPages() - 1, page.getSize(), page.getSort()),
                        LAST));
            }

            if (!page.isFirst()) {
                links.add(createPageLink(
                        uriComponentsBuilder, pageable.first(), FIRST));
                links.add(createPageLink(
                        uriComponentsBuilder, pageable.previousOrFirst(), PREV));
            }

            if (!links.isEmpty()) {
                httpHeaders.add(LINK, Links.of(links).toString());
            }
        }
        return httpHeaders;
    }

    private static Link createPageLink(
            final UriComponentsBuilder uriComponentsBuilder, final Pageable pageable, final String rel) {

        uriComponentsBuilder.replaceQueryParam(PAGE, pageable.getPageNumber());
        uriComponentsBuilder.replaceQueryParam(SIZE, pageable.getPageSize());
        if (pageable.getSort().isSorted()) {
            uriComponentsBuilder.replaceQueryParam(SORT, pageable.getSort());
        } else {
            uriComponentsBuilder.replaceQueryParam(SORT);
        }
        return Link.of(uriComponentsBuilder.toUriString(), rel);
    }
}
