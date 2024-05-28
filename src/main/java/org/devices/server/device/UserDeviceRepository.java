package org.devices.server.device;

import org.devices.server.device.data.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDeviceRepository
	extends JpaRepository<UserDevice, Integer> {
	List<UserDevice> findByUserId(int userId);
}
