package org.jnp2;

import org.apache.camel.Exchange;

import java.util.ArrayList;
import java.util.Random;

public class Emilia {

    public String process(Exchange exchange) {
        Random random = new Random();
        ArrayList<Movie> movieList = exchange.getIn().getBody(ArrayList.class);

        int chosen = random.nextInt(movieList.size());

        return "Emilia picked: " + System.lineSeparator()
                + movieList.get(chosen).toString();
    }
}
