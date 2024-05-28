package org.devices.server.device.company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository
	extends JpaRepository<Company, Integer> {
}
