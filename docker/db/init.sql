-- Initial schema for javaToDo (applied on first container start)

CREATE TABLE IF NOT EXISTS works (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    assignee    VARCHAR(100),
    status      VARCHAR(50) NOT NULL DEFAULT 'NEW',
    due_date    DATE,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);
