package hu.webuni.student.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableAsync
//@EnableSchedulerLock(defaultLockAtMostFor = "1m") // ha vmeik node lefoglalta a lockot, es esetleg az a node elhalna, es ezert nem tudja felszabaditani a lefutott task utan, akkor max mennyi ideig eljen, percben
public class SchedulerConfig {

//    @Bean
//    public LockProvider lockProvider(DataSource dataSource) {
//        return new JdbcTemplateLockProvider(
//                JdbcTemplateLockProvider.Configuration.builder()
//                        .withJdbcTemplate(new JdbcTemplate(dataSource)) //springes jdbc template !!!
//                        .usingDbTime() // Works on Postgres, MySQL, MariaDb, MS SQL, Oracle, DB2, HSQL and H2
//                        .build()
//        );
//    }

}
