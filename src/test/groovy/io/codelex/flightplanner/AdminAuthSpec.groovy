package io.codelex.flightplanner


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.UNAUTHORIZED

class AdminAuthSpec extends BaseSpecification {
    @Autowired
    AdminFlightApi adminFlightApi

    @Autowired
    TestRestTemplate adminApiTemplate

    def 'admin should not be able to access flights with wrong password'() {
        given:
            def restTemplate = adminApiTemplate.withBasicAuth('admin', '00000000')
        expect:
            restTemplate.getForEntity('/flights/123', Void).statusCode == UNAUTHORIZED
    }

    def 'admin should be able to access flights with correct password'() {
        expect:
            adminFlightApi.fetchFlight(123).statusCode == NOT_FOUND
    }
}
