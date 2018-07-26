package example.micronaut.bookrecommendation;

import io.micronaut.http.annotation.QueryValue;
import io.reactivex.Maybe;

import javax.validation.constraints.NotBlank;

public interface BookInventoryOperations {
    Maybe<Boolean> stock(@QueryValue @NotBlank String isbn);
}
