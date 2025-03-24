--1. Afishoni te dhenat per punonjesit qe filluan punen ne vitin 2012.
    SELECT *
    FROM EMPLOYEES
    WHERE HIRE_DATE BETWEEN TO_DATE('2012-01-01', 'YYYY-MM-DD') AND TO_DATE('2012-12-31', 'YYYY-MM-DD');

--2. Afishoni emrin,mbiemrin,numrin dhe emrin e deparamentit per secilin punonjes.
    SELECT
        E.FIRST_NAME,
        E.LAST_NAME,
        E.PHONE_NUMBER,
        D.DEPARTMENT_NAME
    FROM EMPLOYEES E
    LEFT JOIN DEPARTMENTS D
    ON E.DEPARTMENT_ID = D.DEPARTMENT_ID;

--3. Afishoni emrin,mbiemrin, deparamentin,qytetin dhe shetin per secilin punonjes.
    SELECT
        E.FIRST_NAME,
        E.LAST_NAME,
        D.DEPARTMENT_NAME,
        L.CITY,
        C.COUNTRY_NAME
    FROM EMPLOYEES E
    JOIN DEPARTMENTS D ON E.DEPARTMENT_ID = D.DEPARTMENT_ID
    JOIN LOCATIONS L ON D.LOCATION_ID = L.LOCATION_ID
    JOIN COUNTRIES C ON L.COUNTRY_ID = C.COUNTRY_ID;

-- 4. Afishoni emrin,mbiemrin,emrin dhe id e departementit per te gjithe departamentet edhe per ato qe nuk kane punonjes.
    SELECT
        E.FIRST_NAME,
        E.LAST_NAME,
        D.DEPARTMENT_NAME,
        D.DEPARTMENT_ID
    FROM EMPLOYEES E
    RIGHT JOIN DEPARTMENTS D ON E.DEPARTMENT_ID = D.DEPARTMENT_ID;

-- 5. Afishoni emrin e punonjesit dhe menaxherit edhe per punonjesit qe nuk kane menaxher.
    SELECT
        E.FIRST_NAME AS EMPLOYEE_FIRST_NAME,
        E.LAST_NAME AS EMPLOYEE_LAST_NAME,
        M.FIRST_NAME AS MANAGER_FIRST_NAME,
        M.LAST_NAME AS MANAGER_LAST_NAME
    FROM EMPLOYEES E
    LEFT JOIN EMPLOYEES M ON E.MANAGER_ID = M.EMPLOYEE_ID;

-- 6. Afishoni emrin e departamentit,rrogen mesatare dhe numrin e punonjesve per ata punonjes qe marrin komision ne ate department.
    SELECT
        D.DEPARTMENT_NAME,
        ROUND(AVG(E.SALARY),2) AS AVERAGE_SALARY,
        COUNT(E.EMPLOYEE_ID) AS EMPLOYEE_COUNT,
        COUNT(E.COMMISSION_PCT) AS COMMISSION_COUNT
    FROM EMPLOYEES E
    JOIN DEPARTMENTS D ON E.DEPARTMENT_ID = D.DEPARTMENT_ID
    GROUP BY D.DEPARTMENT_NAME
    HAVING COUNT(E.COMMISSION_PCT) >= 0;

-- 7. Afishoni departamntin,emrin e menaxherit dhe rrogen e menaxherve qe kane experience me shume se 5 vjet.
    SELECT DISTINCT
        D.DEPARTMENT_NAME,
        M.FIRST_NAME AS MANAGER_NAME,
        M.SALARY
    FROM EMPLOYEES E
    JOIN EMPLOYEES M ON E.MANAGER_ID = M.EMPLOYEE_ID
    JOIN DEPARTMENTS D ON M.DEPARTMENT_ID = D.DEPARTMENT_ID
    WHERE MONTHS_BETWEEN(SYSDATE, M.HIRE_DATE) > 60;

-- 8. Afishoni shtetin ,qytetin dhe numrin e departamenteve qe kane te pakten 2 punonjes.

    SELECT
        C.COUNTRY_NAME,
        L.CITY,
        COUNT(DISTINCT E.DEPARTMENT_ID) AS NUMBER_OF_DEPARTMENTS
    FROM DEPARTMENTS D
             JOIN LOCATIONS L ON D.LOCATION_ID = L.LOCATION_ID
             JOIN COUNTRIES C ON L.COUNTRY_ID = C.COUNTRY_ID
             JOIN
            (

               SELECT DEPARTMENT_ID
               FROM EMPLOYEES
               GROUP BY DEPARTMENT_ID
               HAVING COUNT(*) >= 2

            ) E ON D.DEPARTMENT_ID = E.DEPARTMENT_ID
    GROUP BY C.COUNTRY_NAME, L.CITY
    ORDER BY NUMBER_OF_DEPARTMENTS;

-- 9. Afishoni emrin e plote,punen,daten e fillimit dhe te mbarimit te punes se fundit per punonjesit qe kane punuar pa perqindje komisioni.
    SELECT
        E.FIRST_NAME,
        E.LAST_NAME,
        J.JOB_TITLE,
        JH.START_DATE,
        JH.END_DATE
    FROM EMPLOYEES E
    JOIN JOBS J ON E.JOB_ID = J.JOB_ID
    JOIN JOB_HISTORY JH ON E.EMPLOYEE_ID = JH.EMPLOYEE_ID
    WHERE E.COMMISSION_PCT IS NULL AND
          JH.END_DATE =
          (
            SELECT MAX(JH2.END_DATE)
            FROM JOB_HISTORY JH2
            WHERE JH2.EMPLOYEE_ID = E.EMPLOYEE_ID
          );

-- 10. Afishoni emrin e punonjesve per ate punonjes qe ka me shume vite se menaxheri I tij.
    SELECT
        E.FIRST_NAME,
        E.LAST_NAME
    FROM EMPLOYEES E
    JOIN EMPLOYEES M ON E.MANAGER_ID = M.EMPLOYEE_ID
    WHERE MONTHS_BETWEEN(SYSDATE, E.HIRE_DATE) > MONTHS_BETWEEN(SYSDATE, M.HIRE_DATE);