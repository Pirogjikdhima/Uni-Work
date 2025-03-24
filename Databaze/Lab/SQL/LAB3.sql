-- 1. Afishoni  id e departamentit,emrn dhe job_id per punonjesit qe punojne ne departamentin e finances.
    SELECT DEPARTMENT_ID,FIRST_NAME,JOB_ID FROM DEPARTMENTS
    JOIN EMPLOYEES  USING (DEPARTMENT_ID)
    WHERE DEPARTMENT_NAME='Finance';


-- 2. Afishoni emrin,id dhe job_id per punonjesit qe e kane departamentin ne ‘Toronto’.
    SELECT FIRST_NAME,EMPLOYEE_ID,JOB_ID FROM EMPLOYEES
    JOIN DEPARTMENTS  USING (DEPARTMENT_ID)
    JOIN LOCATIONS USING (LOCATION_ID)
    WHERE CITY='Toronto';

-- 3. Afishoni id,emrin e plote(emri dhe mbiiemri),rrogen,AvgCompare(rroga – rrogen mesatere) dhe SalaryStatus qe mban high dhe low per punonjesit qe e kane rrogen me te madhe dhe me te vogel se rroga mesatere e punonjesve.
    SELECT EMPLOYEE_ID, FIRST_NAME || ' ' || LAST_NAME AS FULL_NAME,
        SALARY,
        SALARY - (SELECT ROUND(AVG(E.SALARY),2) FROM EMPLOYEES E) AS AvgCompare,
        CASE
            WHEN SALARY > (SELECT ROUND(AVG(E.SALARY),2) FROM EMPLOYEES E) THEN 'High'
        ELSE 'Low'
        END AS SalaryStatus
    FROM EMPLOYEES;

-- 4. Afishoni emrin,mbiemrin e punonjeve qe kane nje menaxher qe qendron ne ‘US’.
    SELECT E.FIRST_NAME||' '||E.LAST_NAME AS FULL_NAME
    FROM EMPLOYEES E
    JOIN DEPARTMENTS D ON E.DEPARTMENT_ID=D.DEPARTMENT_ID
    JOIN LOCATIONS L ON D.LOCATION_ID=L.LOCATION_ID
    WHERE L.COUNTRY_ID='US'AND E.MANAGER_ID IS NOT NULL;

-- 5. Afishoni id,emrin,mbiemri,rrogen,id e departamentit dh qytetin e punonjesve qe kane rrogen qe eshte sa max I rrogave te punonjesve qe kane fillur pune nga data January 1st, 2002 deri ne December 31st, 2003.
    SELECT EMPLOYEE_ID,FIRST_NAME,LAST_NAME,SALARY,DEPARTMENT_ID,CITY
    FROM EMPLOYEES
    JOIN DEPARTMENTS USING (DEPARTMENT_ID)
    JOIN LOCATIONS USING (LOCATION_ID)
    WHERE SALARY = (SELECT MAX(SALARY) FROM EMPLOYEES WHERE HIRE_DATE BETWEEN '01-JAN-02' AND '31-DEC-03');

-- 6. Afishoni id,emrin dhe id e departementit te punonjesve qe fitojne me shume se rroga me e madhe e departamentit me id 40.
    SELECT EMPLOYEE_ID,FIRST_NAME,DEPARTMENT_ID
    FROM EMPLOYEES
    WHERE SALARY > (SELECT MAX(SALARY) FROM EMPLOYEES WHERE DEPARTMENT_ID=40);

-- 7. Afishoni emrin,mbiemrin,rrogen dhe departementin per punonjesit qe punojne ne te njejtin department me punonjesin me id 201
    SELECT FIRST_NAME,LAST_NAME,SALARY,DEPARTMENT_NAME FROM EMPLOYEES
    JOIN DEPARTMENTS USING (DEPARTMENT_ID)
    WHERE DEPARTMENT_ID=(SELECT DEPARTMENT_ID FROM EMPLOYEES WHERE EMPLOYEE_ID=201);

-- 8. Afishoni emrin,mbiemrin,rrogen dhe id e departamentit per punojesit qe fitojne me pak se rroga mesater dhe kane punonjes me emrin ‘Laura’.
    SELECT FIRST_NAME,LAST_NAME,SALARY,DEPARTMENT_ID
    FROM EMPLOYEES
    WHERE SALARY < (SELECT AVG(SALARY) FROM EMPLOYEES) AND FIRST_NAME='Laura';
