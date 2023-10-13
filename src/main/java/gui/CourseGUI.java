package gui;

import domain.Course;
import dao.CourseDaoInterface;
import dao.impl.CourseDao;
import domain.User;
import gui.sub.UserCourses;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Slf4j
public class CourseGUI extends JFrame {
    private final User user;
    private final CourseDaoInterface courseDao;
    private List<Course> courseList;

    public CourseGUI(User user) {
        this.setTitle("All Courses");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
        this.user = user;
        this.courseDao = new CourseDao();
        this.courseList = courseDao.getAllCourses();

        // Create a main panel to hold everything
        JPanel mainPanel = new JPanel();
        JPanel topPanel = new JPanel();

        topPanel.setLayout(new BorderLayout());
        mainPanel.setLayout(new BorderLayout());

        // Create a "Back" button and add it to the top-left corner
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Dialog", Font.BOLD, 15));

        JButton userCoursesButton = new JButton("Check Enrolled Courses");
        userCoursesButton.setFont(new Font("Dialog", Font.BOLD, 15));

        userCoursesButton.addActionListener(e -> {
            log.info("Ready to check " + user.getName() + "'s enrolled courses");
            CourseGUI.this.dispose();
            new UserCourses(user);
        });

        backButton.addActionListener(e -> {
            CourseGUI.this.dispose();
            new MainGUI(user);
        });
        topPanel.add(backButton, BorderLayout.WEST);

        if (!user.isAdmin()){
            topPanel.add(userCoursesButton, BorderLayout.EAST);
        }

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JScrollPane js = new JScrollPane(addCourseList(courseList), ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(js, BorderLayout.CENTER);

        this.setContentPane(mainPanel);
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
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 0)); // Add padding

        JButton checkButton = new JButton("Check");
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("Checking Button Clicked. Redirecting to " + course.getCourseName() + "'s Detail");
                CourseGUI.this.dispose();
                new CourseDetailGUI(course, user);
            }
        });

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(nameLabel, BorderLayout.WEST);
        contentPanel.add(checkButton, BorderLayout.EAST);

        coursePanel.add(contentPanel, BorderLayout.CENTER);

        return coursePanel;
    }
}