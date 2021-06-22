import com.magic.jdbc.entity.EmployeePayroll;
import com.magic.jdbc.service.Operation;
import com.magic.jdbc.utility.JDBCConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class EmployeePayrollTest {
    @Test
    void givenEmployeePayrollDb_retrieveDataFromTable_returnsEmployeePayrollData() {
        try {
            JDBCConnection jdbcConnection=new JDBCConnection();
            ResultSet resultSet = jdbcConnection.createConnection("Select salary from payrollService where id=1").executeQuery();
            Object salary = null;
            while(resultSet.next()){
                salary = resultSet.getObject("salary");
            }
            Assertions.assertEquals(salary,300000);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void givenUpdatedSalary_updateSalaryInDbAndPayrollObject_returnsTrueIfDataMatches() {
        Operation operation=new Operation();
        Assertions.assertEquals("300000",operation.UpdatedEmployeeObject("priyanshi").get(0).getSalary());
    }

    @Test
    void givenDateRange_EmployeePayrollFromDbWithinDateRange_returnsListOfEmployees() {
        Operation operation=new Operation();
        LocalDate startDate = LocalDate.of(2019, 01, 01);
        LocalDate endDate = LocalDate.now();
        Assertions.assertEquals("priyanshi",operation.employeeWithinDateRange(startDate,endDate).get(0).getName());
    }

    @Test
    void GivenNewEmployeeData_InsertIntoDb_retrieveInObjectAndCompare() {
        Operation operation=new Operation();
        Assertions.assertEquals("Ayush",operation.retrieveTable().get(1).getName());
    }

    @Test
    void GivenNewEmployeeData_AddsNewTableInDb_ReturnsDetailsFromNewTable() {
        Operation operation=new Operation();
        LocalDate date=LocalDate.of(2020,02,03);
        operation.addIntoPayrollDetails(3,"saumya",200000,date,"female");
        Assertions.assertEquals(200000,operation.retrieveTable().get(2).getSalary());
    }
}
