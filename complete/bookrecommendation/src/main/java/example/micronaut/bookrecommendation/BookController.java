package example.micronaut.bookrecommendation;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Publisher;

@Controller("/books") // <1>
public class BookController {

    private final BookCatalogueOperations bookCatalogueOperations;
    private final BookInventoryOperations bookInventoryOperations;

    public BookController(BookCatalogueOperations bookCatalogueOperations,
                          BookInventoryOperations bookInventoryOperations) { // <2>
        this.bookCatalogueOperations = bookCatalogueOperations;
        this.bookInventoryOperations = bookInventoryOperations;
    }

    @Get("/") // <3>
    public Flowable<BookRecommendation> index() {
        return bookCatalogueOperations.findAll()
                .parallel(10)
                .runOn(Schedulers.io())
                .filter(b -> {
                    try {
                        Single<HttpResponse<Boolean>> httpResponseSingle = bookInventoryOperations.stock(b.getIsbn());
                        return httpResponseSingle.blockingGet().body();
                    } catch (HttpClientException e) {
                        return Boolean.FALSE;
                    }
                })
                .map(b -> new BookRecommendation(b.getName()))
                .sequential();
    }
}
