-- Only MASTER data, no DIRTY database versions

-- 1. Add "file1", "file2", and "file3" in 3 database versions

INSERT INTO CHUNK VALUES('8ce24fc0ea8e685eb23bf6346713ad9fef920425',1);
INSERT INTO CHUNK VALUES('bf8b4530d8d246dd74ac53a13471bba17941dff7',1);
INSERT INTO CHUNK VALUES('fe83f217d464f6fdfa5b2b1f87fe3a1a47371196',1);
INSERT INTO DATABASEVERSION VALUES(0,'MASTER',TIMESTAMP(1388586369) + TIMEZONE(),'A','(A1)');
INSERT INTO DATABASEVERSION VALUES(1,'MASTER',TIMESTAMP(1388672769) + TIMEZONE(),'A','(A2)');
INSERT INTO DATABASEVERSION VALUES(2,'MASTER',TIMESTAMP(1388759169) + TIMEZONE(),'A','(A3)');
INSERT INTO DATABASEVERSION_VECTORCLOCK VALUES(0,'A',1);
INSERT INTO DATABASEVERSION_VECTORCLOCK VALUES(1,'A',2);
INSERT INTO DATABASEVERSION_VECTORCLOCK VALUES(2,'A',3);
INSERT INTO FILECONTENT VALUES('8ce24fc0ea8e685eb23bf6346713ad9fef920425',1);
INSERT INTO FILECONTENT VALUES('bf8b4530d8d246dd74ac53a13471bba17941dff7',1);
INSERT INTO FILECONTENT VALUES('fe83f217d464f6fdfa5b2b1f87fe3a1a47371196',1);
INSERT INTO FILECONTENT_CHUNK VALUES('8ce24fc0ea8e685eb23bf6346713ad9fef920425','8ce24fc0ea8e685eb23bf6346713ad9fef920425',0);
INSERT INTO FILECONTENT_CHUNK VALUES('bf8b4530d8d246dd74ac53a13471bba17941dff7','bf8b4530d8d246dd74ac53a13471bba17941dff7',0);
INSERT INTO FILECONTENT_CHUNK VALUES('fe83f217d464f6fdfa5b2b1f87fe3a1a47371196','fe83f217d464f6fdfa5b2b1f87fe3a1a47371196',0);
INSERT INTO FILEHISTORY VALUES('4fef2d605640813464792b18b16e1a5e07aa4e53',2);
INSERT INTO FILEHISTORY VALUES('851c441915478a539a5bab2b263ffa4cc48e282f',0);
INSERT INTO FILEHISTORY VALUES('c021aecb2ae36f2a8430eb10309923454b93b61e',1);
INSERT INTO FILEVERSION VALUES('4fef2d605640813464792b18b16e1a5e07aa4e53',1,2,'file3','FILE','NEW',1,TIMESTAMP(1388759169) + TIMEZONE(),NULL,'8ce24fc0ea8e685eb23bf6346713ad9fef920425',TIMESTAMP(1388759169) + TIMEZONE(),'rw-r--r--',NULL);
INSERT INTO FILEVERSION VALUES('851c441915478a539a5bab2b263ffa4cc48e282f',1,0,'file1','FILE','NEW',1,TIMESTAMP(1388586367) + TIMEZONE(),NULL,'fe83f217d464f6fdfa5b2b1f87fe3a1a47371196',TIMESTAMP(1388586368) + TIMEZONE(),'rw-r--r--',NULL);
INSERT INTO FILEVERSION VALUES('c021aecb2ae36f2a8430eb10309923454b93b61e',1,1,'file2','FILE','NEW',1,TIMESTAMP(1388672769) + TIMEZONE(),NULL,'bf8b4530d8d246dd74ac53a13471bba17941dff7',TIMESTAMP(1388672769) + TIMEZONE(),'rw-r--r--',NULL);
INSERT INTO MULTICHUNK VALUES('ac1d89b3f57349edc6fe29f9bef1b0aeadf499a8',0,11);
INSERT INTO MULTICHUNK VALUES('e2a3f6bea38fcc90a35654f3500333115cf67943',2,22);
INSERT INTO MULTICHUNK VALUES('e5c62378c7c4d99a84a186a41034c3dbf9a3ad7b',1,33);
INSERT INTO MULTICHUNK_CHUNK VALUES('ac1d89b3f57349edc6fe29f9bef1b0aeadf499a8','fe83f217d464f6fdfa5b2b1f87fe3a1a47371196');
INSERT INTO MULTICHUNK_CHUNK VALUES('e2a3f6bea38fcc90a35654f3500333115cf67943','8ce24fc0ea8e685eb23bf6346713ad9fef920425');
INSERT INTO MULTICHUNK_CHUNK VALUES('e5c62378c7c4d99a84a186a41034c3dbf9a3ad7b','bf8b4530d8d246dd74ac53a13471bba17941dff7');

