package org.gad.inventory_service.utils;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;


public class QueryUtils {
    private QueryUtils() {
    }

    public static <T extends Comparable<T>> Query buildOptionalDateRangeQuery(String fieldName,
                                                    T minValue,
                                                    T maxValue) {
        Criteria criteria;

        if (minValue != null && maxValue != null) {
            criteria = Criteria.where(fieldName).gte(minValue).lte(maxValue);
        } else if (minValue != null) {
            criteria = Criteria.where(fieldName).gte(minValue);
        } else if (maxValue != null) {
            criteria = Criteria.where(fieldName).lte(maxValue);
        } else {
            return new Query();
        }
        return new Query(criteria);
    }
}
