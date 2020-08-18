package vaninside.kubiot.webclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

@EnableScheduling
@SpringBootApplication
public class TestWebClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestWebClientApplication.class, args);
		/*
	    WebClient client = WebClient.create("http://localhost:8090");
	    ParameterizedTypeReference<ServerSentEvent<String>> type
	     = new ParameterizedTypeReference<ServerSentEvent<String>>() {};
	
	     WebClient cl =  WebClient.builder()
	    		    .baseUrl("http://localhost:8090")
	    		    .clientConnector((ClientHttpConnector) new ReactorClientHttpConnector(
                 HttpClient.create().followRedirect(true)
         ))
         .build();
	     
	     
	    Flux<ServerSentEvent<String>> eventStream = cl.post()
	      .uri("/sseTest").retrieve()
	      .bodyToFlux(type);
	 
	    eventStream.subscribe(System.out::println);
*/
	}

}
