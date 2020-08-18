package vaninside.kubiot.httpclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ImageWebClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageWebClientApplication.class, args);
	}

}
