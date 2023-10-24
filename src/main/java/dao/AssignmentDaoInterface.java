package dao;

import domain.Assignment;

import java.util.List;

public interface AssignmentDaoInterface {
    boolean doesAssignmentExist(Assignment assignment);

    boolean insertAssignment(Assignment assignment);

    boolean updateAssignment(Assignment assignment);

    boolean deleteAssignment(Assignment assignment);

    List<String> getAssignmentNamesByCourseID(int courseID);

    Assignment getAssignmentByAssignmentAndCourseName(String assignmentName, String courseName);
}
