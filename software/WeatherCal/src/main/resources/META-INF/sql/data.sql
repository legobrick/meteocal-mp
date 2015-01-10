INSERT INTO "development"."group_" ("name") SELECT 'USERS' WHERE (SELECT COUNT(*) FROM "development"."group_" WHERE "name" = 'USERS') = 0
