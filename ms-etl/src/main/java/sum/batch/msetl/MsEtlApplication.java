package sum.batch.msetl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan({"sum.batch.*"})
@ComponentScan({"sum.batch.*"})
@EnableJpaRepositories({"sum.batch.*"})
public class MsEtlApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsEtlApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
