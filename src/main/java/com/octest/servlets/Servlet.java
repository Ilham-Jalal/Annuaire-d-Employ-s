package com.octest.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/employees")
public class Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private List<Employee> employees = new ArrayList<>();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        
        if (action == null) {
            action = "list";
        }

        switch (action) {
        case "list":
            listEmployees(request, response);
            break;
        case "showForm":
            showEmployeeForm(request, response);
            break;
        case "add":
            addEmployee(request, response);
            break;
        case "edit":
            showEditForm(request, response);
            break;
        case "update":
            updateEmployee(request, response);
            break;
        case "delete":
            deleteEmployee(request, response);
            break;
        default:
            listEmployees(request, response);
        }
    }

    private void listEmployees(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("employees", employees);
        request.getRequestDispatcher("/WEB-INF/list-employees.jsp").forward(request, response);
    }

    private void showEmployeeForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/employee-form.jsp").forward(request, response);
    }

    private void addEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String department = request.getParameter("department");
        String designation = request.getParameter("designation");
        double salary = Double.parseDouble(request.getParameter("salary"));

        Employee employee = new Employee(id, name, department, designation, salary);
        employees.add(employee);

        response.sendRedirect("employees?action=list");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Employee employee = findEmployeeById(id);
        request.setAttribute("employee", employee);
        request.getRequestDispatcher("/WEB-INF/edit-employee.jsp").forward(request, response);
    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String department = request.getParameter("department");
        String designation = request.getParameter("designation");
        double salary = Double.parseDouble(request.getParameter("salary"));

        Employee updatedEmployee = new Employee(id, name, department, designation, salary);
        updatedEmployee.setId(id);

        // Update the employee in the list
        for (Employee emp : employees) {
            if (emp.getId() == id) {
                emp.setName(updatedEmployee.getName());
                emp.setDepartment(updatedEmployee.getDepartment());
                emp.setDesignation(updatedEmployee.getDesignation());
                emp.setSalary(updatedEmployee.getSalary());
                break;
            }
        }

        response.sendRedirect("employees?action=list");
    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        // Remove the employee from the list
        employees.removeIf(emp -> emp.getId() == id);

        response.sendRedirect("employees?action=list");
    }

    private Employee findEmployeeById(int id) {
        for (Employee emp : employees) {
            if (emp.getId() == id) {
                return emp;
            }
        }
        return null;
    }
}
