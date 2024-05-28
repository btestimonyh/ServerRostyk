package org.devices.server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepository
		extends JpaRepository<UserType, Byte> {
	UserType findByName(String name);
}
