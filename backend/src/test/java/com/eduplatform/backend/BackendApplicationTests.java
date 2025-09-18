package com.eduplatform.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")  // Ensure it picks up the test configuration
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
