package org.devices.server.device.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devices.server.device.company.Company;
import org.devices.server.device.type.Type;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device")
public class Device {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;

	@ManyToOne
	@JoinColumn(name = "company")
	private Company company;

	@ManyToOne
	@JoinColumn(name = "type")
	private Type type;

	@OneToMany(mappedBy = "device")
	@JsonIgnore
	private List<UserDevice> userDevices;

	private double power;
}
