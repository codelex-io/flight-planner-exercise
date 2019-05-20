package io.codelex.flightplanner.model

class PageResult<T> {
    long page
    long totalItems
    List<T> items
}
