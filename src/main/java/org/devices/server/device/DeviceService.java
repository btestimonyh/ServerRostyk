package org.devices.server.device;

import lombok.RequiredArgsConstructor;
import org.devices.server.device.data.Device;
import org.devices.server.device.data.UserDevice;
import org.devices.server.user.User;
import org.devices.server.user.UserRepository;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DeviceService {
	private final DeviceRepository deviceRepository;
	private final UserDeviceRepository userDeviceRepository;
	private final UserRepository userRepository;

	public List<UserDevice> getUserDevices(int userId) {
		return userDeviceRepository.findByUserId(userId);
	}

	public List<UserDevice> addDevices(List<Device> devices, Integer id) {
		User user = userRepository.findById(id).orElseThrow(() ->
				new IllegalArgumentException("User not found"));
		return userDeviceRepository.saveAll(devices.stream()
				.map((e) -> new UserDevice(0, e, user, false))
				.toList());
	}

	public void updateUserDevices(Integer userId, List<UserDevice> userDevices)
			throws AuthenticationException {
		List<UserDevice> devices = getUserDevices(userId);
		for (UserDevice userDevice : userDevices) {
			UserDevice device = userDeviceRepository.findById(userDevice.getId()).orElseThrow(() ->
					new IllegalArgumentException("UserDevice not found"));
			if (!Objects.equals(device.getUser().getId(), userId)) {
				throw new AuthenticationException("Device " + userDevice.getId() + " don't belong to user " + userId);
			}

			device.setUsed(userDevice.isUsed());
			devices.add(device);
		}

		userDeviceRepository.saveAll(devices);
	}

	public void deleteUserDevice(Integer id, UserDevice userDevice)
			throws AuthenticationException {
		UserDevice device = userDeviceRepository.findById(userDevice.getId()).orElseThrow(() ->
				new IllegalArgumentException("UserDevice not found"));
		if (!Objects.equals(device.getUser().getId(), id)) {
			throw new AuthenticationException("Device " + userDevice.getId() + " don't belong to user " + id);
		}
		userDeviceRepository.deleteById(userDevice.getId());
	}

	public List<Device> getDevices(Integer company, Integer type) {
		Stream<Device> devices = deviceRepository.findAll().stream();

		if (company != 0) {
			devices = devices.filter(d -> Objects.equals(d.getCompany().getId(), company));
		}
		if (type != 0) {
			devices = devices.filter(d -> Objects.equals(d.getType().getId(), type));
		}

		return devices.toList();
	}
}
