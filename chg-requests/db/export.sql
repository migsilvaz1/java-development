--------------------------------------------------------
--  DDL for Sequence REQUESTS_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "REQUESTS_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1;
--------------------------------------------------------
--  DDL for Sequence TEMPLATES_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "TEMPLATES_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 5;
--------------------------------------------------------
--  DDL for Sequence USERS_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "USERS_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 2 ;
--------------------------------------------------------
--  DDL for Table REQUESTS
--------------------------------------------------------

  CREATE TABLE "REQUESTS" 
   (	"ID" NUMBER, 
	"USERS_FK" NUMBER, 
	"TEMPLATES_FK" NUMBER, 
	"DOC_CONTENT" BLOB, 
	"REGISTER_DATE" TIMESTAMP (2) WITH TIME ZONE DEFAULT SYSTIMESTAMP, 
	"STATUS" VARCHAR2(20 CHAR) DEFAULT 'PENDIENTE_FIRMA', 
	"PF_REFERENCE" VARCHAR2(100 CHAR), 
	"PF_DETAILED_STATUS" CLOB, 
	"DOC_SIGNED_CONTENT" BLOB
   ) ;
--------------------------------------------------------
--  DDL for Table TEMPLATES
--------------------------------------------------------

  CREATE TABLE "TEMPLATES" 
   (	"ID" NUMBER, 
	"NAME" VARCHAR2(100 CHAR), 
	"VERSION" VARCHAR2(3 CHAR) DEFAULT '1.0', 
	"ACTIVE" NUMBER(1,0) DEFAULT 1, 
	"CONTENT" BLOB, 
	"APPROVAL_CIRCUIT" VARCHAR2(1000 CHAR)
   ) ;

   COMMENT ON COLUMN "TEMPLATES"."APPROVAL_CIRCUIT" IS 'Al menos debe tener una firma. Formato: PARALELO|CASCADA:CARGO|NIF-VISTO_BUENO|FIRMA:CARGO|NIF-VISTO_BUENO|FIRMA';
--------------------------------------------------------
--  DDL for Table USERS
--------------------------------------------------------

  CREATE TABLE "USERS" 
   (	"ID" NUMBER, 
	"IDENTIFIER" VARCHAR2(9 CHAR), 
	"NAME" VARCHAR2(100 CHAR)
   ) ;
  
