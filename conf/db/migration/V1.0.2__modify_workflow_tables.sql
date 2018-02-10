ALTER TABLE workflow_engine drop method;

CREATE TABLE workflow_engine_group (
    id                      INT AUTO_INCREMENT,
    workflow_group_id       INT NOT NULL,
    workflow_id             INT NOT NULL,
    running_status          INT NOT NULL DEFAULT 0,
    before_workflow_id      INT,
    condition_to_start      VARCHAR(300),
    created_at              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_idx_workflow_engine (workflow_id, workflow_group_id)
);
