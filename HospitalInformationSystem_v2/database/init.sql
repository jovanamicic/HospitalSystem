-- ADDRESS
INSERT INTO `hospital`.`address` (`address_id`, `city`, `country`, `number`, `street`, `zip_code`) VALUES ('1', 'Novi Sad', 'Srbija', '10', 'Bulevar Mihajla Pupina', '21000');
INSERT INTO `hospital`.`address` (`address_id`, `city`, `country`, `number`, `street`, `zip_code`) VALUES ('2', 'Novi Sad', 'Srbija', '6', 'Gagarinova', '21000');

-- MEDICAL STAFF
INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `photo`, `education`) VALUES ('MEDICAL_STAFF', '1', 'jovana@gmail.com', 'Jovana', '$2a$06$uhV/Qt/kORxGuww7S9.vDORMtizMMTKy7NPFK7vwwhJE35I9meEMa', '1010199481012', 'Mićić', 'jo', 'images/medicalStuff/jovana.png', 'Doktor');
INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `photo`, `education`) VALUES ('MEDICAL_STAFF', '2', 'katarina@gmail.com', 'Katarina', '$2a$06$YMGISkfYmwgOHzS7raR7GupVj7yFoIaUoZDkcL8qurV3CFr4KnS4K', '1111199481012', 'Preradov', 'keti', 'images/medicalStuff/katarina.jpg', 'Doktor');
INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `photo`, `education`) VALUES ('MEDICAL_STAFF', '3', 'mina@gmail.com', 'Mina', '$2a$06$6CekuMHG/UTa3UHLrWfj4O28Yu5cot2AS3slQsh4ZLii628zopTdm', '1212199481012', 'Medić', 'mina', 'images/medicalStuff/mina.jpg', 'Doktor');

-- PATIENT
INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `gender`, `chosen_doctor`, `address`, `birthday`) VALUES ('PATIENT', '4', 'pera@gmail.com', 'Petar', '$2a$10$UPGHz1h0ljOL3TiO1n.ZOeTK.1gy89qRdTajENsI5XEpuIg7FqK1y', '0202195481012', 'Perić', 'pera', 'Muško', 1, 1, '1954-02-02');
INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `gender`, `chosen_doctor`, `birthday`) VALUES ('PATIENT', '5', 'mika@gmail.com', 'Mika', '$2a$10$4iu9rQqKvmQpyi1PDxW6BesO.NDx9aDXqgM4UvzP8GpRNE3cCUwby', '0303197181012', 'Mikić', 'mika', 'Muško', 1, '1971-03-03');
INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `gender`, `chosen_doctor`, `address`, `birthday`) VALUES ('PATIENT', '6', 'djole@gmail.com', 'Djole', '$2a$06$duWUXdaerEURz0U9APos9u.JXcz38woPArt35bG8k6EL7aJfKkSCG', '0404193781012', 'Đolić', 'djole', 'Muško', 2, 2, '1937-04-04');

-- MANAGER
INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `role`) VALUES ('MANAGER', '7', 'marko@gmail.com', 'Marko', '$2a$06$G7pro00k5GkmtzXvIW8TCO6Cd5KHpu42F8E9zYSzynRUg2hdzLw0S', '0232195481012', 'Marković', 'marko', 'general');
INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `role`) VALUES ('MANAGER', '8', 'vlada@gmail.com', 'Vlada', '$2a$06$tqS5a7jM/6hsOJx2LzeUWOMJMuOKpN2K24rArCO8krcV6gli2rqDu', '0512198481012', 'Vladić', 'vlada', 'general');
INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `role`) VALUES ('MANAGER', '9', 'vanja@gmail.com', 'Vanja', '$2a$06$OglHQiitE.Qg.RcMbf713eyPoGbO2HF1y8a27iM1yU1kETdPXn9My', '2202195981012', 'Vanjić', 'vanja', 'financial');


-- ROLE
INSERT INTO `hospital`.`role` (`id`, `name`) VALUES (1, 'MEDICAL_STUFF');
INSERT INTO `hospital`.`role` (`id`, `name`) VALUES (2, 'GENERAL_MANAGER');
INSERT INTO `hospital`.`role` (`id`, `name`) VALUES (3, 'FINANCE_MANAGER');
INSERT INTO `hospital`.`role` (`id`, `name`) VALUES (4, 'PATIENT');
INSERT INTO `hospital`.`role` (`id`, `name`) VALUES (5, 'GOVERNMENT');

