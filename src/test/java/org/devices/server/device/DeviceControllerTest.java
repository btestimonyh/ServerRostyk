package org.devices.server.device;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.devices.server.config.JwtService;
import org.devices.server.device.data.Device;
import org.devices.server.device.data.UserDevice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class DeviceControllerTest {
	@Autowired
	private DeviceController deviceController;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private DeviceService deviceService;

	@MockBean
	private JwtService jwtService;

	private final String token = "1";
	private final Integer id = 1;

	@BeforeEach
	void setUp() {
		when(jwtService.extractClaim(any(), any())).thenReturn(1);
	}

	@Test
	public void contentLoads() {
		assertNotNull(deviceController);
	}

	@Test
	@WithMockUser
	public void getUserDevicesTest() throws Exception {
		List<UserDevice> userDevices = List.of(new UserDevice());
		when(deviceService.getUserDevices(id)).thenReturn(userDevices);
		mvc.perform(MockMvcRequestBuilders.get("/api/device/user")
						.header("Authorization", "Bearer " + token))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(userDevices)));
	}

	@Test
	@WithMockUser
	public void addDevicesTest() throws Exception {
		List<UserDevice> userDevices = List.of(new UserDevice());
		mvc.perform(MockMvcRequestBuilders.post("/api/device/user")
						.content(objectMapper.writeValueAsString(userDevices))
						.header("Content-type", "application/json")
						.header("Authorization", "Bearer " + token))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void updateUserDevicesTest() throws Exception {
		List<UserDevice> userDevices = List.of(new UserDevice());
		mvc.perform(MockMvcRequestBuilders.patch("/api/device/user")
						.content(objectMapper.writeValueAsString(userDevices))
						.header("Content-type", "application/json")
						.header("Authorization", "Bearer " + token))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void deleteUserDeviceTest() throws Exception {
		UserDevice userDevice = new UserDevice();
		mvc.perform(MockMvcRequestBuilders.delete("/api/device/user")
						.content(objectMapper.writeValueAsString(userDevice))
						.header("Content-type", "application/json")
						.header("Authorization", "Bearer " + token))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void getDevicesTest() throws Exception {
		List<Device> devices = List.of(new Device());
		Integer company = 0;
		Integer type = 0;

		when(deviceService.getDevices(company, type)).thenReturn(devices);
		mvc.perform(MockMvcRequestBuilders.get("/api/device/all?company=" + company + "&type=" + type))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(devices)));
	}
}