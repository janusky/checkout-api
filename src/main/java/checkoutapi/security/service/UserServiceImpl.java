package checkoutapi.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import checkoutapi.entity.User;
import checkoutapi.security.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;

	@Transactional
	public User save(User userEdit) {
		User person = userEdit;

		// Se busca si se trata de una modificación
		if (userEdit.getId() != null) {
			person = userRepository.findById(userEdit.getId()).orElseGet(() -> userEdit);

			// Merge change
			person.setEmail(userEdit.getEmail());
			person.setEnabled(person.getEnabled());
			if (userEdit.getRoles() != null && !userEdit.getRoles().isEmpty()) {
				person.setRoles(userEdit.getRoles());
			}
		}

		// Si hay password
		if (StringUtils.hasText(userEdit.getPassword())) {
			person.setPassword(passwordEncoder.encode(userEdit.getPassword()));
		}

		User uSave = userRepository.save(person);

		return uSave;
	}
}
