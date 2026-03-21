ALTER TABLE item_hunt_runs ADD COLUMN verification_status VARCHAR(20) DEFAULT 'PENDING' NOT NULL;
UPDATE item_hunt_runs SET verification_status = 'VERIFIED' WHERE verification_status IS NULL;