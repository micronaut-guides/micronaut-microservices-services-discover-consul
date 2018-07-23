//tag::packageandimports[]
package example.micronaut.bookrecommendation;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.Client;
import io.reactivex.Flowable;
//end::packageandimports[]

/*
//tag::harcoded[]
@Client("http://localhost:8081")
//end::harcoded[]
*/
//tag::consul[]
@Client(id = "bookcatalogue")
//end::consul[]
//tag::clazz[]
public interface BookCatalogueClient extends BookCatalogueOperations {

    @Get("/books")
    Flowable<Book> findAll();
}
//end::clazz[]
