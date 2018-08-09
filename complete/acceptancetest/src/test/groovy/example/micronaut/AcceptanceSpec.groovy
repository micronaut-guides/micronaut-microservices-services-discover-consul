package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import org.junit.Assume
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

class AcceptanceSpec extends Specification implements MicroserviceHealth {

    @Shared
    @AutoCleanup
    ApplicationContext applicationContext = ApplicationContext.run()

    @Shared
    @AutoCleanup
    RxHttpClient client = applicationContext.createBean(RxHttpClient, 'http://localhost:8080')

    def "verifies three microservices collaborate together with consul service registration"() {

        given:
        Assume.assumeTrue(isUp('http://localhost:8080'))
        Assume.assumeTrue(isUp('http://localhost:8081'))
        Assume.assumeTrue(isUp('http://localhost:8082'))

        when:
        List<BookRecommendation> books = client.toBlocking().retrieve(HttpRequest.GET('/books'), Argument.of(List, BookRecommendation))

        then:
        books
        books*.name.first() == "Building Microservices"
    }
}
