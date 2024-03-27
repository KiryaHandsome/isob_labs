create table if not exists resources
(
    id   int generated always as identity primary key,
    data varchar(100) not null
);