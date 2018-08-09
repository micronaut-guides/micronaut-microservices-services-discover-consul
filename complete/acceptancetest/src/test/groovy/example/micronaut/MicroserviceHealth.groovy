package example.micronaut

import groovy.util.logging.Slf4j
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.exceptions.HttpClientException

@Slf4j
trait MicroserviceHealth {

    boolean isUp(String url) {
        String microservicesUrl = url.endsWith('/health') ? url : "${url}/health"
        try {
            StatusResponse statusResponse = client.toBlocking().retrieve(HttpRequest.GET(microservicesUrl), StatusResponse)
            if ( statusResponse.status == 'UP' ) {
                return true
            }
        } catch (HttpClientException e) {
        }
        return false

    }
}