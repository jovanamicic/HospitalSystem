-- MEDICAL STAFF
INSERT INTO `hospital_v1`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `photo`) VALUES ('MEDICAL_STAFF', '1', 'jovana@gmail.com', 'Jovana', 'jo', '1111', 'Micic', 'jo', 'static/images/medicalStuff/jovana.png');
INSERT INTO `hospital_v1`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `photo`) VALUES ('MEDICAL_STAFF', '2', 'katarina@gmail.com', 'Katarina', 'keti', '2222', 'Preradov', 'keti', 'static/images/medicalStuff/katarina.jpg');
INSERT INTO `hospital_v1`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `photo`) VALUES ('MEDICAL_STAFF', '3', 'mina@gmail.com', 'Mina', 'mina', '3333', 'Medic', 'mina', 'static/images/medicalStuff/mina.jpg');
UPDATE `hospital_v1`.`person` SET `education`='doctor' WHERE `person_id`='1';
UPDATE `hospital_v1`.`person` SET `education`='doctor' WHERE `person_id`='2';
UPDATE `hospital_v1`.`person` SET `education`='doctor' WHERE `person_id`='3';
UPDATE `hospital_v1`.`person` SET `chosen_doctor`='1' WHERE `person_id`='4';
UPDATE `hospital_v1`.`person` SET `chosen_doctor`='1' WHERE `person_id`='5';
UPDATE `hospital_v1`.`person` SET `chosen_doctor`='2' WHERE `person_id`='6';


-- PATIENT
INSERT INTO `hospital_v1`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `gender`) VALUES ('PATIENT', '4', 'pera@gmail.com', 'Pera', 'pera', '4444', 'Peric', 'pera', 'muško');
INSERT INTO `hospital_v1`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `gender`) VALUES ('PATIENT', '5', 'mika@gmail.com', 'Mika', 'mika', '5555', 'Mikic', 'mika', 'muško');
INSERT INTO `hospital_v1`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `gender`) VALUES ('PATIENT', '6', 'djole@gmail.com', 'Djole', 'djole', '6666', 'Djolic', 'djole', 'muško');
UPDATE `hospital_v1`.`person` SET `chosen_doctor`='1' WHERE `person_id`='4';
UPDATE `hospital_v1`.`person` SET `chosen_doctor`='1' WHERE `person_id`='5';
UPDATE `hospital_v1`.`person` SET `chosen_doctor`='2' WHERE `person_id`='6';

-- MANAGER
INSERT INTO `hospital_v1`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `role`) VALUES ('MANAGER', '7', 'marko@gmail.com', 'Marko', 'marko', '7777', 'Markovic', 'marko', 'general');
INSERT INTO `hospital_v1`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `role`) VALUES ('MANAGER', '8', 'vlada@gmail.com', 'Vlada', 'vlada', '8888', 'Vladic', 'vlada', 'general');
INSERT INTO `hospital_v1`.`person` (`discriminator`, `person_id`, `email`, `name`, `password`, `personalid`, `surname`, `username`, `role`) VALUES ('MANAGER', '9', 'vanja@gmail.com', 'Vanja', 'vanja', '9999', 'Vanjic', 'vanja', 'financial');


-- ADDRESS
INSERT INTO `hospital_v1`.`address` (`address_id`, `city`, `country`, `number`, `street`, `zip_code`) VALUES ('1', 'Novi Sad', 'Serbia', '10', 'Bulevar Mihajla Pupina', '21000');


-- ROLE
INSERT INTO `hospital_v1`.`role` (`id`, `name`) VALUES (1, 'DOCTOR');
INSERT INTO `hospital_v1`.`role` (`id`, `name`) VALUES (2, 'MEDICAL_STUFF');
INSERT INTO `hospital_v1`.`role` (`id`, `name`) VALUES (3, 'GENERAL_MANAGER');
INSERT INTO `hospital_v1`.`role` (`id`, `name`) VALUES (4, 'FINANCE_MANAGER');
INSERT INTO `hospital_v1`.`role` (`id`, `name`) VALUES (5, 'PATIENT');
INSERT INTO `hospital_v1`.`role` (`id`, `name`) VALUES (6, 'GOVERNMENT');

