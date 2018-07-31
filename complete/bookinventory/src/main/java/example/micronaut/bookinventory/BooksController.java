package example.micronaut.bookinventory;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.tracing.annotation.ContinueSpan;
import io.micronaut.tracing.annotation.SpanTag;
import io.micronaut.validation.Validated;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Validated
@Controller("/books")
public class BooksController {

    @ContinueSpan  // <1>
    @Produces(MediaType.TEXT_PLAIN)
    @Get("/stock/{isbn}")
    public Boolean stock(@SpanTag("stock.isbn") @NotBlank String isbn) { // <2>
        Optional<BookInventory> bookInventoryOptional = bookInventoryByIsbn(isbn);
        if (!bookInventoryOptional.isPresent()) {
            return null;
        }
        BookInventory bookInventory = bookInventoryOptional.get();
        return bookInventory.getStock() > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    private Optional<BookInventory> bookInventoryByIsbn(String isbn) {
        if(isbn.equals("1491950358")) {
            return Optional.of(new BookInventory(isbn, 4));

        } else if(isbn.equals("1680502395")) {
            return Optional.of(new BookInventory(isbn, 0));
        }
        return Optional.empty();
    }
}
