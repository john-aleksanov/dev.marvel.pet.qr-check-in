INSERT INTO activities
VALUES ('dcae5194-804b-470d-8c88-a0fdb8f3ae18', 'Potions Brewing Basics'),
       ('8eb45b56-c567-4735-85cd-7866050bdfc4', 'Wandwork and Spellcasting 101'),
       ('d72c8f47-1452-456e-bdfa-64382db4c556', 'Herbological Studies: Magical Plants'),
       ('93ed42c0-8caf-48c0-8e7d-abffbb2030c9', 'Magical Creature Care Workshop');

INSERT INTO classrooms
VALUES ('4e3858cc-8343-48da-887a-f5ced51bc4c9', 'Darkest Dungeon'),
       ('fcb0a731-768e-404c-a334-2c4934f723b0', 'Transfiguration Hall');

INSERT INTO scheduled_activities
VALUES ('ff084718-2fc0-4a33-9885-7d136fc8bf1c',
           SELECT id FROM activities WHERE activity_name = 'Potions Brewing Basics',
           SELECT id FROM classrooms WHERE classroom_name = 'Darkest Dungeon',
        CURRENT_TIMESTAMP() + INTERVAL '10' MINUTE,
        CURRENT_TIMESTAMP() + INTERVAL '55' MINUTE),
       ('eb680257-6467-43e5-9501-d7ce13679677',
           SELECT id FROM activities WHERE activity_name = 'Wandwork and Spellcasting 101',
           SELECT id FROM classrooms WHERE classroom_name = 'Transfiguration Hall',
        CURRENT_TIMESTAMP() - INTERVAL '10' MINUTE,
        CURRENT_TIMESTAMP() + INTERVAL '35' MINUTE);
