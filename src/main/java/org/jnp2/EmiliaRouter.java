package org.jnp2;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jackson.ListJacksonDataFormat;

public class EmiliaRouter extends RouteBuilder {
    public static final String MESSAGE = "You stand in the presence of Daenerys Stormborn of House Targaryen, rightful heir to the Iron Throne, rightful Queen of the Andals, and the First Men, Protector of the Seven Kingdoms, the Mother of Dragons, the Khaleesi of the Great Grass Sea, the Unburnt, the Breaker of Chains.";
    public static final String HELP = "pick = Emilia, please pick me a movie to watch";

    private String parseBody(Exchange exchange) {
        String body = exchange.getIn().getBody(String.class);
        body = body.replaceAll("\\W+", "");
        body = body.toUpperCase();
        return body;
    }

    @Override
    public void configure() throws Exception {
        restConfiguration().component("http");
        restConfiguration().host("api.themoviedb.org/3/");
        restConfiguration().apiVendorExtension(true);
        JacksonDataFormat format = new ListJacksonDataFormat(Movie.class);

        from("telegram:bots")
            .to("log:INFO?showHeaders=true")
            .choice()
            .when(exchange -> "PICK".equals(parseBody(exchange)))
                .enrich("rest:get:/movie/top_rated?api_key={{api.api_key}}&page={{api.page}}", new MovieListGetter())
                .to("log:INFO")
                .unmarshal(format)
                .to("log:INFO")
                .bean(Emilia.class)
                .to("log:INFO?showHeaders=true")
                .to("telegram:bots")
            .when(exchange -> "HELP".equals(parseBody(exchange)))
                .setBody(simple(HELP))
                .to("telegram:bots")
            .otherwise()
                .setBody(simple(MESSAGE))
                .to("telegram:bots");
    }
}
