CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name varchar(100) NOT NULL,
  email varchar(100) UNIQUE NOT NULL,
  password varchar(155) NOT NULL,
  created_at timestamp DEFAULT current_timestamp,
  updated_at timestamp DEFAULT current_timestamp
);
