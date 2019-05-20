package io.codelex.flightplanner

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles('test')
class BaseSpecification extends Specification {
    @Autowired
    TestApi testApi

    def setup() throws Exception {
        testApi.clearFlights()
    }
}
