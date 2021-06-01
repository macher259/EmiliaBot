package org.jnp2;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class MovieListGetter implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        // New message should contain list of movies to pick in JSON format as a body and Telegram headers.
        String newBody = newExchange.getIn().getBody(String.class);
        newBody = newBody.substring(newBody.indexOf("["), newBody.lastIndexOf("]") + 1);
        oldExchange.getIn().setBody(newBody);
        return oldExchange;
    }
}
