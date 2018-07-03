package cf.nirvandil.fhdomainadder;

import cf.nirvandil.fhdomainadder.service.cloud.impl.DummyRestExceptionHandler;
import com.jcraft.jsch.JSch;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class FhDomainAdderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FhDomainAdderApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DummyRestExceptionHandler());
        return restTemplate;
    }

    @Bean
    public JSch jSch() {
        return new JSch();
    }

}