-- PERMISSION
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (1, 'View_patient_profile');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (2, 'View_patient_schedule');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (3, 'Edit_patient_profile');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (4, 'Edit_patient_password');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (5, 'View_patient_schedule');

INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (6, 'View_medical_staff_schedule');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (7, 'Add_new_patient');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (8, 'View_all_patients');

INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (9, 'View_manager_profile');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (10, 'Edit_manager_profile');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (11, 'Edit_manager_password');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (12, 'View_all_payments');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (13, 'Submit_payment');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (14, 'View_all_operations');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (15, 'View_all_examinations');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (16, 'Edit_operation');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (17, 'Edit_examination');

-- ROLE MEMBER
INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (1, 1, 1);
INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (2, 2, 1);
INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (3, 3, 1);

INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (4, 4, 4);
INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (5, 5, 4);
INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (6, 6, 4);

INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (7, 7, 2);
INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (8, 8, 2);
INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (9, 9, 3);



-- PAYMENTS
INSERT INTO `hospital`.`payment` (`payment_id`, `account`, `amount`, `currency`, `date`, `recipient`, `manager_id`) VALUES ('1', '54488-888-88888', '50000', 'rsd', '2017-05-18 12:37:22', 'Jovana Micic', '9');
INSERT INTO `hospital`.`payment` (`payment_id`, `account`, `amount`, `currency`, `date`, `recipient`, `manager_id`) VALUES ('2', '8989-8989-89898', '77777', 'RSD', '2017-05-18 13:01:30', 'Buci buci', '9');
INSERT INTO `hospital`.`payment` (`payment_id`, `account`, `amount`, `currency`, `date`, `recipient`, `manager_id`) VALUES ('3', '878788778', '44444444', 'RSD', '2017-05-18 13:06:07', 'Katarina Preradov', '9');


-- ROOMS
INSERT INTO `hospital`.`room` (`room_id`, `name`) VALUES ('1', 'Sala 1');
INSERT INTO `hospital`.`room` (`room_id`, `name`) VALUES ('2', 'Sala 2');
INSERT INTO `hospital`.`room` (`room_id`, `name`) VALUES ('3', 'Sala 3');
INSERT INTO `hospital`.`room` (`room_id`, `name`) VALUES ('4', 'Sala 4');

-- RECORDS
INSERT INTO `hospital`.`record` (`record_id`) VALUES ('4444');

-- OPERATIONS
INSERT INTO `hospital`.`operation` (`operation_id`, `date`, `duration`, `name`, `head_doctor_id`, `record_id`) VALUES ('1', '2017-05-20', '2', 'Operacija nosa', '1', '4444');
INSERT INTO `hospital`.`operation` (`operation_id`, `date`, `duration`, `name`, `head_doctor_id`, `record_id`) VALUES ('2', '2017-05-21', '4', 'Operacija nosa', '1', '4444');
INSERT INTO `hospital`.`operation` (`operation_id`, `date`, `duration`, `name`, `head_doctor_id`, `record_id`) VALUES ('3', '2017-05-21', '1', 'Operacija sinusa', '1', '4444');
INSERT INTO `hospital`.`operation` (`operation_id`, `date`, `duration`, `name`, `head_doctor_id`, `record_id`) VALUES ('4', '2017-05-21', '5', 'Operacija kicme', '2', '4444');

-- EXAMINATIONS
INSERT INTO `hospital`.`examination` (`examination_id`, `date`, `name`, `new`, `passed`, `doctor_id`, `record_id`) VALUES ('1', '2017-01-07', 'EKG', TRUE, FALSE, '1', '4444');
INSERT INTO `hospital`.`examination` (`examination_id`, `date`, `name`, `new`, `passed`, `doctor_id`, `record_id`) VALUES ('2', '2017-05-07', 'Hitan pregled', TRUE, FALSE, '1', '4444');
INSERT INTO `hospital`.`examination` (`examination_id`, `date`, `name`, `new`, `passed`, `doctor_id`, `record_id`) VALUES ('3', '2017-05-07 00:00:00', 'EEG', FALSE, FALSE, '1', '4444');
