CREATE TABLE workflow_status_group (
    id                      INT AUTO_INCREMENT,
    user_id                 INT NOT NULL DEFAULT 0,
    workflow_group_id       INT NOT NULL,
    workflow_id             INT NOT NULL,
    running_status          INT NOT NULL DEFAULT 0,
    created_at              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_idx_workflow_engine (workflow_id, workflow_group_id)
);

ALTER TABLE workflow_engine_group drop running_status;

