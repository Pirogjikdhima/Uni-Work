-- 1-Krijoni nje sekuence per rritjen e id ne tabelen Employees.
----Nuk duhet te lejoje riperseritje vlerash
----Vlera duhet gjeneruar vetem kur te kerkohet nga ne, jo para.

CREATE SEQUENCE sk_rritje
    NOCYCLE
    NOCACHE;

--2- Ndertoni nje view qe shfaq emrin dhe id e departamenteve.
CREATE OR REPLACE VIEW vw_em_id_dep AS
SELECT DEPARTMENT_ID, DEPARTMENT_NAME
FROM DEPARTMENTS;

-- 3-Ndertoni nje view qe afishon id,emrin,rrogen,id e departamentit dhe qytetin e punonjesve qe kane rrogen qe eshte sa minI rrogave te punonjesve qe kane fillur pune nga data January 1st, 2002 deri ne December 31st, 2003.
CREATE OR REPLACE VIEW vw_usht_2 AS
SELECT EMPLOYEE_ID, FIRST_NAME, LAST_NAME, DEPARTMENT_ID, CITY
FROM EMPLOYEES
         JOIN DEPARTMENTS USING (DEPARTMENT_ID)
         JOIN LOCATIONS USING (LOCATION_ID)
WHERE SALARY = (SELECT MIN(SALARY)
                FROM EMPLOYEES
                WHERE HIRE_DATE BETWEEN TO_DATE('2002-01-01', 'YYYY-MM-DD') AND TO_DATE('2003-12-31', 'YYYY-MM-DD'));

-- 4-Shto nje punonjes te ri ne tabelen Employees
INSERT INTO EMPLOYEES(EMPLOYEE_ID, FIRST_NAME, LAST_NAME, EMAIL, PHONE_NUMBER, HIRE_DATE, JOB_ID, SALARY,
                      COMMISSION_PCT, MANAGER_ID, DEPARTMENT_ID)
VALUES (sk_rritje.NEXTVAL, 'John', 'Doe', 'john.doe@example.com', '123-456-7890', TO_DATE('2023-01-01', 'YYYY-MM-DD'),
        'IT_PROG', 60000, NULL, 101, 10);

-- 5-Krijoni nje indeks ne tabelen Employees duke ditur qe na kerkohet shpesh info mbi perqindje e komisionit per nje punonjes
CREATE INDEX idx_commission_pct ON EMPLOYEES (COMMISSION_PCT);





