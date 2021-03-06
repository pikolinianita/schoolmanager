package apt.project.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import apt.project.backend.domain.Course;
import apt.project.backend.domain.Exam;
import apt.project.backend.domain.Student;

public class CourseRepositoryTest {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private CourseRepository courseRepository;
    private static TransactionManager<Course> transactionManager;

    @BeforeClass
    public static void setUpClass() throws Exception {
        entityManagerFactory = Persistence
                .createEntityManagerFactory("H2_TEST_BACKEND");
        transactionManager = new TransactionManager<>(entityManagerFactory);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        entityManagerFactory.close();
    }

    @Before
    public void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("DELETE FROM Exam").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM Course").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM Student").executeUpdate();
        entityManager.getTransaction().commit();
        courseRepository = new CourseRepository(transactionManager);

    }

    @After
    public void tearDown() {
        entityManager.clear();
        entityManager.close();
    }

    @Test
    public void testFindAllWhenDataBaseIsEmpty() throws RepositoryException {
        // exercise and verify
        assertThat(courseRepository.findAll()).isEmpty();
    }

    @Test
    public void testFindAllWhenDataBaseIsNotEmpty() throws RepositoryException {
        // setUp
        Course course1 = new Course("Course1");
        Course course2 = new Course("Course2");
        entityManager.getTransaction().begin();
        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.getTransaction().commit();
        // exercise and verify
        assertThat(courseRepository.findAll()).containsExactly(course1,
                course2);
    }

    @Test
    public void testFindByTitleWhenCourseExist() throws RepositoryException {
        // setUp
        Course course1 = new Course("Course1");
        Course course2 = new Course("Course2");
        entityManager.getTransaction().begin();
        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.getTransaction().commit();
        // exercise and verify
        assertThat(courseRepository.findByTitle("Course1")).isEqualTo(course1);
    }

    @Test
    public void testFindByTitleWhenTheCourseDoesNotExist()
            throws RepositoryException {
        // exercise and verify
        assertThat(courseRepository.findByTitle("Course1")).isNull();
    }

    @Test
    public void testSave() throws RepositoryException {
        // setup
        Course course = new Course("Course1");
        // exercise
        courseRepository.save(course);
        // verify
        entityManager.getTransaction().begin();
        List<Course> courses = entityManager
                .createQuery("from Course", Course.class).getResultList();
        entityManager.getTransaction().commit();
        assertThat(courses).containsExactly(course);
    }

    @Test
    public void testDelete() throws RepositoryException {
        // setup
        Course courseToDelete = new Course("Course1");
        entityManager.getTransaction().begin();
        entityManager.persist(courseToDelete);
        entityManager.getTransaction().commit();
        // exercise
        courseRepository.delete(courseToDelete);
        // verify
        entityManager.getTransaction().begin();
        List<Course> courses = entityManager
                .createQuery("from Course", Course.class).getResultList();
        entityManager.getTransaction().commit();
        assertThat(courses).isEmpty();
    }

    @Test
    public void testUpdate() throws RepositoryException {
        // setup
        Course existingCourse = new Course("Course1");
        entityManager.getTransaction().begin();
        entityManager.persist(existingCourse);
        entityManager.getTransaction().commit();
        entityManager.clear(); // this is done because otherwise Hibernate would
                               // update the entity automatically.
        existingCourse.setTitle("Course1Modified");
        // exercise
        courseRepository.update(existingCourse);
        // verify
        entityManager.getTransaction().begin();
        Course retrievedCourse = entityManager.find(Course.class,
                existingCourse.getId());
        entityManager.getTransaction().commit();

        assertThat(retrievedCourse)
                .isEqualToComparingFieldByField(existingCourse);
    }

    @Test
    public void testFindById() throws RepositoryException {
        // setup
        Course course = new Course("Course1");
        entityManager.getTransaction().begin();
        entityManager.persist(course);
        entityManager.getTransaction().commit();
        // exercise
        Course retrievedCourse = courseRepository.findById(course.getId());
        // verify
        assertThat(course).isEqualTo(retrievedCourse);
    }

    @Test
    public void testWhenCourseIsDeletedThenRelatedExamsAreAlsoDeleted()
            throws RepositoryException {

        // setup
        Course course = new Course("Course");
        Course course1 = new Course("Course1");

        Student student = new Student("student");
        Exam exam = new Exam(course, 23);
        Exam exam1 = new Exam(course1, 29);
        student.addExam(exam);
        student.addExam(exam1);
        entityManager.getTransaction().begin();
        entityManager.persist(course);
        entityManager.persist(course1);
        entityManager.persist(student);
        entityManager.getTransaction().commit();

        // exercise
        courseRepository.delete(course);
        entityManager.clear();

        // verify
        entityManager.getTransaction().begin();
        Student retrievedStudent = entityManager
                .createQuery("from Student", Student.class).getResultList()
                .get(0);

        List<Exam> retrievedExams = entityManager
                .createQuery("from Exam", Exam.class).getResultList();
        entityManager.getTransaction().commit();

        assertThat(retrievedExams).hasSize(1);
        assertThat(retrievedExams.get(0)).isEqualTo(exam1);

        assertThat(retrievedStudent.getExams()).hasSize(1);
        assertThat(retrievedStudent.getExams().get(0)).isEqualTo(exam1);
    }
}
