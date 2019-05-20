package io.codelex.flightplanner

import io.codelex.flightplanner.model.Airport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

import static io.codelex.flightplanner.model.Airport.getLIST_TYPE
import static org.springframework.http.HttpEntity.EMPTY
import static org.springframework.http.HttpMethod.GET

@Component
class CustomerAirportApi {
    @Autowired
    TestRestTemplate publicApiTemplate

    ResponseEntity<List<Airport>> search(String search) {
        def uri = UriComponentsBuilder.fromPath('/airports')
                .queryParam('search', search)
                .build().toUriString()
        return publicApiTemplate.exchange(uri, GET, EMPTY, LIST_TYPE)
    }
}
