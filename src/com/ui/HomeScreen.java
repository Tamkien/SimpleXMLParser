package com.ui;

import com.entities.Employee;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.swing.*;
import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeScreen extends JFrame {
    private static final String fileName = "out/production/PRX/com/res/employee.xml";
    static JFrame frame;
    private JPanel panelHome;
    private JTextField tfID;
    private JButton btnAdd;
    private JTextField tfName;
    private JButton btnUpdate;
    private JTextField tfDOB;
    private JButton btnDelete;
    private JTextField tfPlaceOfWork;
    private JTextField tfAddress;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JButton btnSearch;
    private JButton btnShow;
    private JButton btnExit;

    public HomeScreen() {
        btnAdd.addActionListener(e -> {
            Employee employee = new Employee(tfID.getText(), tfName.getText(), tfDOB.getText(), tfAddress.getText(),
                    tfEmail.getText(),
                    tfPhone.getText(), tfPlaceOfWork.getText());
            List<Employee> employees = parseXML();
            if (valid(employee)) {
                boolean existedID = false;
                for (Employee emp : employees) {
                    if (emp.getID().equals(employee.getID())) {
                        JOptionPane.showMessageDialog(panelHome, "ID already exist! Insert another ID or choose " +
                                        "Update.",
                                "Warning", JOptionPane.WARNING_MESSAGE);
                        existedID = true;
                        break;
                    }
                    existedID = false;
                }
                if (!existedID) try {
                    addEmployee(employee);
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(panelHome, "Error!",
                            "Error", JOptionPane.WARNING_MESSAGE);
                    ioException.printStackTrace();
                }
            } else JOptionPane.showMessageDialog(panelHome, "Invalid Information!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        });
        btnUpdate.addActionListener(e -> {
            Employee employee = new Employee(tfID.getText(), tfName.getText(), tfDOB.getText(), tfAddress.getText(),
                    tfEmail.getText(),
                    tfPhone.getText(), tfPlaceOfWork.getText());
            if (valid(employee)) updateEmployee(employee);
            else JOptionPane.showMessageDialog(panelHome, "Invalid Information!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        });
        btnDelete.addActionListener(e -> deleteEmployee(tfID.getText()));
        btnSearch.addActionListener(e -> searchEmployee(tfID.getText()));
        btnShow.addActionListener(e -> showAllEmployees());
        btnExit.addActionListener(e -> exit());
    }

    public static void main(String[] args) {
        frame = new JFrame("Employee Management");
        frame.setContentPane(new HomeScreen().panelHome);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static List<Employee> parseXML() {
        List<Employee> empList = new ArrayList<>();
        Employee employee = null;
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(fileName));
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    switch (startElement.getName().getLocalPart()) {
                        case "employee":
                            employee = new Employee();
                            Attribute idAttr = startElement.getAttributeByName(new QName("employee_no"));
                            if (idAttr != null)
                                employee.setID(idAttr.getValue());
                            break;
                        case "name":
                            xmlEvent = xmlEventReader.nextEvent();
                            assert employee != null;
                            employee.setName(xmlEvent.asCharacters().getData());
                            break;
                        case "dob":
                            xmlEvent = xmlEventReader.nextEvent();
                            assert employee != null;
                            employee.setDOB(xmlEvent.asCharacters().getData());
                            break;
                        case "address":
                            xmlEvent = xmlEventReader.nextEvent();
                            assert employee != null;
                            employee.setAddress(xmlEvent.asCharacters().getData());
                            break;
                        case "email":
                            xmlEvent = xmlEventReader.nextEvent();
                            assert employee != null;
                            employee.setEmail(xmlEvent.asCharacters().getData());
                            break;
                        case "phone_no":
                            xmlEvent = xmlEventReader.nextEvent();
                            assert employee != null;
                            employee.setPhone(xmlEvent.asCharacters().getData());
                            break;
                        case "place_of_work":
                            xmlEvent = xmlEventReader.nextEvent();
                            assert employee != null;
                            employee.setPlaceOfWork(xmlEvent.asCharacters().getData());
                            break;
                    }
                }
                if (xmlEvent.isEndElement()) {
                    EndElement endElement = xmlEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals("employee")) {
                        empList.add(employee);
                    }
                }
            }

        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
        return empList;
    }

    public void loadEmployee(Employee employee) {
        tfID.setText(employee.getID());
        tfName.setText(employee.getName());
        tfAddress.setText(employee.getAddress());
        tfEmail.setText(employee.getEmail());
        tfPhone.setText(employee.getPhone());
        tfPlaceOfWork.setText(employee.getPlaceOfWork());
        tfDOB.setText(employee.getDOB());
        this.repaint();
    }

    private boolean valid(Employee employee) {
        tfID.setBackground(employee.validID() ? Color.white : Color.pink);
        tfName.setBackground(employee.validName() ? Color.white : Color.pink);
        tfDOB.setBackground(employee.validDOB() ? Color.white : Color.pink);
        tfAddress.setBackground(employee.validAddress() ? Color.white : Color.pink);
        tfEmail.setBackground(employee.validEmail() ? Color.white : Color.pink);
        tfPhone.setBackground(employee.validPhone() ? Color.white : Color.pink);
        tfPlaceOfWork.setBackground(employee.validPlaceOfWork() ? Color.white : Color.pink);
        return employee.validID() && employee.validName() && employee.validDOB() && employee.validAddress() && employee.validEmail() && employee.validPhone() && employee.validPlaceOfWork();
    }

    private void addEmployee(Employee employee) throws IOException {
        Path in = Paths.get(fileName);
        Path temp = Files.createTempFile(null, null);
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        try (FileWriter out = new FileWriter(temp.toFile())) {
            XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(new FileReader(in.toFile()));
            XMLEventWriter writer = XMLOutputFactory.newInstance().createXMLEventWriter(out);
            int depth = 0;
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                int eventType = event.getEventType();
                if (eventType == XMLStreamConstants.START_ELEMENT) {
                    depth++;
                } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                    depth--;
                    if (depth == 0) {
                        List<Attribute> attrs = new ArrayList<>(1);
                        attrs.add(eventFactory.createAttribute("employee_no", employee.getID()));
                        writer.add(eventFactory.createStartElement("", null, "employee", attrs.iterator(), null));
                        writer.add(eventFactory.createSpace(System.getProperty("line.separator")));
                        writer.add(eventFactory.createStartElement("", null, "name", null, null));
                        writer.add(eventFactory.createCharacters(employee.getName()));
                        writer.add(eventFactory.createEndElement("", null, "name"));
                        writer.add(eventFactory.createSpace(System.getProperty("line.separator")));
                        writer.add(eventFactory.createStartElement("", null, "dob", null, null));
                        writer.add(eventFactory.createCharacters(employee.getDOB()));
                        writer.add(eventFactory.createEndElement("", null, "dob"));
                        writer.add(eventFactory.createSpace(System.getProperty("line.separator")));
                        writer.add(eventFactory.createStartElement("", null, "address", null, null));
                        writer.add(eventFactory.createCharacters(employee.getAddress()));
                        writer.add(eventFactory.createEndElement("", null, "address"));
                        writer.add(eventFactory.createSpace(System.getProperty("line.separator")));
                        writer.add(eventFactory.createStartElement("", null, "email", null, null));
                        writer.add(eventFactory.createCharacters(employee.getEmail()));
                        writer.add(eventFactory.createEndElement("", null, "email"));
                        writer.add(eventFactory.createSpace(System.getProperty("line.separator")));
                        writer.add(eventFactory.createStartElement("", null, "phone_no", null, null));
                        writer.add(eventFactory.createCharacters(employee.getPhone()));
                        writer.add(eventFactory.createEndElement("", null, "phone_no"));
                        writer.add(eventFactory.createSpace(System.getProperty("line.separator")));
                        writer.add(eventFactory.createStartElement("", null, "place_of_work", null, null));
                        writer.add(eventFactory.createCharacters(employee.getPlaceOfWork()));
                        writer.add(eventFactory.createEndElement("", null, "place_of_work"));
                        writer.add(eventFactory.createSpace(System.getProperty("line.separator")));
                        writer.add(eventFactory.createEndElement("", null, "employee"));
                        writer.add(eventFactory.createSpace(System.getProperty("line.separator")));
                    }
                }
                writer.add(event);
            }
            writer.flush();
            writer.close();
            JOptionPane.showMessageDialog(panelHome, "Added to XML", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (XMLStreamException | FactoryConfigurationError e) {
            throw new IOException(e);
        }
        Files.move(temp, in, StandardCopyOption.REPLACE_EXISTING);
    }

    private void updateEmployee(Employee employee) {
        boolean foundEmployee = false;
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(
                    new FileReader(fileName));
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(new File(fileName));
            Element rootElement = document.getRootElement();
            List<Element> employeeElement = rootElement.getChildren("employee");

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();

                    if (qName.equals("employee")) {
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        String employeeID = attributes.next().getValue();

                        if (employeeID.equals(employee.getID())) {
                            foundEmployee = true;
                            for (Element element : employeeElement) {
                                if (element.getAttribute(
                                        "employee_no").getValue().equals(employee.getID())) {
                                    element.removeChild("name");
                                    element.addContent(new Element("name").setText(employee.getName()));
                                    element.removeChild("dob");
                                    element.addContent(new Element("dob").setText(employee.getDOB()));
                                    element.removeChild("address");
                                    element.addContent(new Element("address").setText(employee.getAddress()));
                                    element.removeChild("email");
                                    element.addContent(new Element("email").setText(employee.getEmail()));
                                    element.removeChild("phone_no");
                                    element.addContent(new Element("phone_no").setText(employee.getPhone()));
                                    element.removeChild("place_of_work");
                                    element.addContent(new Element("place_of_work").setText(employee.getPlaceOfWork()));
                                }
                            }
                        }
                    }
                }
            }
            XMLOutputter xmlOutput = new XMLOutputter();

            // display xml
            xmlOutput.setFormat(Format.getPrettyFormat());
            try (FileOutputStream output = new FileOutputStream(fileName)) {
                xmlOutput.output(document, output);
            }
            if (foundEmployee)
                JOptionPane.showMessageDialog(panelHome, "Updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
            else notFoundEmployee();

        } catch (XMLStreamException | JDOMException | IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteEmployee(String ID) {

        boolean foundEmployee = false;
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(
                    new FileReader(fileName));
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(new File(fileName));
            Element rootElement = document.getRootElement();
            List<Element> employeeElement = rootElement.getChildren("employee");

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();

                    if (qName.equals("employee")) {
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        String employeeID = attributes.next().getValue();

                        if (employeeID.equals(ID)) {
                            foundEmployee = true;
                            for (Element element : employeeElement) {
                                if (element.getAttribute(
                                        "employee_no").getValue().equals(ID)) {
                                    element.detach();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            XMLOutputter xmlOutput = new XMLOutputter();

            // display xml
            xmlOutput.setFormat(Format.getPrettyFormat());
            try (FileOutputStream output = new FileOutputStream(fileName)) {
                xmlOutput.output(document, output);
            }
            if (foundEmployee)
                JOptionPane.showMessageDialog(panelHome, "Deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
            else notFoundEmployee();

        } catch (XMLStreamException | JDOMException | IOException e) {
            e.printStackTrace();
        }
    }

    private void searchEmployee(String employeeID) {
        List<Employee> employees = parseXML();
        Employee foundEmployee = new Employee();
        for (Employee employee : employees) if (employee.getID().equals(employeeID)) foundEmployee = employee;
        if (foundEmployee.getID() == null) notFoundEmployee();
        else showEmployee(foundEmployee);
    }

    private void showEmployee(Employee foundEmployee) {
        tfID.setText(foundEmployee.getID());
        tfName.setText(foundEmployee.getName());
        tfDOB.setText(foundEmployee.getDOB());
        tfAddress.setText(foundEmployee.getAddress());
        tfEmail.setText(foundEmployee.getEmail());
        tfPhone.setText(foundEmployee.getPhone());
        tfPlaceOfWork.setText(foundEmployee.getPlaceOfWork());
    }

    private void notFoundEmployee() {
        JOptionPane.showMessageDialog(panelHome, "Cannot find any employee by that ID!",
                "Warning", JOptionPane.WARNING_MESSAGE);
    }

    private void showAllEmployees() {
        new AllEmployeesScreen(this).setVisible(true);
    }

    private void exit() {
        System.exit(0);
    }
}
