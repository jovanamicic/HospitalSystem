-- ADDRESS
INSERT INTO `hospital`.`address` (`address_id`, `city`, `country`, `number`, `street`, `zip_code`) VALUES ('1', 'Novi Sad', 'Srbija', '10', 'Bulevar Mihajla Pupina', '21000');
INSERT INTO `hospital`.`address` (`address_id`, `city`, `country`, `number`, `street`, `zip_code`) VALUES ('2', 'Novi Sad', 'Srbija', '6', 'Gagarinova', '21000');

-- MEDICAL STAFF
INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `photo`, `education`) VALUES ('MEDICAL_STAFF', '1', 'jovana@gmail.com', 'Jovana', '$2a$06$uhV/Qt/kORxGuww7S9.vDORMtizMMTKy7NPFK7vwwhJE35I9meEMa', '1010199481012', 'Mićić', 'jo', 'images/medicalStuff/jovana.png', 'Doktor');
INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `photo`, `education`) VALUES ('MEDICAL_STAFF', '2', 'katarina@gmail.com', 'Katarina', '$2a$06$YMGISkfYmwgOHzS7raR7GupVj7yFoIaUoZDkcL8qurV3CFr4KnS4K', '1111199481012', 'Preradov', 'keti', 'images/medicalStuff/katarina.jpg', 'Doktor');
INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `photo`, `education`) VALUES ('MEDICAL_STAFF', '3', 'mina@gmail.com', 'Mina', '$2a$06$6CekuMHG/UTa3UHLrWfj4O28Yu5cot2AS3slQsh4ZLii628zopTdm', '1212199481012', 'Medić', 'mina', 'images/medicalStuff/mina.jpg', 'Doktor');

-- PATIENT
-- INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `gender`, `chosen_doctor`, `address`, `birthday`) VALUES ('PATIENT', '4', 'pera@gmail.com', 'Petar', '$2a$10$UPGHz1h0ljOL3TiO1n.ZOeTK.1gy89qRdTajENsI5XEpuIg7FqK1y', '4444', 'Perić', 'pera', 'Muško', 1, 1, '1954-02-02');
-- INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `gender`, `chosen_doctor`, `birthday`) VALUES ('PATIENT', '5', 'mika@gmail.com', 'Mika', '$2a$10$4iu9rQqKvmQpyi1PDxW6BesO.NDx9aDXqgM4UvzP8GpRNE3cCUwby', '0303197181012', 'Mikić', 'mika', 'Muško', 1, '1971-03-03');
-- INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `gender`, `chosen_doctor`, `address`, `birthday`) VALUES ('PATIENT', '6', 'djole@gmail.com', 'Djole', '$2a$06$duWUXdaerEURz0U9APos9u.JXcz38woPArt35bG8k6EL7aJfKkSCG', '0404193781012', 'Đolić', 'djole', 'Muško', 2, 2, '1937-04-04');

-- MANAGER
INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `role`) VALUES ('MANAGER', '7', 'marko@gmail.com', 'Marko', '$2a$06$G7pro00k5GkmtzXvIW8TCO6Cd5KHpu42F8E9zYSzynRUg2hdzLw0S', '0232195481012', 'Marković', 'marko', 'general');
INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `role`) VALUES ('MANAGER', '8', 'vlada@gmail.com', 'Vlada', '$2a$06$tqS5a7jM/6hsOJx2LzeUWOMJMuOKpN2K24rArCO8krcV6gli2rqDu', '0512198481012', 'Vladić', 'vlada', 'general');
INSERT INTO `hospital`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `role`) VALUES ('MANAGER', '9', 'vanja@gmail.com', 'Vanja', '$2a$06$OglHQiitE.Qg.RcMbf713eyPoGbO2HF1y8a27iM1yU1kETdPXn9My', '2202195981012', 'Vanjić', 'vanja', 'financial');


-- ROLE
INSERT INTO `hospital`.`role` (`id`, `name`) VALUES (1, 'ROLE_MEDICAL_STAFF');
INSERT INTO `hospital`.`role` (`id`, `name`) VALUES (2, 'ROLE_GENERAL_MANAGER');
INSERT INTO `hospital`.`role` (`id`, `name`) VALUES (3, 'ROLE_FINANCE_MANAGER');
INSERT INTO `hospital`.`role` (`id`, `name`) VALUES (4, 'ROLE_PATIENT');
INSERT INTO `hospital`.`role` (`id`, `name`) VALUES (5, 'ROLE_GOVERNMENT');


