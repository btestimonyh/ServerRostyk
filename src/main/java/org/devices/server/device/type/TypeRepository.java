package org.devices.server.device.type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository
		extends JpaRepository<Type, Integer> {
}
