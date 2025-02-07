CREATE TABLE "venues" (
  "id" int PRIMARY KEY,
  "name" varchar(100) NOT NULL,
  "location" varchar(200) NOT NULL,
  "capacity" int NOT NULL
);

CREATE TABLE "genre" (
  "id" int PRIMARY KEY,
  "name" varchar(50) NOT NULL
);

CREATE TABLE "artists" (
  "id" int PRIMARY KEY,
  "name" varchar(100) NOT NULL,
  "genre_id" int
);

CREATE TABLE "concerts" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "name" varchar(50) NOT NULL,
  "artist_id" int,
  "date" timestamp NOT NULL,
  "venue_id" int,
  created_at timestamp DEFAULT current_timestamp,
  updated_at timestamp DEFAULT current_timestamp
);

CREATE TABLE "tickets" (
  "id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "concert_id" int,
  "price" int NOT NULL,
  "purchase_date" timestamp NOT NULL,
  "stock" int NOT NULL,
  created_at timestamp DEFAULT current_timestamp,
  updated_at timestamp DEFAULT current_timestamp
);

CREATE TABLE "orders" (
  "id" uuid PRIMARY KEY,
  "user_id" uuid,
  "ticket_id" int,
  "status" varchar(20) DEFAULT 'pending',
  created_at timestamp DEFAULT current_timestamp,
  updated_at timestamp DEFAULT current_timestamp
);

ALTER TABLE "artists" ADD FOREIGN KEY ("genre_id") REFERENCES "genre" ("id");

ALTER TABLE "concerts" ADD FOREIGN KEY ("artist_id") REFERENCES "artists" ("id");

ALTER TABLE "concerts" ADD FOREIGN KEY ("venue_id") REFERENCES "venues" ("id");

ALTER TABLE "tickets" ADD FOREIGN KEY ("concert_id") REFERENCES "concerts" ("id");

ALTER TABLE "orders" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "orders" ADD FOREIGN KEY ("ticket_id") REFERENCES "tickets" ("id");
