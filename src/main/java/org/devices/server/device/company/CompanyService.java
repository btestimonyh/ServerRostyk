package org.devices.server.device.company;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
	private final CompanyRepository companyRepository;

	public List<Company> getAll() {
		return companyRepository.findAll();
	}
}
