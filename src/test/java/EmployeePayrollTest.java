import com.magic.jdbc.entity.EmployeePayroll;
import com.magic.jdbc.service.Operation;
import com.magic.jdbc.utility.JDBCConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeePayrollTest {
    @Test
    void givenEmployeePayrollDb_retrieveDataFromTable_returnsEmployeePayrollData() {
        try {
            Operation operation=new Operation();
            JDBCConnection jdbcConnection=new JDBCConnection();
            ResultSet resultSet = jdbcConnection.createConnection("Select salary from payrollService where id=1").executeQuery();
            Object salary = null;
            while(resultSet.next()){
                salary = resultSet.getObject("salary");
            }
            Assertions.assertEquals(salary,operation.retrieveData().get(0).getSalary());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
