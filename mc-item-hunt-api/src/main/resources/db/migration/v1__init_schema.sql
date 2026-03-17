CREATE TABLE item_hunt_runs (
    run_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_uuid CHAR(36) NOT NULL,
    player_username VARCHAR(255) NOT NULL,
    world_seed VARCHAR(255) NOT NULL,
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE item_hunt_run_events (
    event_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    run_id BIGINT NOT NULL,
    event_timestamp BIGINT NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    event_data JSON NOT NULL,

    CONSTRAINT fk_run_id
        FOREIGN KEY (run_id)
        REFERENCES item_hunt_runs(run_id)
        ON DELETE CASCADE,

    INDEX idx_run_lookup (run_id)
);