-- 2a. Add changed "file1" (changed posix permission) and new file "beef" (new content!) -- DIRTY

INSERT INTO CHUNK VALUES('beefbeefbeefbeefbeefbeefbeefbeefbeefbeef',1);
INSERT INTO DATABASEVERSION VALUES(3,'DIRTY',TIMESTAMP(1388845689) + TIMEZONE(),'B','(B1)');
INSERT INTO DATABASEVERSION_VECTORCLOCK VALUES(3,'B',1);
INSERT INTO FILECONTENT VALUES('beefbeefbeefbeefbeefbeefbeefbeefbeefbeef',1);
INSERT INTO FILECONTENT_CHUNK VALUES('beefbeefbeefbeefbeefbeefbeefbeefbeefbeef','beefbeefbeefbeefbeefbeefbeefbeefbeefbeef',0);
INSERT INTO FILEHISTORY VALUES('beef111111111111111111111111111111111111',3);
INSERT INTO FILEHISTORY VALUES('851c441915478a539a5bab2b263ffa4cc48e282f',3);
INSERT INTO FILEVERSION VALUES('851c441915478a539a5bab2b263ffa4cc48e282f',2,3,'file1','FILE','CHANGED',1,TIMESTAMP(1388845687) + TIMEZONE(),NULL,'fe83f217d464f6fdfa5b2b1f87fe3a1a47371196',TIMESTAMP(1388845568) + TIMEZONE(),'rwxrw-r--',NULL);
INSERT INTO FILEVERSION VALUES('beef111111111111111111111111111111111111',1,3,'beef','FILE','NEW',1,TIMESTAMP(1388932087) + TIMEZONE(),NULL,'beefbeefbeefbeefbeefbeefbeefbeefbeefbeef',TIMESTAMP(1388931968) + TIMEZONE(),'rw-r--r--',NULL);
INSERT INTO MULTICHUNK VALUES('1234567890987654321123456789098765433222',3,44);
INSERT INTO MULTICHUNK_CHUNK VALUES('1234567890987654321123456789098765433222','beefbeefbeefbeefbeefbeefbeefbeefbeefbeef');

-- 2b. Delete "file1"

INSERT INTO DATABASEVERSION VALUES(4,'MASTER',TIMESTAMP(1388845689) + TIMEZONE(),'A','(A4)');
INSERT INTO DATABASEVERSION_VECTORCLOCK VALUES(4,'A',4);
INSERT INTO FILEHISTORY VALUES('851c441915478a539a5bab2b263ffa4cc48e282f',4);
INSERT INTO FILEVERSION VALUES('851c441915478a539a5bab2b263ffa4cc48e282f',2,4,'file1','FILE','DELETED',1,TIMESTAMP(1388845687) + TIMEZONE(),NULL,'fe83f217d464f6fdfa5b2b1f87fe3a1a47371196',TIMESTAMP(1388845568) + TIMEZONE(),'rw-r--r--',NULL);

-- 3. Add new "file1"

INSERT INTO CHUNK VALUES('ffffffffffffffffffffffffffffffffffffffff',1);
INSERT INTO DATABASEVERSION VALUES(5,'MASTER',TIMESTAMP(1388932089) + TIMEZONE(),'A','(A5)');
INSERT INTO DATABASEVERSION_VECTORCLOCK VALUES(5,'A',5);
INSERT INTO FILECONTENT VALUES('ffffffffffffffffffffffffffffffffffffffff',1);
INSERT INTO FILECONTENT_CHUNK VALUES('ffffffffffffffffffffffffffffffffffffffff','ffffffffffffffffffffffffffffffffffffffff',0);
INSERT INTO FILEHISTORY VALUES('abcdeffaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa',5);
INSERT INTO FILEVERSION VALUES('abcdeffaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa',1,5,'file1','FILE','NEW',1,TIMESTAMP(1388932087) + TIMEZONE(),NULL,'ffffffffffffffffffffffffffffffffffffffff',TIMESTAMP(1388931968) + TIMEZONE(),'rw-r--r--',NULL);
INSERT INTO MULTICHUNK VALUES('dddddddddddddddddddddddddddddddddddddddd',5,55);
INSERT INTO MULTICHUNK_CHUNK VALUES('dddddddddddddddddddddddddddddddddddddddd','ffffffffffffffffffffffffffffffffffffffff');



