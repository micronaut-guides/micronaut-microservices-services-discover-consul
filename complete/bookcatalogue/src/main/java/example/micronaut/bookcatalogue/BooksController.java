package example.micronaut.bookcatalogue;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.reactivex.Flowable;

import static java.lang.Thread.sleep;

@Controller("/books")
public class BooksController {

    @Get("/")
    Flowable<Book> index() {
        Book buildingMicroservices = new Book("1491950358", "Building Microservices");
        Book releaseIt = new Book("1680502395", "Release It!");
        Book cidelivery = new Book("0321601912", "Continuous Delivery:");
        return Flowable.just(buildingMicroservices, releaseIt, cidelivery);
    }
}
