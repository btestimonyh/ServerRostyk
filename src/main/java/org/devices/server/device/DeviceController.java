package org.devices.server.device;

import lombok.RequiredArgsConstructor;
import org.devices.server.config.JwtService;
import org.devices.server.device.data.Device;
import org.devices.server.device.data.UserDevice;
import org.devices.server.util.MainLogger;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequestMapping("api/device")
@RequiredArgsConstructor
public class DeviceController {
	private final DeviceService deviceService;
	private final JwtService jwtService;

	@GetMapping("/user")
	public List<UserDevice> getUserDevices(@RequestHeader(name = "Authorization") String token) {
		Integer id = getUserId(token);
		MainLogger.log.info("User {} requested his devices", id);
		return deviceService.getUserDevices(id);
	}

	@PostMapping("/user")
	public List<UserDevice> addDevices(@RequestHeader(name = "Authorization") String token,
						  @RequestBody List<Device> devices) {
		Integer id = getUserId(token);
		MainLogger.log.info("User {} requested to add {} devices", id, devices.size());
		return deviceService.addDevices(devices, id);
	}

	@PatchMapping("/user")
	public void updateUserDevices(@RequestHeader(name = "Authorization") String token,
							  @RequestBody List<UserDevice> userDevices)
			throws AuthenticationException {
		Integer id = getUserId(token);
		MainLogger.log.info("User {} requested to update {} devices", id, userDevices.size());
		deviceService.updateUserDevices(id, userDevices);
	}

	@DeleteMapping("/user")
	public void deleteUserDevice(@RequestHeader(name = "Authorization") String token,
								  @RequestBody UserDevice userDevice)
			throws AuthenticationException {
		Integer id = getUserId(token);
		MainLogger.log.info("User {} requested to delete device {}", id, userDevice.getId());
		deviceService.deleteUserDevice(id, userDevice);
	}

	@GetMapping(path = "/all")
	public List<Device> getDevices(@RequestParam Integer company,
									  @RequestParam Integer type) {
		MainLogger.log.info("User requested all devices from company {} and of type {}", company, type);
		return deviceService.getDevices(company, type);
	}

	private Integer getUserId(String token) {
		token = token.substring(7);
		return (Integer) jwtService.extractClaim(token, c -> c.get("id"));
	}
}
