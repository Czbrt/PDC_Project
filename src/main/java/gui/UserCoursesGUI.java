package gui;

import constants.ExportConstants;
import dao.CourseDaoInterface;
import dao.impl.CourseDao;
import domain.Course;
import domain.User;
import domain.enums.CourseDetailPageFrom;
import gui.sub.CourseDetailGUI;
import lombok.extern.slf4j.Slf4j;
import util.FrameUtils;
import util.MethodUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

@Slf4j
public class UserCoursesGUI extends JFrame{
    private final User user;
    private final CourseDaoInterface courseDao;
    private List<Course> courseList;

    public UserCoursesGUI(User user) {
        this.setTitle(user.getName() + "'s Courses");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
        this.user = user;
        this.courseDao = new CourseDao();
        this.courseList = courseDao.getCourseByUser(user);

        // Create a main panel to hold everything
        JPanel mainPanel = new JPanel();
        JPanel topPanel = new JPanel();

        topPanel.setLayout(new BorderLayout());
        mainPanel.setLayout(new BorderLayout());

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Dialog", Font.BOLD, 15));

        backButton.addActionListener(e -> {
            UserCoursesGUI.this.dispose();
            new CourseGUI(user);
        });

        ActionListener exportAction = e -> performExport();
        JButton exportButton = new JButton("Export My Course Info");
        exportButton.setFont(new Font("Dialog", Font.BOLD, 15));
        exportButton.addActionListener(exportAction);

        topPanel.add(exportButton, BorderLayout.EAST);
        topPanel.add(backButton, BorderLayout.WEST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JScrollPane js = new JScrollPane(addCourseList(courseList), ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(js, BorderLayout.CENTER);

        this.setContentPane(mainPanel);
    }

    private void performExport() {
        MethodUtil.exportCoursesToExcel(courseList, user);
        FrameUtils.showDialog("Export successfully! Please check the " + ExportConstants.EXPORT_COURSE_TO_PATH + " directory.");
    }

    public JPanel addCourseList(List<Course> courseList) {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new GridLayout(0, 1));

        for (Course course : courseList) {
            listPanel.add(addCourseItem(course));
        }
        if (courseList.size() < 6) {
            for (int i = 0; i < 6 - courseList.size(); i++) {
                listPanel.add(new JPanel());
            }
        }
        return listPanel;
    }

    private JPanel addCourseItem(Course course) {
        JPanel coursePanel = new JPanel();
        coursePanel.setLayout(new BorderLayout());

        JLabel nameLabel = new JLabel(course.getCourseName());
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 15));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 0));

        JButton checkButton = new JButton("Check");
        checkButton.addActionListener(e -> {
            log.info("Checking Button Clicked. Redirecting to " + course.getCourseName() + "'s Detail");
            UserCoursesGUI.this.dispose();
            new CourseDetailGUI(course, user, CourseDetailPageFrom.USER_COURSE_PAGE);
        });

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(nameLabel, BorderLayout.WEST);
        contentPanel.add(checkButton, BorderLayout.EAST);

        coursePanel.add(contentPanel, BorderLayout.CENTER);

        return coursePanel;
    }
}