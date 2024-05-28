package org.devices.server.device.type;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/type")
@RequiredArgsConstructor
public class TypeController {
	private final TypeService typeService;

	@GetMapping("/all")
	public List<Type> getAll() {
		return typeService.getAll();
	}
}
