package io.codelex.flightplanner

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.stereotype.Component

import static org.springframework.http.HttpEntity.EMPTY
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpStatus.OK

@Component
class TestApi {
    @Autowired
    TestRestTemplate testingApiTemplate

    void clearFlights() {
        def response = testingApiTemplate.exchange('/clear', POST, EMPTY, Void)
        assert response.statusCode == OK
    }
}
