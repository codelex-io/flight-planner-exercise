package io.codelex.flightplanner

import io.codelex.flightplanner.model.AddFlightRequest
import io.codelex.flightplanner.model.Flight
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

import static org.springframework.http.HttpEntity.EMPTY
import static org.springframework.http.HttpMethod.*

@Component
class AdminFlightApi {
    @Autowired
    TestRestTemplate adminApiTemplate

    ResponseEntity<Flight> addFlight(AddFlightRequest req, Class responseType = Flight) {
        return adminApiTemplate.exchange('/flights', PUT, new HttpEntity<>(req), responseType)
    }

    ResponseEntity<Flight> fetchFlight(Long id, Class responseType = Flight) {
        return adminApiTemplate.exchange('/flights/' + id, GET, EMPTY, responseType)
    }

    ResponseEntity<?> deleteFlight(Long id) {
        return adminApiTemplate.exchange('/flights/' + id, DELETE, EMPTY, Void)
    }
}
