package apt.project.frontend.controller;

import apt.project.backend.domain.Course;
import apt.project.backend.repository.CourseRepository;
import apt.project.frontend.view.View;

public class CourseController extends BaseController<Course> {

    public CourseController(View<Course> view, CourseRepository repository) {
        super(view, repository);
    }

    @Override
    public void newEntity(Course course) {
        Course existingCourse;

        existingCourse = ExceptionManager
                .catcher(() -> ((CourseRepository) repository)
                        .findByTitle(course.getTitle()), view, course);
        if (existingCourse != null) {
            view.showError("Already existing entity: " + course, course);
            return;
        }
        super.newEntity(course);
    }

}
