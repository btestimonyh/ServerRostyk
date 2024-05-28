package org.devices.server.device.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devices.server.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_device")
public class UserDevice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "device")
	private Device device;

	@ManyToOne
	@JoinColumn(name = "\"user\"")
	@JsonIgnore
	private User user;
	private boolean used;
}
