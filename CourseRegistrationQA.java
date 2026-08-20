import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CourseRegistrationQA {
    private CourseRegistration student;

    @BeforeEach
    public void setUp() {
        student = new CourseRegistration("S101", "CS", 3, 10); // Limit: 10 Credits
    }

    @Test
    public void testValidRegistration() {
        assertTrue(student.registerCourse("Programming"));
        assertEquals(4, student.calculateTotalCredits());
    }

    @Test
    public void testMissingPrerequisite() {
        // DBMS requires Programming
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            student.registerCourse("DBMS");
        });
        assertTrue(exception.getMessage().contains("Missing prerequisite"));
    }

    @Test
    public void testCreditLimitViolation() {
        student.registerCourse("Programming"); // 4 credits
        student.registerCourse("Networking");  // 3 credits (Total: 7)
        student.addCompletedCourse("Programming");
        
        // Adding DBMS (4 credits) will total 11 > Limit of 10
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            student.registerCourse("DBMS");
        });
        assertTrue(exception.getMessage().contains("Credit-limit violation"));
    }

    @Test
    public void testTimetableConflict() {
        student.addCompletedCourse("Data Structures");
        student.registerCourse("Programming"); // Mon 09:00-10:00

        // AI is at the same time slot as Programming (Mon 09:00-10:00)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            student.registerCourse("AI");
        });
        assertTrue(exception.getMessage().contains("Timetable conflict"));
    }

    @Test
    public void testFullCourse() {
        student.addCompletedCourse("Statistics");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            student.registerCourse("ML"); // Capacity is 0
        });
        assertTrue(exception.getMessage().contains("Course is full"));
    }

    @Test
    public void testDuplicateRegistration() {
        student.registerCourse("Programming");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            student.registerCourse("Programming");
        });
        assertTrue(exception.getMessage().contains("Duplicate registration"));
    }

    @Test
    public void testInvalidCourse() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            student.registerCourse("INVALID_101");
        });
        assertTrue(exception.getMessage().contains("Invalid course code"));
    }
}
