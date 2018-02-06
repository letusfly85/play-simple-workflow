CREATE TABLE workflow_status (
    id                      INT AUTO_INCREMENT,
    workflow_id             INT,
    workflow_step_id        INT,
    user_id                 INT NOT NULL DEFAULT 0,
    is_executed             BOOLEAN NOT NULL DEFAULT FALSE,
    created_at              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_idx_workflow_engine (workflow_id, workflow_step_id)
);