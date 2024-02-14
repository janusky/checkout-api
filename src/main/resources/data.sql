INSERT INTO roles(name) VALUES('ROLE_GUEST');
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

INSERT INTO users(email, username, password, enabled, created_at, updated_at) 
VALUES 	('admin@server.com', 'admin', '$2a$10$cJX9sqntnx6RFRfwF.FAjOmPq60Zni32LnJF1BWsJyHeRPZW1FNsy', true, NOW(), NOW()),
		('user@server.com', 'user', '$2a$10$VvErvjP6Wr7M5RQnHBQ0T.VTCGemQENQXqjhjRNLMHHXjAPRvn6MW', true, NOW(), NOW());

/*Rol Admin para el usuario `admin`*/
INSERT INTO user_roles (user_id, role_id) VALUES (1, 3);

/*Rol User para el usuario `user`*/
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);