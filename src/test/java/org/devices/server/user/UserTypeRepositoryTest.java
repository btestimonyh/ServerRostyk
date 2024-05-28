package org.devices.server.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class UserTypeRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserTypeRepository userTypeRepository;

	@Test
	public void findByNameTest() {
		UserType userType = new UserType(null, "user", null);
		entityManager.persist(userType);
		entityManager.flush();

		assertEquals(userType, userTypeRepository.findByName("user"));
		assertEquals(null, userTypeRepository.findByName("other"));
	}
}