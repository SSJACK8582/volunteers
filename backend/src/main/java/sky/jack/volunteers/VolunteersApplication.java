package sky.jack.volunteers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude = {RedisRepositoriesAutoConfiguration.class})
public class VolunteersApplication {

    public static void main(String[] args) {
        SpringApplication.run(VolunteersApplication.class, args);
    }

}
