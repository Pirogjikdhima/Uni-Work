-- 1.Ndertoni funksionin qe gjen numrin total te punonjesve pasi merr titullin e punes.
CREATE OR REPLACE FUNCTION tot_emp(job_title IN VARCHAR2) RETURN INT
    IS
    total_emp INT;
BEGIN
    SELECT COUNT(*)
    INTO total_emp
    FROM EMPLOYEES
    WHERE JOB_ID = job_title;
    RETURN total_emp;
END;
BEGIN
    DECLARE
        result INT;
    BEGIN
        result := tot_emp('AD_VP');
        DBMS_OUTPUT.PUT_LINE('Total employees: ' || result);
    END;
END;
/
-- 2.Ndertoni nje funksion qe kthen n punonjesit me rrogen me te larte.
CREATE OR REPLACE FUNCTION top_n_employees(n IN NUMBER)
    RETURN SYS_REFCURSOR
    IS
    emp_cursor SYS_REFCURSOR;
BEGIN
    OPEN emp_cursor FOR
        SELECT FIRST_NAME, LAST_NAME, SALARY
        FROM EMPLOYEES
        ORDER BY SALARY DESC
            FETCH FIRST n ROWS ONLY;
    RETURN emp_cursor;
END;

-- 3.Ndertoni nje funksion qe kthen punonjesin me rrogen me te larte ne baze te departamentit.
CREATE OR REPLACE FUNCTION top_emp_per_dep(dep_ID IN INT) RETURN SYS_REFCURSOR
    IS
    emp_cursor SYS_REFCURSOR;
BEGIN
    OPEN emp_cursor FOR
        SELECT FIRST_NAME, LAST_NAME, SALARY
        FROM EMPLOYEES
        WHERE DEPARTMENT_ID = dep_ID
            FETCH FIRST 1 ROWS ONLY;
    RETURN emp_cursor;
END;


-- 4.Ndertoni nje funksion qe kthen punonjesit qe kane te njejtin menaxher.
CREATE OR REPLACE FUNCTION same_manager(menag_id IN INT) RETURN SYS_REFCURSOR
    IS
    emp_cursor SYS_REFCURSOR;
BEGIN
    OPEN emp_cursor FOR
        SELECT FIRST_NAME, LAST_NAME, SALARY
        FROM EMPLOYEES
        WHERE MANAGER_ID = menag_id;
    RETURN emp_cursor;
END;

-- 5.Ndertoni nje triger qe hedh exception nese shtohen produket me emra te dublikuara ne tabelen products.
CREATE OR REPLACE TRIGGER prevent_duplicate_products
    BEFORE INSERT OR UPDATE
    ON PRODUCTS
    FOR EACH ROW
DECLARE
    duplicate_count INT;
BEGIN
    SELECT COUNT(*)
    INTO duplicate_count
    FROM PRODUCTS
    WHERE LOWER(PRODUCT_NAME) = LOWER(:NEW.PRODUCT_NAME);

    IF duplicate_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Duplicate product name is not allowed.');
    END IF;
END;
/
-- 6.Afishoni per punonjesit qe e kane salary me te madh se 3200 department_name, d.department_id, first_name, last_name, job_id, salary.(duke perdorur kursorin)
DECLARE
    CURSOR cur_for_emp IS
        SELECT D.DEPARTMENT_NAME, D.DEPARTMENT_ID, E.FIRST_NAME, E.LAST_NAME, E.JOB_ID, E.SALARY
        FROM EMPLOYEES E
                 JOIN DEPARTMENTS D ON E.DEPARTMENT_ID = D.DEPARTMENT_ID
        WHERE E.SALARY > 3200;
    emp_record cur_for_emp%ROWTYPE;
BEGIN
    OPEN cur_for_emp;
    LOOP
        FETCH cur_for_emp INTO emp_record;
        EXIT WHEN cur_for_emp%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE('Department: ' || emp_record.department_name ||
                             ', Department ID: ' || emp_record.department_id ||
                             ', First Name: ' || emp_record.first_name ||
                             ', Last Name: ' || emp_record.last_name ||
                             ', Job ID: ' || emp_record.job_id ||
                             ', Salary: ' || emp_record.salary);
    END LOOP;
    CLOSE cur_for_emp;
END;
/