REM INSERTING into TEMPLATES
Insert into TEMPLATES (ID,NAME,VERSION,ACTIVE,APPROVAL_CIRCUIT) values (1,'Solicitud de Ocupación de Vivienda en Pantano de los Hurones
','1.0',1,'PARALELO:48964671V-VISTO_BUENO');
Insert into TEMPLATES (ID,NAME,VERSION,ACTIVE,APPROVAL_CIRCUIT) values (2,'Solicitud de Ocupación de Vivienda en el Pantano del Pintado','1.0',1,'PARALELO:48964671V-VISTO_BUENO');
Insert into TEMPLATES (ID,NAME,VERSION,ACTIVE,APPROVAL_CIRCUIT) values (3,'Solicitud de Material','1.0',1,'PARALELO:48964671V-VISTO_BUENO');
Insert into TEMPLATES (ID,NAME,VERSION,ACTIVE,APPROVAL_CIRCUIT) values (4,'Solicitud de Préstamo','1.0',1,'PARALELO:48964671V-VISTO_BUENO');

REM INSERTING into USERS
Insert into USERS (ID,IDENTIFIER,NAME) values (1,'48964671V','José Antonio Cortegana Camúñez');
--------------------------------------------------------
--  DDL for Index REQUESTS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "REQUESTS_PK" ON "REQUESTS" ("ID");
--------------------------------------------------------
--  DDL for Index TEMPLATES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TEMPLATES_PK" ON "TEMPLATES" ("ID");
--------------------------------------------------------
--  DDL for Index USERS_NIF_UK
--------------------------------------------------------

  CREATE UNIQUE INDEX "USERS_NIF_UK" ON "USERS" ("IDENTIFIER");
--------------------------------------------------------
--  DDL for Index USERS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "USERS_PK" ON "USERS" ("ID");
--------------------------------------------------------
--  Constraints for Table REQUESTS
--------------------------------------------------------

  ALTER TABLE "REQUESTS" ADD CONSTRAINT "REQUESTS_STATUS_CHK" CHECK (STATUS IN ('PENDIENTE_FIRMA', 'FIRMADO', 'PENDIENTE_APROBACION', 'APROBADO', 'RECHAZADO')) ENABLE;
  ALTER TABLE "REQUESTS" ADD CONSTRAINT "REQUESTS_PK" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "REQUESTS" MODIFY ("STATUS" NOT NULL ENABLE);
  ALTER TABLE "REQUESTS" MODIFY ("REGISTER_DATE" NOT NULL ENABLE);
  ALTER TABLE "REQUESTS" MODIFY ("DOC_CONTENT" NOT NULL ENABLE);
  ALTER TABLE "REQUESTS" MODIFY ("TEMPLATES_FK" NOT NULL ENABLE);
  ALTER TABLE "REQUESTS" MODIFY ("USERS_FK" NOT NULL ENABLE);
  ALTER TABLE "REQUESTS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TEMPLATES
--------------------------------------------------------

  ALTER TABLE "TEMPLATES" MODIFY ("APPROVAL_CIRCUIT" NOT NULL ENABLE);
  ALTER TABLE "TEMPLATES" ADD CONSTRAINT "TEMPLATES_ACTIVE_CHK" CHECK (ACTIVE IN (0, 1)) ENABLE;
  ALTER TABLE "TEMPLATES" MODIFY ("ACTIVE" NOT NULL ENABLE);
  ALTER TABLE "TEMPLATES" MODIFY ("VERSION" NOT NULL ENABLE);
  ALTER TABLE "TEMPLATES" ADD CONSTRAINT "TEMPLATES_PK" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "TEMPLATES" MODIFY ("CONTENT" NOT NULL ENABLE);
  ALTER TABLE "TEMPLATES" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "TEMPLATES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table USERS
--------------------------------------------------------

  ALTER TABLE "USERS" ADD CONSTRAINT "USERS_NIF_UK" UNIQUE ("IDENTIFIER") ENABLE;
  ALTER TABLE "USERS" ADD CONSTRAINT "USERS_PK" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "USERS" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "USERS" MODIFY ("IDENTIFIER" NOT NULL ENABLE);
  ALTER TABLE "USERS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table REQUESTS
--------------------------------------------------------

  ALTER TABLE "REQUESTS" ADD CONSTRAINT "REQ_TEMPLATES_FK" FOREIGN KEY ("TEMPLATES_FK")
	  REFERENCES "TEMPLATES" ("ID") ENABLE;
  ALTER TABLE "REQUESTS" ADD CONSTRAINT "REQ_USERS_FK" FOREIGN KEY ("USERS_FK")
	  REFERENCES "USERS" ("ID") ENABLE;
--------------------------------------------------------
--  DDL for Trigger REQUESTS_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "REQUESTS_TRG" BEFORE INSERT ON REQUESTS 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF :NEW.ID IS NULL THEN
      SELECT REQUESTS_SEQ.NEXTVAL INTO :NEW.ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "REQUESTS_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TEMPLATES_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "TEMPLATES_TRG" BEFORE INSERT ON TEMPLATES 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF :NEW.ID IS NULL THEN
      SELECT TEMPLATES_SEQ.NEXTVAL INTO :NEW.ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "TEMPLATES_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger USERS_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "USERS_TRG" BEFORE INSERT ON USERS 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF :NEW.ID IS NULL THEN
      SELECT USERS_SEQ.NEXTVAL INTO :NEW.ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "USERS_TRG" ENABLE;
