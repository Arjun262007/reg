import java.util.*;

public class CourseRegistration {
    private final String studentId;
    private final String program;
    private final int semester;
    private final int maxCreditLimit;
    
    private final Map<String, Course> courseCatalog = new HashMap<>();
    private final Set<String> completedCourses = new HashSet<>();
    private final Set<String> registeredCourses = new HashSet<>();

    public CourseRegistration(String studentId, String program, int semester, int maxCreditLimit) {
        this.studentId = studentId;
        this.program = program;
        this.semester = semester;
        this.maxCreditLimit = maxCreditLimit;
        
        // Initialize Course Catalog according to specification table
        courseCatalog.put("Programming", new Course("Programming", 4, null, 30, "Mon 09:00-10:00"));
        courseCatalog.put("Data Structures", new Course("Data Structures", 4, "Programming", 25, "Tue 10:00-11:00"));
        courseCatalog.put("Statistics", new Course("Statistics", 3, null, 20, "Wed 11:00-12:00"));
        courseCatalog.put("Networking", new Course("Networking", 3, null, 15, "Thu 02:00-03:00"));
        courseCatalog.put("DBMS", new Course("DBMS", 4, "Programming", 30, "Mon 10:00-11:00"));
        courseCatalog.put("AI", new Course("AI", 4, "Data Structures", 2, "Mon 09:00-10:00")); // Clashes with Programming
        courseCatalog.put("ML", new Course("ML", 3, "Statistics", 0, "Fri 09:00-10:00"));      // Full capacity
        courseCatalog.put("Cloud", new Course("Cloud", 3, "Networking", 20, "Thu 02:00-03:00"));  // Clashes with Networking
    }

    public void addCompletedCourse(String courseCode) {
        completedCourses.add(courseCode);
    }

    public int calculateTotalCredits() {
        int total = 0;
        for (String code : registeredCourses) {
            total += courseCatalog.get(code).credits;
        }
        return total;
    }

    public boolean registerCourse(String courseCode) {
        // 1. Invalid Course Check
        if (!courseCatalog.containsKey(courseCode)) {
            throw new IllegalArgumentException("Invalid course code");
        }

        Course course = courseCatalog.get(courseCode);

        // 2. Duplicate Registration Check
        if (registeredCourses.contains(courseCode)) {
            throw new IllegalArgumentException("Duplicate registration detected");
        }

        // 3. Prerequisite Check
        if (course.prerequisite != null && !completedCourses.contains(course.prerequisite)) {
            throw new IllegalArgumentException("Missing prerequisite: " + course.prerequisite + " required");
        }

        // 4. Credit Limit Check
        if (calculateTotalCredits() + course.credits > maxCreditLimit) {
            throw new IllegalArgumentException("Credit-limit violation");
        }

        // 5. Timetable Clash Check
        for (String regCode : registeredCourses) {
            Course registeredCourse = courseCatalog.get(regCode);
            if (registeredCourse.timeSlot.equals(course.timeSlot)) {
                throw new IllegalArgumentException("Timetable conflict with " + regCode);
            }
        }

        // 6. Course Capacity Check
        if (course.capacity <= 0) {
            throw new IllegalArgumentException("Course is full");
        }

        // Successfully Register
        registeredCourses.add(courseCode);
        return true;
    }
}
