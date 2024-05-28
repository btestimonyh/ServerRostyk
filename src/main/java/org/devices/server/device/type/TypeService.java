package org.devices.server.device.type;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeService {
	private final TypeRepository typeRepository;

	public List<Type> getAll() {
		return typeRepository.findAll();
	}
}
