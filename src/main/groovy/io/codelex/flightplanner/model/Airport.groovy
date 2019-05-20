package io.codelex.flightplanner.model

import groovy.transform.EqualsAndHashCode
import org.springframework.core.ParameterizedTypeReference

@EqualsAndHashCode
class Airport {
    static ParameterizedTypeReference LIST_TYPE = new ParameterizedTypeReference<List<Airport>>() {}
    String country
    String city
    String airport
}
