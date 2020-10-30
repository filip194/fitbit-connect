-----------
-- ROLES --
-----------
insert into roles (id, role_id, name)
    values (1, '00000000-0000-0000-0000-000000000011', 'ADMIN');

insert into roles (id, role_id, name)
    values (2, '00000000-0000-0000-0000-000000000012', 'MODERATOR');

insert into roles (id, role_id, name)
    values (3, '00000000-0000-0000-0000-000000000013', 'USER');

-----------
-- USERS --
-----------
-- passwords for all test users are 123 encrypted with Bcrypt --
insert into users (id, user_id, username, password, type, email, name, last_name, age, created, updated)
    values
    (
        1,
        '00000000-0000-0000-0000-000000000001',
        'admin',
        '$2y$12$Q.23eJ7aPlJyviQVVX9WpeSYJ1Nkp7UnqYz30J6CVezbFFUbhLadm',
        'ADMIN',
        'admin@admin.net',
        'admin_first_name',
        'admin_last_name',
        '100',
        '2020-04-09 14:10:19.805',
        '2020-04-09 14:10:19.805'
    );

insert into users (id, user_id, username, password, type, email, name, last_name, age, created, updated)
    values
    (
        2,
        '00000000-0000-0000-0000-000000000002',
        'moderator',
        '$2y$12$GjgRv.loxRJHVdmIsM3O4O8JXDOeDO1q6Et7S0pdaa6VfBYBKBftW',
        'MODERATOR',
        'moderator@moderator.net',
        'moderator_first_name',
        'moderator_last_name',
        '45',
        '2020-04-09 14:10:19.805',
        '2020-04-09 14:10:19.805'
    );

insert into users (id, user_id, username, password, type, email, name, last_name, age, created, updated)
    values
    (
        3,
        '00000000-0000-0000-0000-000000000003',
        'test_user',
        '$2y$12$vnPcJmY26PobD8Lsgc7/iOz0Sx.A4z/ySLSNM2/Hvb1mv0nDWfk0q',
        'USER',
        'user@user.net',
        'user_first_name',
        'user_last_name',
        '30',
        '2020-04-09 14:10:19.805',
        '2020-04-09 14:10:19.805'
    );

insert into users (id, user_id, username, password, type, email, name, last_name, age, created, updated)
    values
    (
        4,
        '00000000-0000-0000-0000-000000000004',
        'test_user1',
        '$2y$12$mTLZdm70e/m2Qm84XWsHuenY1z6velpD3.T4nhe3TrE6xhATkO4na',
        'USER',
        'user1@user.net',
        'user1_first_name',
        'user1_last_name',
        '29',
        '2020-04-09 14:10:19.805',
        '2020-04-09 14:10:19.805'
    );

insert into users (id, user_id, username, password, type, email, name, last_name, age, created, updated)
    values
    (
        5,
        '00000000-0000-0000-0000-000000000005',
        'test_user2',
        '$2y$12$5oRQHX1I3EGeme5AdWW2X.EX.aXvp4mQFh.iQnmOVC71J4OS46Uje',
        'USER',
        'user2@user.net',
        'user2_first_name',
        'user2_last_name',
        '28',
        '2020-04-09 14:10:19.805',
        '2020-04-09 14:10:19.805'
    );

----------------
-- USER_ROLES --
----------------

-- for admin --
insert into user_roles (user_entity_id, role_entity_id) values (1, 1);
insert into user_roles (user_entity_id, role_entity_id) values (1, 2);
insert into user_roles (user_entity_id, role_entity_id) values (1, 3);

-- for moderator --
insert into user_roles (user_entity_id, role_entity_id) values (2, 2);
insert into user_roles (user_entity_id, role_entity_id) values (2, 3);

-- for other users --
insert into user_roles (user_entity_id, role_entity_id) values (3, 3);

insert into user_roles (user_entity_id, role_entity_id) values (4, 3);

insert into user_roles (user_entity_id, role_entity_id) values (5, 3);

--insert into user_roles (user_id, role_id)
--    values ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000011');
--
--insert into user_roles (user_id, role_id)
--    values ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000012');
--
--insert into user_roles (user_id, role_id)
--    values ('00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000013');
--
--insert into user_roles (user_id, role_id)
--    values ('00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000013');
--
--insert into user_roles (user_id, role_id)
--    values ('00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000013');
