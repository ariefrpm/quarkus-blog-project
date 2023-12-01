CREATE TABLE IF NOT EXISTS post(
    id serial not null,
    title varchar,
    content varchar,
    tags varchar[]
);
CREATE TABLE IF NOT EXISTS tag(
    id serial not null,
    label varchar unique
);
INSERT INTO post(id, title, content, tags) VALUES(1, 'tittle1', 'content1', '{"tag1"}');
INSERT INTO post(id, title, content, tags) VALUES(2, 'tittle2', 'content2', '{"tag1", "tag2"}');
INSERT INTO post(id, title, content) VALUES(3, 'tittle3', 'content3');
INSERT INTO tag(id, label) VALUES(1, 'tag1');
INSERT INTO tag(id, label) VALUES(2, 'tag2');
ALTER SEQUENCE Post_SEQ RESTART WITH 4;
ALTER SEQUENCE Tag_SEQ RESTART WITH 3;
ALTER SEQUENCE Post_SEQ INCREMENT BY 1;
ALTER SEQUENCE Tag_SEQ INCREMENT BY 1;