package com.marjorie.scoop

import com.marjorie.scoop.home.HomeController
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ScoopApplicationTests {

	@Autowired
	private val homeController: HomeController? = null

	@Test
	fun `Context creates a controller`() {
		assertThat(homeController).isNotNull;
	}

}
