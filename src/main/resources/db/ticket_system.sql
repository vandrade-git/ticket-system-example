CREATE TYPE ticket_system_issue_status AS ENUM ('NEW', 'FEEDBACK', 'CONFIRMED', 'ASSIGNED', 'RESOLVED', 'CLOSED');
CREATE TYPE ticket_system_issue_priority AS ENUM ('NONE', 'LOW', 'NORMAL', 'HIGH', 'URGENT', 'IMMEDIATE');

-- primary_key_null_handler()
CREATE OR REPLACE FUNCTION primary_key_null_handler() RETURNS TRIGGER AS
$func$
BEGIN
  IF NEW.id IS NULL THEN
    NEW.id := nextval(pg_get_serial_sequence(tg_table_name, 'id'));
  END IF;
  RETURN NEW;
END
$func$ LANGUAGE plpgsql;

-- timestamp_handler()
CREATE OR REPLACE FUNCTION timestamp_handler() RETURNS TRIGGER AS
$func$
BEGIN
  NEW.created_at := OLD.created_at;
  NEW.updated_at := now();
  RETURN NEW;
END
$func$ LANGUAGE plpgsql;

-- close_issue_handler()
CREATE OR REPLACE FUNCTION close_issue_handler() RETURNS TRIGGER AS
$func$
BEGIN
  IF NEW.status::text ILIKE 'CLOSED' THEN
    NEW.closed_at := now();
  END IF;
  RETURN NEW;
END
$func$ LANGUAGE plpgsql;

-- ticket_system_issue
CREATE TABLE IF NOT EXISTS ticket_system_issue(
  id bigserial PRIMARY KEY,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  author_id bigint NOT NULL REFERENCES "user" (id),
  title TEXT NOT NULL,
  browser_info TEXT NOT NULL,
  screenshot TEXT NOT NULL,
  page_url TEXT NOT NULL,
  description TEXT NOT NULL,
  status ticket_system_issue_status NOT NULL DEFAULT 'NEW',
  priority ticket_system_issue_priority NOT NULL DEFAULT 'LOW',
  assigned_to bigint REFERENCES "user" (id),
  closed_at timestamptz DEFAULT now(),
  closed_by bigint REFERENCES "user" (id)
);

CREATE INDEX ticket_system_issue_multicolumn_index ON ticket_system_issue (assigned_to, status, priority);

CREATE TRIGGER ticket_system_issue_primary_key_null_handler
  BEFORE INSERT ON ticket_system_issue
  FOR EACH ROW
EXECUTE PROCEDURE primary_key_null_handler();

CREATE TRIGGER ticket_system_issue_timestamp_trigger
  BEFORE UPDATE ON ticket_system_issue
  FOR EACH ROW
EXECUTE PROCEDURE timestamp_handler();

CREATE TRIGGER ticket_system_issue_close_issue_trigger
  BEFORE UPDATE ON ticket_system_issue
  FOR EACH ROW
EXECUTE PROCEDURE close_issue_handler();

-- ticket_system_note
CREATE TABLE IF NOT EXISTS ticket_system_note(
  id bigserial PRIMARY KEY,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  author_id bigint NOT NULL REFERENCES "user" (id),
  issue_id bigint NOT NULL REFERENCES ticket_system_issue (id),
  body TEXT NOT NULL
);

CREATE TRIGGER ticket_system_note_primary_key_null_handler
  BEFORE INSERT ON ticket_system_note
  FOR EACH ROW
EXECUTE PROCEDURE primary_key_null_handler();

CREATE TRIGGER ticket_system_note_timestamp_trigger
  BEFORE UPDATE ON ticket_system_note
  FOR EACH ROW
EXECUTE PROCEDURE timestamp_handler();