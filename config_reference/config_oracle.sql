🖥️ Steps to Create a New User
#Log in as SYSDBA  
Open your terminal/command prompt and connect: Launch SQLPlus*
>sqlplus sys/password@localhost:1521/xe as sysdba
or
>sqlplus system/password@XE
OR @XEPDB1
>sqlplus hr/hr@//localhost:1521/XEPDB1
#>sqlplus NEXTLEVEL_FILE/admin@//localhost:1521/XEPDB1

Replace password with your SYSDBA password.

--------------
#Reconnect as SYSDBA (full DBA rights)  
From CMD:

cmd
sqlplus / as sysdba
This logs you in with the highest privileges.
--------------

1. Check if the user exists
Log in as SYSDBA:

cmd
sqlplus / as sysdba

Run:

sql
SELECT username FROM dba_users WHERE username = 'NEXTLEVEL_FILE';
--------------

#Create the user

sql
>CREATE USER NEXTLEVEL_FILE IDENTIFIED BY StrongPass123;
* NEXTLEVEL_FILE → your desired username

* StrongPass123 → your chosen password (must meet Oracle’s complexity rules)


#Then grant privileges:

sql
GRANT CONNECT, RESOURCE TO NEXTLEVEL_FILE;
ALTER USER NEXTLEVEL_FILE QUOTA 100M ON USERS;


#Grant privileges  
At minimum, grant CREATE SESSION so the user can log in:
sql
GRANT CREATE SESSION TO NEXTLEVEL_FILE;

#If the user needs to create tables and manage schema objects:
sql
GRANT CREATE TABLE, CREATE VIEW, CREATE SEQUENCE, CREATE PROCEDURE TO NEXTLEVEL_FILE;

#For full developer access, you can grant:
sql
GRANT RESOURCE, CONNECT TO NEXTLEVEL_FILE;

#Optional: grant DBA (not recommended for production)
sql
GRANT DBA TO NEXTLEVEL_FILE;

#Test the connection  
Exit and reconnect:
bash
sqlplus NEXTLEVEL_FILE/StrongPass123@localhost:1521/xe
------------------
###Error Fix ###########

SQL> CREATE USER NEXTLEVEL_FILE IDENTIFIED BY admin;
CREATE USER NEXTLEVEL_FILE IDENTIFIED BY admin
            *
ERROR at line 1:
ORA-65096: invalid common user or role name

The errors you’re seeing are because of Oracle multitenant architecture rules and syntax issues:

🔎 Why You Got ORA-65096
In Oracle 12c and later, when you’re connected to the CDB (Container Database), you can only create common users (names must start with C##).

#InterviewBit
The ORA-65096: invalid common user or role name error happens because you’re trying to create a user in the CDB$ROOT container without following Oracle’s naming rules. In Oracle 12c and above, there are two types of users:

* Common users (exist across all containers) → must start with C## (e.g., C##NEXTLEVEL_FILE).

* Local users (exist only in one pluggable database, like XEPDB1) → can have normal names (like NEXTLEVEL_FILE).

Right now, you’re in the root container, so Oracle expects the C## prefix.

#InterviewBit
⚖️ Which should you use?
* If your application connects specifically to XEPDB1 (as in your sqlplus NEXTLEVEL_FILE/admin@//localhost:1521/XEPDB1), you should create the user inside XEPDB1.

* Creating a C## common user in SYSDBA mode works, but it’s heavier and less secure — it exists across all containers, which is usually unnecessary for application schemas.

Example:

sql
CREATE USER C##NEXTLEVEL_FILE IDENTIFIED BY admin;
If you want a local user (without C##), you must connect to a PDB (Pluggable Database), not the root container.

🔎 Why You Got ORA-01935
You used single quotes 'NEXTLEVEL_FILE'.

In Oracle, single quotes denote string literals, not identifiers.

Correct syntax uses no quotes (or double quotes for case-sensitive names):

sql
CREATE USER NEXTLEVEL_FILE IDENTIFIED BY admin;
or

sql
CREATE USER "NextLevel_File" IDENTIFIED BY admin;

✅ How to Fix Step-by-Step
1. Check which container you’re in:

sql
SHOW CON_NAME;

* If it shows CDB$ROOT, you’re in the root container.
* If it shows XEPDB1 (or another PDB), you’re in a pluggable database.

2. Switch to a PDB (recommended for local users):

sql
ALTER SESSION SET CONTAINER = XEPDB1;
(Replace XEPDB1 with your actual PDB name.)

3. Create the user in the PDB:
sql
CREATE USER NEXTLEVEL_FILE IDENTIFIED BY admin;
GRANT CREATE SESSION TO NEXTLEVEL_FILE;

4. If you must stay in CDB root:
Use the C## prefix:
sql
CREATE USER C##NEXTLEVEL_FILE IDENTIFIED BY admin;
GRANT CREATE SESSION TO C##NEXTLEVEL_FILE;

⚠️ Important Notes
C## prefix requirement applies only in root container.

Local user creation is the usual approach for application schemas.

Privileges: Don’t forget to grant CONNECT or CREATE SESSION so the user can log in.
--------------------------------------------------------------------------------------------
Issue Fix: 2026-08-26 17:26:52 INFO  [boundedElastic-3] org.hibernate.orm.jdbc.batch - HHH100503: JDBC batch still contained JDBC statements on release
2026-08-26 17:26:52 WARN  [boundedElastic-3] org.hibernate.orm.jdbc.error - HHH000247: ErrorCode: 1950, SQLState: 42000
2026-08-26 17:26:52 WARN  [boundedElastic-3] org.hibernate.orm.jdbc.error - ORA-01950: no privileges on tablespace 'USERS'

https://docs.oracle.com/error-help/db/ora-01950/
Solution:
Meaning: The user account attempting the insert/update has insufficient quota or no privileges on the USERS tablespace.

✅ Solutions

1. Connect as SYSDBA:

cmd
sqlplus / as sysdba

2. Switch to the pluggable database:

sql
ALTER SESSION SET CONTAINER = XEPDB1;

sql
>GRANT CONNECT, RESOURCE TO NEXTLEVEL_FILE;
ALTER USER NEXTLEVEL_FILE QUOTA 100M ON USERS;

# For unlimited quota, run:
sql
ALTER USER your_user QUOTA UNLIMITED ON USERS;
Or grant a specific quota:

#OR

Grant System Privileges
If quota is not enough, grant space privileges:

sql
GRANT UNLIMITED TABLESPACE TO your_user;
⚠️ Use with caution — this gives unlimited access to all tablespaces.

3. Verify Current Quota
Check what quota is assigned:

sql
SELECT tablespace_name, max_bytes
FROM dba_ts_quotas
WHERE username = 'YOUR_USER';

4. Alternative: Use a Different Tablespace
If USERS is restricted, create a dedicated tablespace:

sql
CREATE TABLESPACE app_data DATAFILE 'app_data01.dbf' SIZE 500M AUTOEXTEND ON;
ALTER USER your_user DEFAULT TABLESPACE app_data;
ALTER USER your_user QUOTA UNLIMITED ON app_data;

⚠️ Risks & Best Practices
* Security: Avoid UNLIMITED TABLESPACE unless absolutely necessary. Prefer specific quotas.

* Production Safety: Always coordinate with DBA before altering quotas.

* Hibernate Batch: Ensure batch inserts are committed properly; dangling statements may appear if quota errors interrupt execution.