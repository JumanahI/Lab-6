package com.example.Employee_Management_System.Controller;

import com.example.Employee_Management_System.Api.ApiResponse;
import com.example.Employee_Management_System.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee-management-system")
public class EmployeeController {

    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/get")
    public ResponseEntity<?> getEmployees() {
        return ResponseEntity.status(200).body(employees);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@RequestBody @Valid Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        employees.add(employee);
        return ResponseEntity.status(200).body(new ApiResponse("Employee added successfully"));
    }

    @PutMapping("/update/{index}")
    public ResponseEntity<?> updateEmployee(@PathVariable int index, @RequestBody Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        employees.set(index, employee);
        return ResponseEntity.status(200).body(new ApiResponse("Employee data updated successfully"));
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String ID){
       boolean employeeExist=false;
        for (Employee employee:employees) {
            if (employee.getID().equals(ID)) {
                employeeExist = true;
            }
        }
        if (employeeExist){
            for (Employee employeeFound:employees){
                if(employeeFound.getID().equals(ID)){
                    employees.remove(employeeFound);
                    return ResponseEntity.status(200).body(new ApiResponse("Employee deleted successfully"));
                }
            }
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("There is no employee with given ID"));
        }
        return null;
    }

    @GetMapping("/search-by-position/{position}")
    public ResponseEntity<?> searchByPosition(@PathVariable String position){

        ArrayList<Employee> positionEmployee=new ArrayList<>();

        if(!position.equalsIgnoreCase("supervisor")&&!position.equalsIgnoreCase("coordinator")){
            return ResponseEntity.status(400).body(new ApiResponse("You must choose either supervisor or coordinator"));
        }
        for(Employee employee:employees){
            if (employee.getPosition().equalsIgnoreCase(position)) {
                positionEmployee.add(employee);
            }
        }
        if(positionEmployee.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("there is no employee with the given position"));

        }
        return ResponseEntity.status(200).body(positionEmployee);
    }

    @GetMapping("/search-by-age-range/{min}/{max}")
    public ResponseEntity<?> searchByAgeRange(@PathVariable int min,@PathVariable int max) {

        ArrayList<Employee> employeesAgeRange = new ArrayList<>();

        if (min >= max) {
            return ResponseEntity.status(400).body(new ApiResponse("min age must be less then max age"));
        }
        for (Employee employee : employees) {
            if (employee.getAge()>=min && employee.getAge()<=max){
                employeesAgeRange.add(employee);
            }
        }
        if(employeesAgeRange.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("there is no employee with the given age range"));
        }
        return ResponseEntity.status(200).body(employeesAgeRange);
    }

    @PutMapping("/apply-for-annule-leave/{ID}")
    public ResponseEntity<?> applyAnnualLeave(@PathVariable String ID){
        boolean foundEmployee=false;
        for (Employee employee:employees){
            if(employee.getID().equalsIgnoreCase(ID)){
                foundEmployee=true;
                if (employee.getAnnualLeave()==0){
                    return ResponseEntity.status(400).body(new ApiResponse("You don't have any annual leave left"));
                }
                employee.setAnnualLeave(1-employee.getAnnualLeave());
                return ResponseEntity.status(200).body(new ApiResponse("You got your annual leave"));
            }
        }
        if (!foundEmployee){
            return ResponseEntity.status(400).body(new ApiResponse("There is no employee with the given ID"));
        }
        return null;
    }

    @GetMapping("/get-employee-with-annual-leave")
    public ResponseEntity<?> searchByAnnualLeave(){
        ArrayList<Employee> employeesWithNoAnnualLeave=new ArrayList<>();

        for (Employee employee:employees){
            if(employee.getAnnualLeave()==0){
                employeesWithNoAnnualLeave.add(employee);
            }
        }
        if(employeesWithNoAnnualLeave.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("There is no employee with no annual leave"));
        }
        return ResponseEntity.status(200).body(employeesWithNoAnnualLeave);
    }

    @PutMapping("/promote-employee/{supervisorID}/{coordinatorID}")
    public ResponseEntity<?> promoteEmployee(@PathVariable String supervisorID,@PathVariable String coordinatorID){
        boolean supervisorExist=false;
        boolean coordinatorExist=false;
        for (Employee employee:employees){
            if(employee.getID().equalsIgnoreCase(supervisorID)&&employee.getPosition().equalsIgnoreCase("supervisor")){
                supervisorExist=true;
            }
            if(employee.getID().equalsIgnoreCase(coordinatorID)&&employee.getPosition().equalsIgnoreCase("coordinator")){
                coordinatorExist=true;
            }
        }
        if(!supervisorExist){
            return ResponseEntity.status(400).body(new ApiResponse("Supervisor not found"));
        }
        if(!coordinatorExist){
            return ResponseEntity.status(400).body(new ApiResponse("Coordinator not found"));
        }
        for (Employee employee:employees){
            if(employee.getID().equalsIgnoreCase(coordinatorID)){
                employee.setPosition("supervisor");
                return ResponseEntity.status(200).body(new ApiResponse("coordinator has been promoted successfully"));
            }
        }
        return null;
    }


}
