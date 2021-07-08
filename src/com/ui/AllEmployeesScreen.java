package com.ui;

import com.entities.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AllEmployeesScreen extends JFrame {
    private JPanel panelAllEmployees;
    private JScrollPane scrollPane;
    private final HomeScreen parent;

    public AllEmployeesScreen(HomeScreen parent) {
        this.parent = parent;
        JTable table = new JTable();
        String[] columnNames = {"ID", "Name", "DOB", "Address", "Email", "Phone", "Place of work"};
        List<Employee> data = HomeScreen.parseXML();
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Employee e : data) {
            Object[] tableData = {e.getID(), e.getName(), e.getDOB(), e.getAddress(), e.getEmail(), e.getPhone(),
                    e.getPlaceOfWork()};
            tableModel.addRow(tableData);
        }
        table.setModel(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                Employee f = new Employee();
                f.setID(table.getValueAt(table.getSelectedRow(), 0).toString());
                f.setName(table.getValueAt(table.getSelectedRow(), 1).toString());
                f.setDOB(table.getValueAt(table.getSelectedRow(), 2).toString());
                f.setAddress(table.getValueAt(table.getSelectedRow(), 3).toString());
                f.setEmail(table.getValueAt(table.getSelectedRow(), 4).toString());
                f.setPhone(table.getValueAt(table.getSelectedRow(), 5).toString());
                f.setPlaceOfWork(table.getValueAt(table.getSelectedRow(), 6).toString());
                this.parent.loadEmployee(f);
            }
        });
        scrollPane.setViewportView(table);
        this.setContentPane(panelAllEmployees);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("All Employees");
        this.pack();
        this.setLocationRelativeTo(null);
    }

}