-- PERMISSION
INSERT INTO `hospital_v1`.`permission` (`id`, `name`) VALUES (1, 'Ovde nesto pisemo');

-- ROLE MEMBER
INSERT INTO `hospital_v1`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (1, 1, 1);
INSERT INTO `hospital_v1`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (2, 2, 1);
INSERT INTO `hospital_v1`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (3, 3, 1);

INSERT INTO `hospital_v1`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (4, 1, 2);
INSERT INTO `hospital_v1`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (5, 2, 2);
INSERT INTO `hospital_v1`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (6, 3, 2);

INSERT INTO `hospital_v1`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (7, 4, 5);
INSERT INTO `hospital_v1`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (8, 5, 5);
INSERT INTO `hospital_v1`.`role_member` (`id`, `person_person_id`, `role_id`) VALUES (9, 6, 5);

-- PAYMENTS
INSERT INTO `hospital_v1`.`payment` (`payment_id`, `account`, `amount`, `currency`, `date`, `recipient`, `manager_id`) VALUES ('1', '54488-888-88888', '50000', 'rsd', '2017-05-18 12:37:22', 'Jovana Micic', '9');
INSERT INTO `hospital_v1`.`payment` (`payment_id`, `account`, `amount`, `currency`, `date`, `recipient`, `manager_id`) VALUES ('2', '8989-8989-89898', '77777', 'RSD', '2017-05-18 13:01:30', 'Buci buci', '9');
INSERT INTO `hospital_v1`.`payment` (`payment_id`, `account`, `amount`, `currency`, `date`, `recipient`, `manager_id`) VALUES ('3', '878788778', '44444444', 'RSD', '2017-05-18 13:06:07', 'Katarina Preradov', '9');


-- ROOMS
INSERT INTO `hospital_v1`.`room` (`room_id`, `name`) VALUES ('1', 'Sala 1');
INSERT INTO `hospital_v1`.`room` (`room_id`, `name`) VALUES ('2', 'Sala 2');
INSERT INTO `hospital_v1`.`room` (`room_id`, `name`) VALUES ('3', 'Sala 3');
INSERT INTO `hospital_v1`.`room` (`room_id`, `name`) VALUES ('4', 'Sala 4');

-- RECORDS
INSERT INTO `hospital_v1`.`record` (`record_id`) VALUES ('4444');
INSERT INTO `hospital_v1`.`record` (`record_id`) VALUES ('5555');
INSERT INTO `hospital_v1`.`record` (`record_id`) VALUES ('6666');


-- OPERATIONS
INSERT INTO `hospital_v1`.`operation` (`operation_id`, `date`, `duration`, `name`, `head_doctor_id`, `record_id`) VALUES ('1', '2017-05-20', '2', 'Operacija nosa', '1', '4444');
INSERT INTO `hospital_v1`.`operation` (`operation_id`, `date`, `duration`, `name`, `head_doctor_id`, `record_id`) VALUES ('2', '2017-05-21', '4', 'Operacija nosa', '1', '4444');
INSERT INTO `hospital_v1`.`operation` (`operation_id`, `date`, `duration`, `name`, `head_doctor_id`, `record_id`) VALUES ('3', '2017-05-21', '1', 'Operacija sinusa', '1', '4444');
INSERT INTO `hospital_v1`.`operation` (`operation_id`, `date`, `duration`, `name`, `head_doctor_id`, `record_id`) VALUES ('4', '2017-05-21', '5', 'Operacija kicme', '2', '4444');

-- EXAMINATIONS
INSERT INTO `hospital_v1`.`examination` (`examination_id`, `date`, `name`, `new`, `passed`, `doctor_id`, `record_id`) VALUES ('1', '2017-01-07', 'EKG', TRUE, FALSE, '1', '4444');
INSERT INTO `hospital_v1`.`examination` (`examination_id`, `date`, `name`, `new`, `passed`, `doctor_id`, `record_id`) VALUES ('2', '2017-05-07', 'Hitan pregled', TRUE, FALSE, '1', '4444');
INSERT INTO `hospital_v1`.`examination` (`examination_id`, `date`, `name`, `new`, `passed`, `doctor_id`, `record_id`) VALUES ('3', '2017-05-07 00:00:00', 'EEG', FALSE, FALSE, '1', '4444');
