package io.codelex.flightplanner.model


import org.springframework.core.ParameterizedTypeReference

import java.time.LocalDateTime

class Flight {
    static ParameterizedTypeReference LIST_TYPE = new ParameterizedTypeReference<List<Flight>>() {}
    static ParameterizedTypeReference PAGE_TYPE = new ParameterizedTypeReference<PageResult<Flight>>() {}

    Long id
    Airport from
    Airport to
    String carrier
    LocalDateTime departureTime
    LocalDateTime arrivalTime
}
