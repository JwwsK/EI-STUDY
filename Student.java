import java.util.*;

// Class representing a Student
class Student {
    private String id;
    private List<String> submittedAssignments;

    public Student(String id) {
        this.id = id;
        this.submittedAssignments = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void submitAssignment(String assignment) {
        submittedAssignments.add(assignment);
    }

    public List<String> getSubmittedAssignments() {
        return submittedAssignments;
    }
}

// Class representing a Classroom
class Classroom {
    private String name;
    private List<Student> students;
    private List<String> assignments;

    public Classroom(String name) {
        this.name = name;
        this.students = new ArrayList<>();
        this.assignments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void scheduleAssignment(String assignment) {
        assignments.add(assignment);
    }

    public boolean isAssignmentScheduled(String assignment) {
        return assignments.contains(assignment);
    }
}

// Class representing the Virtual Classroom Manager
class VirtualClassroomManager {
    private Map<String, Classroom> classrooms;

    public VirtualClassroomManager() {
        this.classrooms = new HashMap<>();
    }

    public void addClassroom(String className) {
        if (!classrooms.containsKey(className)) {
            classrooms.put(className, new Classroom(className));
            System.out.println("Classroom " + className + " has been created.");
        } else {
            System.out.println("Classroom " + className + " already exists.");
        }
    }

    public void addStudent(String studentId, String className) {
        Classroom classroom = classrooms.get(className);
        if (classroom != null) {
            classroom.addStudent(new Student(studentId));
            System.out.println("Student " + studentId + " has been enrolled in " + className + ".");
        } else {
            System.out.println("Classroom " + className + " does not exist.");
        }
    }

    public void scheduleAssignment(String className, String assignment) {
        Classroom classroom = classrooms.get(className);
        if (classroom != null) {
            classroom.scheduleAssignment(assignment);
            System.out.println("Assignment for " + className + " has been scheduled.");
        } else {
            System.out.println("Classroom " + className + " does not exist.");
        }
    }

    public void submitAssignment(String studentId, String className, String assignment) {
        Classroom classroom = classrooms.get(className);
        if (classroom != null) {
            if (classroom.isAssignmentScheduled(assignment)) {
                for (Student student : classroom.getStudents()) {
                    if (student.getId().equals(studentId)) {
                        student.submitAssignment(assignment);
                        System.out.println("Assignment submitted by Student " + studentId + " in " + className + ".");
                        return;
                    }
                }
                System.out.println("Student " + studentId + " is not enrolled in " + className + ".");
            } else {
                System.out.println("Assignment " + assignment + " is not scheduled for " + className + ".");
            }
        } else {
            System.out.println("Classroom " + className + " does not exist.");
        }
    }

    public void listClassrooms() {
        if (classrooms.isEmpty()) {
            System.out.println("No classrooms available.");
        } else {
            System.out.println("Classrooms:");
            for (String className : classrooms.keySet()) {
                System.out.println("- " + className);
            }
        }
    }

    public void listStudentsInClassroom(String className) {
        Classroom classroom = classrooms.get(className);
        if (classroom != null) {
            List<Student> students = classroom.getStudents();
            if (students.isEmpty()) {
                System.out.println("No students enrolled in " + className + ".");
            } else {
                System.out.println("Students in " + className + ":");
                for (Student student : students) {
                    System.out.println("- " + student.getId());
                }
            }
        } else {
            System.out.println("Classroom " + className + " does not exist.");
        }
    }
}

// Main class to run the Virtual Classroom Manager
public class VirtualClassroomManagerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VirtualClassroomManager manager = new VirtualClassroomManager();
        String command;

        System.out.println("Welcome to the Virtual Classroom Manager!");

        while (true) {
            System.out.print("Enter command (add_classroom, add_student, schedule_assignment, submit_assignment, list_classrooms, list_students, exit): ");
            command = scanner.nextLine();

            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the Virtual Classroom Manager.");
                break;
            } else if (command.startsWith("add_classroom")) {
                String className = command.split(" ", 2)[1];
                manager.addClassroom(className);
            } else if (command.startsWith("add_student")) {
                String[] parts = command.split(" ");
                if (parts.length == 3) {
                    String studentId = parts[1];
                    String className = parts[2];
                    manager.addStudent(studentId, className);
                } else {
                    System.out.println("Usage: add_student [student_id] [class_name]");
                }
            } else if (command.startsWith("schedule_assignment")) {
                String[] parts = command.split(" ", 3);
                if (parts.length == 3) {
                    String className = parts[1];
                    String assignment = parts[2];
                    manager.scheduleAssignment(className, assignment);
                } else {
                    System.out.println("Usage: schedule_assignment [class_name] [assignment_details]");
                }
            } else if (command.startsWith("submit_assignment")) {
                String[] parts = command.split(" ", 4);
                if (parts.length == 4) {
                    String studentId = parts[1];
                    String className = parts[2];
                    String assignment = parts[3];
                    manager.submitAssignment(studentId, className, assignment);
                } else {
                    System.out.println("Usage: submit_assignment [student_id] [class_name] [assignment_details]");
                }
            } else if (command.equalsIgnoreCase("list_classrooms")) {
                manager.listClassrooms();
            } else if (command.startsWith("list_students")) {
                String className = command.split(" ", 2)[1];
                manager.listStudentsInClassroom(className);
            } else {
                System.out.println("Unknown command. Please try again.");
            }
        }

        scanner.close();
    }
}
