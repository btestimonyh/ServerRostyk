package org.devices.server.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class UserRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	@Test
	public void findByUsernameTest() {
		UserType userType = new UserType(null, "user", null);
		userType = entityManager.persist(userType);

		User user = new User(null, "1@example.com", "11111111", userType, null);
		user = entityManager.persist(user);
		entityManager.flush();

		assertEquals(user, userRepository.findByEmail(user.getEmail()).orElse(null));
		assertEquals(Optional.empty(), userRepository.findByEmail("2@example.com"));
	}
}