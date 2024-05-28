package org.devices.server.device;

import org.devices.server.device.data.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository
	extends JpaRepository<Device, Integer> {
}
