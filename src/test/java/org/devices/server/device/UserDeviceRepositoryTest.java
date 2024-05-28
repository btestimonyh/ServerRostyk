package org.devices.server.device;

import org.devices.server.device.company.Company;
import org.devices.server.device.company.CompanyRepository;
import org.devices.server.device.data.Device;
import org.devices.server.device.data.UserDevice;
import org.devices.server.device.type.Type;
import org.devices.server.device.type.TypeRepository;
import org.devices.server.user.User;
import org.devices.server.user.UserRepository;
import org.devices.server.user.UserType;
import org.devices.server.user.UserTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class UserDeviceRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserDeviceRepository userDeviceRepository;

	@Test
	public void findByUserIdTest() {
		UserType userType = new UserType(null, "user", null);
		userType = entityManager.persist(userType);

		User user = new User(null, "1@example.com", "11111111", userType, null);
		user = entityManager.persist(user);

		Company company = new Company(null, "company", null);
		company = entityManager.persist(company);
		Type type = new Type(null, "type", null);
		type = entityManager.persist(type);

		Device device1 = new Device(null, "1", company, type, null, 1.0);
		Device device2 = new Device(null, "1", company, type, null, 1.0);
		device1 = entityManager.persist(device1);
		device2 = entityManager.persist(device2);

		UserDevice userDevice1 = new UserDevice(null, device1, user, false);
		UserDevice userDevice2 = new UserDevice(null, device2, user, false);
		userDevice1 = entityManager.persist(userDevice1);
		userDevice2 = entityManager.persist(userDevice2);
		entityManager.flush();

		assertEquals(List.of(userDevice1, userDevice2), userDeviceRepository.findByUserId(user.getId()));
		assertEquals(List.of(), userDeviceRepository.findByUserId(2));
	}
}