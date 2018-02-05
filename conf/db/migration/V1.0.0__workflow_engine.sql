CREATE TABLE workflow_engine (
    id                      INT AUTO_INCREMENT,
    workflow_id             INT,
    workflow_name           VARCHAR(300),
    workflow_description    VARCHAR(300),
    path                    VARCHAR(300),
    method                  VARCHAR(300),
    enable_role             VARCHAR(300),
    workflow_step_id        INT,
    workflow_step_next_id   INT,
    condition_for_next_step VARCHAR(300),
    is_first_step           BOOLEAN,
    is_last_step            BOOLEAN,
    created_at              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_idx_workflow_engine (workflow_id, workflow_step_id)
);