package hr.fitbit.demo.fitbitconnect.apisupport.pagination;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * Creates Link representation to be something like:
 * Link=<http://localhost:8080/rest/api/admin/foo?page=0&size=2&sort=asc>; rel=”first”,
 * <http://localhost:8080/rest/api/admin/foo?page=103&size=2&sort=asc>; rel=”last”
 */
public class PaginationSetupTest {

    private static final String X_TOTAL_COUNT = "X-Total-Count";
    private static final String SCHEME = "http";
    private static final String HOST = "localhost";
    private static final String[] PATH_SEGMENTS = {"api", "pagination", "test"};
    private static final int PORT = 23456;
    private static final int NUMBER_OF_INTEGERS = 50;
    private static final int PAGE_SIZE = 5;
    private static final String FIRST_PAGE_URL = "<http://localhost:23456/api/pagination/test?page=1&size=" + PAGE_SIZE + ">;rel=\"next\","
            + "<http://localhost:23456/api/pagination/test?page=9&size=" + PAGE_SIZE + ">;rel=\"last\"";
    private static final String LAST_PAGE_URL = "<http://localhost:23456/api/pagination/test?page=0&size=" + PAGE_SIZE + ">;rel=\"first\","
            + "<http://localhost:23456/api/pagination/test?page=8&size=" + PAGE_SIZE + ">;rel=\"prev\"";
    private static final String FIFTH_PAGE_URL = "<http://localhost:23456/api/pagination/test?page=5&size=" + PAGE_SIZE + ">;rel=\"next\","
            + "<http://localhost:23456/api/pagination/test?page=9&size=" + PAGE_SIZE + ">;rel=\"last\","
            + "<http://localhost:23456/api/pagination/test?page=0&size=" + PAGE_SIZE + ">;rel=\"first\","
            + "<http://localhost:23456/api/pagination/test?page=3&size=" + PAGE_SIZE + ">;rel=\"prev\"";

    private UriComponentsBuilder uriComponentsBuilder;
    private List<Integer> integerList;

    @BeforeEach
    public void init() {
        // http://localhost:23456/api/pagination/test

        uriComponentsBuilder = UriComponentsBuilder.newInstance()
                .scheme(SCHEME)
                .host(HOST)
                .port(PORT)
                .pathSegment(PATH_SEGMENTS);

        integerList = IntStream.rangeClosed(0, NUMBER_OF_INTEGERS - 1).boxed().collect(Collectors.toList());
    }

    @Test
    public void shouldCreatePaginationHeadersAndPageLinksForFirstPage() {

        createPaginationTest(PAGE_SIZE, 0, FIRST_PAGE_URL);
    }

    @Test
    public void shouldCreatePaginationHeadersAndPageLinksForLastPage() {

        createPaginationTest(PAGE_SIZE, 9, LAST_PAGE_URL);
    }

    @Test
    public void shouldCreatePaginationHeadersAndPageLinksForFifthPage() {

        createPaginationTest(PAGE_SIZE, 4, FIFTH_PAGE_URL);
    }

    private void createPaginationTest(final Integer pageSize, final Integer pageNumber, final String pageUrl) {
        final PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        final int pageOffset = Long.valueOf(pageRequest.getOffset()).intValue();
        Page<Integer> newPage = new PageImpl<>(integerList.subList(pageOffset, pageOffset + pageSize), pageRequest, integerList.size());

        final HttpHeaders httpHeaders = PaginationSetup.createHeadersWithPagination(uriComponentsBuilder, newPage);

        assertThat(httpHeaders.get(X_TOTAL_COUNT).get(0)).isEqualTo(String.valueOf(NUMBER_OF_INTEGERS));
        assertThat(httpHeaders.get(HttpHeaders.LINK).get(0)).isEqualTo(pageUrl);
    }

}

