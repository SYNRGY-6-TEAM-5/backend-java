package com.finalproject.Tiket.Pesawat.specification;

import com.finalproject.Tiket.Pesawat.model.Airport;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class AirportSpecification implements Specification<Airport> {
        private final String search;

    public AirportSpecification(String search) {
        this.search = search;
    }

    @Override
    public Predicate toPredicate(Root<Airport> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (search == null || search.isEmpty()) {
            return cb.isTrue(cb.literal(true));
        }
        return cb.or(
                cb.like(root.get("airPortName"), "%" + search + "%"),
                cb.like(root.get("iataCode"), "%" + search + "%"),
                cb.like(root.get("cityName"), "%" + search + "%"),
                cb.like(root.get("countryName"), "%" + search + "%")
        );
    }
}

