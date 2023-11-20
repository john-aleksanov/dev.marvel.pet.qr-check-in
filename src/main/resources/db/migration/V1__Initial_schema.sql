CREATE TABLE activities
(
    id            UUID PRIMARY KEY,
    activity_name VARCHAR(255)
);

CREATE TABLE classrooms
(
    id             UUID PRIMARY KEY,
    classroom_name VARCHAR(255)
);

CREATE TABLE scheduled_activities
(
    id           UUID PRIMARY KEY,
    activity_id  UUID,
    classroom_id UUID,
    start_time   TIMESTAMP,
    end_time     TIMESTAMP,
    FOREIGN KEY (activity_id) REFERENCES activities (id),
    FOREIGN KEY (classroom_id) REFERENCES classrooms (id)
);