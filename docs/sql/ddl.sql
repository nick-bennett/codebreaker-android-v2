CREATE TABLE IF NOT EXISTS `user_profile`
(
    `user_id`      INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `display_name` TEXT                              NOT NULL,
    `oauth_key`    TEXT                              NOT NULL,
    `created`      INTEGER                           NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS `index_user_profile_oauth_key` ON `user_profile` (`oauth_key`);

CREATE TABLE IF NOT EXISTS `Match`
(
    `match_id`  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `match_key` BLOB                              NOT NULL,
    `started`   INTEGER                           NOT NULL,
    `deadline`  INTEGER                           NOT NULL,
    `state`     INTEGER                           NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS `index_Match_match_key` ON `Match` (`match_key`);

CREATE INDEX IF NOT EXISTS `index_Match_started` ON `Match` (`started`);

CREATE INDEX IF NOT EXISTS `index_Match_deadline` ON `Match` (`deadline`);

CREATE INDEX IF NOT EXISTS `index_Match_state` ON `Match` (`state`);

CREATE TABLE IF NOT EXISTS `Game`
(
    `game_id`     INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `game_key`    BLOB,
    `match_id`    INTEGER,
    `pool`        TEXT                              NOT NULL,
    `code`        TEXT,
    `code_length` INTEGER                           NOT NULL,
    `started`     INTEGER                           NOT NULL,
    FOREIGN KEY (`match_id`) REFERENCES `Match` (`match_id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS `index_Game_game_key` ON `Game` (`game_key`);

CREATE INDEX IF NOT EXISTS `index_Game_match_id` ON `Game` (`match_id`);

CREATE INDEX IF NOT EXISTS `index_Game_code_length` ON `Game` (`code_length`);

CREATE INDEX IF NOT EXISTS `index_Game_started` ON `Game` (`started`);

CREATE TABLE IF NOT EXISTS `Guess`
(
    `guess_id`  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `game_id`   INTEGER                           NOT NULL,
    `guess_key` BLOB,
    `submitted` INTEGER                           NOT NULL,
    `text`      TEXT                              NOT NULL,
    `correct`   INTEGER                           NOT NULL,
    `close`     INTEGER                           NOT NULL,
    FOREIGN KEY (`game_id`) REFERENCES `Game` (`game_id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS `index_Guess_guess_key` ON `Guess` (`guess_key`);

CREATE INDEX IF NOT EXISTS `index_Guess_game_id_submitted` ON `Guess` (`game_id`, `submitted`);

CREATE INDEX IF NOT EXISTS `index_Guess_game_id` ON `Guess` (`game_id`);

CREATE VIEW `Score` AS
SELECT gm.game_id, gm.pool, gm.code, gm.code_length, gm.started, s.submitted, gs.guess_count
FROM Game AS gm
         INNER JOIN Guess AS s ON s.game_id = gm.game_id AND s.correct = gm.code_length
         INNER JOIN (SELECT game_id, COUNT(*) AS guess_count FROM Guess GROUP BY game_id) AS gs
                    ON gs.game_id = gm.game_id;