-- ROLE MEMBER
INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (1, 1, 1);
INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (2, 2, 1);
INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (3, 3, 1);

-- INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (4, 4, 4);
-- INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (5, 5, 4);
-- INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (6, 6, 4);

INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (7, 7, 2);
INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (8, 8, 2);
INSERT INTO `hospital`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (9, 9, 3);


-- PERMISSION
-- manager
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (1, 'Schedule_rooms');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (2, 'View_manager_profile');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (3, 'Edit_manager_profile');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (4, 'Edit_manager_password');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (5, 'View_all_payments');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (6, 'Submit_payment');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (7, 'View_all_operations');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (8, 'View_all_examinations');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (9, 'Edit_operation');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (10, 'Edit_examination');

-- medical staff
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (11, 'Search_patients');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (12, 'View_medical_staff_schedule');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (13, 'Add_new_patient');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (14, 'View_all_patients');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (15, 'Add_new_operation');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (16, 'Add_new_examination');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (17, 'Delete_operation');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (18, 'Delete_examination');

-- patients
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (19, 'View_patient_profile');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (20, 'Edit_patient_profile');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (21, 'Edit_patient_password');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (22, 'View_patient_record');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (23, 'View_operation');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (24, 'View_examination');
INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (25, 'View_patient_schedule');

INSERT INTO `hospital`.`permission` (`id`, `name`) VALUES (26, 'View_all_medical_staff');


-- ROLE PERMISSION

-- financial manager
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (1, 2, 3);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (2, 3, 3);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (3, 4, 3);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (4, 5, 3);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (5, 6, 3);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (38, 26, 3);
-- general manager
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (6, 1, 2);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (7, 2, 2);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (8, 3, 2);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (9, 4, 2);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (10, 7, 2);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (11, 8, 2);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (12, 9, 2);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (13, 10, 2);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (36, 23, 2);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (37, 24, 2);

-- patient
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (14, 19, 4);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (15, 20, 4);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (16, 21, 4);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (17, 22, 4);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (18, 23, 4);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (19, 24, 4);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (20, 25, 4);

-- medical staff
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (21, 11, 1);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (22, 12, 1);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (23, 13, 1);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (24, 14, 1);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (25, 15, 1);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (26, 16, 1);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (27, 17, 1);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (28, 18, 1);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (29, 19, 1);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (30, 22, 1);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (31, 23, 1);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (32, 24, 1);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (33, 7, 1);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (34, 8, 1);
INSERT INTO `hospital`.`role_permission` (`id`, `permission_id`, `role_id`) VALUES (35, 26, 1);



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
-- INSERT INTO `hospital`.`record` (`record_id`) VALUES ('4444');
-- INSERT INTO `hospital`.`record` (`record_id`) VALUES ('0303197181012');
-- INSERT INTO `hospital`.`record` (`record_id`) VALUES ('0404193781012');

-- OPERATIONS
-- INSERT INTO `hospital`.`operation` (`operation_id`, `date`, `duration`, `name`, `head_doctor_id`, `record_id`) VALUES ('1', '2017-05-20', '2', 'Operacija nosa', '1', '4444');
-- INSERT INTO `hospital`.`operation` (`operation_id`, `date`, `duration`, `name`, `head_doctor_id`, `record_id`) VALUES ('2', '2017-05-21', '4', 'Operacija nosa', '1', '4444');
-- INSERT INTO `hospital`.`operation` (`operation_id`, `date`, `duration`, `name`, `head_doctor_id`, `record_id`) VALUES ('3', '2017-05-21', '1', 'Operacija sinusa', '1', '4444');
-- INSERT INTO `hospital`.`operation` (`operation_id`, `date`, `duration`, `name`, `head_doctor_id`, `record_id`) VALUES ('4', '2017-05-21', '5', 'Operacija kicme', '2', '4444');

-- EXAMINATIONS
-- INSERT INTO `hospital`.`examination` (`examination_id`, `date`, `name`, `new`, `passed`, `doctor_id`, `record_id`) VALUES ('1', '2017-01-07', 'EKG', TRUE, FALSE, '1', '4444');
-- INSERT INTO `hospital`.`examination` (`examination_id`, `date`, `name`, `new`, `passed`, `doctor_id`, `record_id`) VALUES ('2', '2017-05-07', 'Hitan pregled', TRUE, FALSE, '1', '4444');
-- INSERT INTO `hospital`.`examination` (`examination_id`, `date`, `name`, `new`, `passed`, `doctor_id`, `record_id`) VALUES ('3', '2017-05-07 00:00:00', 'EEG', FALSE, FALSE, '1', '4444');
