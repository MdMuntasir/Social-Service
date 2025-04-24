USE social_service_db;

DROP TABLE IF EXISTS job_request;
DROP TABLE IF EXISTS profile;
DROP TABLE IF EXISTS job;
DROP TABLE IF EXISTS user;

CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE profile (
    user_id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    work_field VARCHAR(255) NOT NULL,
    experience TEXT,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE job (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    reward DOUBLE NOT NULL,
    status VARCHAR(50) NOT NULL,
    assigned_freelancer_id BIGINT,
    FOREIGN KEY (assigned_freelancer_id) REFERENCES user(id)
);

CREATE TABLE job_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    freelancer_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (job_id) REFERENCES job(id),
    FOREIGN KEY (freelancer_id) REFERENCES user(id)
);

INSERT INTO user (username, password, role) VALUES
('head', '$2a$12$u8dWhS4Pxm4xW.pZWUjs0.aoB19pbqrtrZ87GZg9JCBBGNaqmmbDW', 'ROLE_HEAD'),
('freelancer1', '$2a$12$u8dWhS4Pxm4xW.pZWUjs0.aoB19pbqrtrZ87GZg9JCBBGNaqmmbDW', 'ROLE_FREELANCER');

INSERT INTO profile (user_id, name, work_field, experience, is_available) VALUES
((SELECT id FROM user WHERE username='freelancer1'), 'John Smith', 'Landscaping', '5 years cutting grass and gardening', TRUE);
