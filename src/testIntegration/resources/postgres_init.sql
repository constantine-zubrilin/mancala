CREATE SCHEMA IF NOT EXISTS mankala;
ALTER ROLE postgres SET search_path = mankala, "$user";
