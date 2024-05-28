package org.devices.server.device;

import org.devices.server.device.company.Company;
import org.devices.server.device.data.Device;
import org.devices.server.device.data.UserDevice;
import org.devices.server.device.type.Type;
import org.devices.server.user.User;
import org.devices.server.user.UserRepository;
import org.devices.server.user.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class DeviceServiceTest {
	@Autowired
	private DeviceService deviceService;

	@MockBean
	private DeviceRepository deviceRepository;

	@MockBean
	private UserDeviceRepository userDeviceRepository;

	@MockBean
	private UserRepository userRepository;

	private final User user = new User(1, "1@example.com", "11111111", null, null);
	private final User wrongUser = new User(0, "0@example.com", "00000000", null, null);

	@Test
	public void contentLoads() {
		assertNotNull(deviceService);
	}

	@Test
	public void getUserDevicesTest() {
		int id = 1;
		List<UserDevice> devices = List.of(
				new UserDevice(),
				new UserDevice()
		);
		when(userDeviceRepository.findByUserId(id)).thenReturn(devices);

		assertEquals(devices, deviceService.getUserDevices(id));
	}

	@Test
	public void addDevicesTest() {
		Integer id = 1;
		User user = new User();
		List<UserDevice> devices = List.of();

		when(userRepository.findById(id)).thenReturn(Optional.of(user));
		when(userDeviceRepository.saveAll(any())).thenReturn(devices);
		assertEquals(devices, deviceService.addDevices(List.of(), id));

		when(userRepository.findById(0)).thenReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class, () -> deviceService.addDevices(List.of(), 0));
	}

	@Test
	public void updateUserDevicesTest() throws AuthenticationException {
		List<UserDevice> devices = new ArrayList<>(List.of(
				new UserDevice(1, new Device(), user, false),
				new UserDevice(2, new Device(), user, false)
		));

		when(userDeviceRepository.findByUserId(user.getId())).thenReturn(new ArrayList<>(devices));
		for (UserDevice device : devices) {
			when(userDeviceRepository.findById(device.getId())).thenReturn(Optional.of(device));
		}
		when(userDeviceRepository.saveAll(devices)).thenReturn(devices);
		deviceService.updateUserDevices(user.getId(), devices);

		List<UserDevice> wrongDevices = List.of(
				new UserDevice(1, new Device(), wrongUser, false)
		);
		when(userDeviceRepository.findById(wrongDevices.get(0).getId())).thenReturn(Optional.of(wrongDevices.get(0)));
		assertThrows(AuthenticationException.class, () -> deviceService.updateUserDevices(user.getId(), wrongDevices));

		when(userDeviceRepository.findById(wrongDevices.get(0).getId())).thenReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class, () -> deviceService.updateUserDevices(user.getId(), wrongDevices));
	}

	@Test
	public void deleteUserDevicesTest() throws AuthenticationException {
		UserDevice device = new UserDevice(1, new Device(), user, false);
		UserDevice wrongDevice = new UserDevice(1, new Device(), wrongUser, false);

		when(userDeviceRepository.findById(device.getId())).thenReturn(Optional.of(device));
		deviceService.deleteUserDevice(user.getId(), device);

		when(userDeviceRepository.findById(wrongDevice.getId())).thenReturn(Optional.of(wrongDevice));
		assertThrows(AuthenticationException.class, () -> deviceService.deleteUserDevice(user.getId(), wrongDevice));

		when(userDeviceRepository.findById(wrongDevice.getId())).thenReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class, () -> deviceService.deleteUserDevice(user.getId(), wrongDevice));
	}

	@Test
	public void getDevicesTest() {
		Company company1 = new Company(1, "1", null);
		Company company2 = new Company(2, "2", null);
		Type type1 = new Type(1, "1", null);
		Type type2 = new Type(2, "2", null);

		Device device1 = new Device(1, "1", company1, type1, null, 1.0);
		Device device2 = new Device(2, "2", company2, type1, null, 2.0);
		Device device3 = new Device(3, "3", company1, type2, null, 3.0);

		List<Device> devices = List.of(device1, device2, device3);

		when(deviceRepository.findAll()).thenReturn(devices);
		assertEquals(devices, deviceService.getDevices(0, 0));
		assertEquals(List.of(device1, device3), deviceService.getDevices(company1.getId(), 0));
		assertEquals(List.of(device1, device2), deviceService.getDevices(0, type1.getId()));
		assertEquals(List.of(device1), deviceService.getDevices(company1.getId(), type1.getId()));
		assertEquals(List.of(), deviceService.getDevices(company2.getId(), type2.getId()));
	}
}