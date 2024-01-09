package hu.webuni.student;

import hu.webuni.student.service.DeleteImageDirectory;
import hu.webuni.student.service.InitDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@RequiredArgsConstructor
@EnableAsync // for ws
@EnableCaching
//@SpringBootApplication
//ha security nélkül akarjuk futtatni:
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class StudentApplication implements CommandLineRunner{

	public static int testNumber = 0;

	@Autowired
	InitDbService initDbService;

	@Autowired
	DeleteImageDirectory deleteImageDirectory;

	public static void main(String[] args) {
		SpringApplication.run(StudentApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		// airportService.createFlight();
//		System.out.println(priceService.getFinalPrice(200));
//		System.out.println(priceService.getFinalPrice(20000));

//		initDbService.createUsersIfNeeded();


//		initDbService.deleteDb();
//		initDbService.deleteAudTables();

		initDbService.addInitData();

		deleteImageDirectory.deleteAllFiles(); // delete image directory while starting app




	}

//teszt



}
