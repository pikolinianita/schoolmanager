package apt.project.frontend.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import apt.project.backend.domain.Course;
import apt.project.backend.repository.CourseRepository;
import apt.project.backend.repository.RepositoryException;
import apt.project.backend.repository.TransactionManager;
import apt.project.frontend.view.View;

public class CourseControllerIT {

    @Mock
    private View<Course> courseView;

    private CourseController courseController;
    private CourseRepository courseRepository;
    private EntityManager entityManager;
    private static EntityManagerFactory entityManagerFactory;
    private static TransactionManager<Course> transactionManager;

    @BeforeClass
    public static void setUpClass() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("H2");
        transactionManager = new TransactionManager<>(entityManagerFactory);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        entityManagerFactory.close();
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        entityManager = entityManagerFactory.createEntityManager();
        courseRepository = new CourseRepository(transactionManager);
        transactionManager = new TransactionManager<>(entityManagerFactory);
        courseRepository = new CourseRepository(transactionManager);
        for (Course course : courseRepository.findAll()) {
            courseRepository.delete(course);
        }
        courseController = new CourseController(courseView, courseRepository);
    }

    @Test
    public void testAllEntities() throws RepositoryException {
        // setup
        Course course = new Course("Course_1");
        courseRepository.save(course);
        // exercise
        courseController.allEntities();
        // verify
        verify(courseView).showAll(asList(course));
    }

    @Test
    public void testNewEntity() throws RepositoryException {
        // setup
        Course course = new Course("Course_1");
        // exercise
        courseController.newEntity(course);
        // verify
        verify(courseView).entityAdded(course);
    }

    @Test
    public void testDeleteEntity() throws RepositoryException {
        // setup
        Course course = new Course("Course_1");
        courseRepository.save(course);
        // exercise
        courseController.deleteEntity(course);
        // verify
        verify(courseView).entityDeleted(course);
    }

    @Test
    public void testUpdateEntityWhenEntityExists() throws RepositoryException {
        // setup
        Course existingCourse = new Course("existingTitle");
        Course modifiedCourse = new Course("modifiedTitle");
        courseRepository.save(existingCourse);
        // exercise
        courseController.updateEntity(modifiedCourse);
        // verify
        verify(courseView).entityUpdated(modifiedCourse);
    }

}
