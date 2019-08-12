package apt.project.frontend.controller;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import apt.project.backend.domain.Student;
import apt.project.backend.repository.RepositoryException;
import apt.project.backend.repository.StudentRepository;
import apt.project.frontend.view.View;

public class StudentControllerTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private View<Student> studentView;

    @InjectMocks
    private StudentController studentController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAllEntitiesShouldCallView() throws RepositoryException {
        // setup
        List<Student> students = asList(new Student("John"));
        when(studentRepository.findAll()).thenReturn(students);
        // exercise
        studentController.allEntities();
        // verify
        verify(studentView).showAll(students);
    }

    @Test
    public void testAllEntitiesWhenRepositoryExceptionIsThrownInFindAllShouldShowError()
            throws RepositoryException {
        // setup
        String message = "message";
        when(studentRepository.findAll())
                .thenThrow(new RepositoryException(message));
        // exercise
        studentController.allEntities();
        // verify
        verify(studentView).showError("Repository exception: " + message, null);
    }

    @Test
    public void testNewEntityShouldCallRepositoryAndView()
            throws RepositoryException {
        // setup
        Student student = new Student("John");
        when(studentRepository.findByName("John")).thenReturn(null);
        // exercise
        studentController.newEntity(student);
        // verify
        InOrder inOrder = inOrder(studentRepository, studentView);
        inOrder.verify(studentRepository).save(student);
        inOrder.verify(studentView).entityAdded(student);
    }

    @Test
    public void testNewEntityWhenRepositoryExceptionIsThrownInSaveShouldCallShowError()
            throws RepositoryException {
        // setup
        String message = "message";
        Student student = new Student("John");
        doThrow(new RepositoryException(message)).when(studentRepository)
                .save(student);
        // exercise
        studentController.newEntity(student);
        // verify
        verify(studentView).showError("Repository exception: " + message,
                student);
    }

    @Test
    public void testDeleteEntityWhenEntityExists() throws RepositoryException {
        // setup
        Student studentToDelete = new Student("John");
        when(studentRepository.findById((Long) any()))
                .thenReturn(studentToDelete);
        // exercise
        studentController.deleteEntity(studentToDelete);
        // verify
        InOrder inOrder = inOrder(studentRepository, studentView);
        inOrder.verify(studentRepository).delete(studentToDelete);
        inOrder.verify(studentView).entityDeleted(studentToDelete);
    }

    @Test
    public void testDeleteEntityWhenEntityDoesNotExist()
            throws RepositoryException {
        // setup
        Student studentToDelete = new Student("John");
        when(studentRepository.findById((Long) any())).thenReturn(null);
        // exercise
        studentController.deleteEntity(studentToDelete);
        // verify
        verify(studentView).showError("No existing entity: " + studentToDelete,
                studentToDelete);
    }

    @Test
    public void testDeleteEntityWhenRepositoryExceptionIsThrownInFindByIdShouldCallShowError()
            throws RepositoryException {
        // setup
        String message = "message";
        Student studentToDelete = new Student("John");
        when(studentRepository.findById((Long) any()))
                .thenThrow(new RepositoryException(message));
        // exercise
        studentController.deleteEntity(studentToDelete);
        // verify
        verify(studentView).showError("Repository exception: " + message,
                studentToDelete);
        verifyNoMoreInteractions(ignoreStubs(studentRepository));
    }

    @Test
    public void testDeleteEntityWhenRepositoryExceptionIsThrownInDeleteShouldCallShowError()
            throws RepositoryException {
        // setup
        String message = "message";
        Student studentToDelete = new Student("John");
        when(studentRepository.findById((Long) any()))
                .thenReturn(studentToDelete);
        doThrow(new RepositoryException(message)).when(studentRepository)
                .delete(studentToDelete);
        // exercise
        studentController.deleteEntity(studentToDelete);
        // verify
        verify(studentView).showError("Repository exception: " + message,
                studentToDelete);
        verifyNoMoreInteractions(ignoreStubs(studentRepository));
    }

    @Test
    public void testUpdateEntityWhenEntityExists() throws RepositoryException {
        // setup
        Student existingStudent = new Student("John");
        Student modifiedStudent = new Student("Jane");
        when(studentRepository.findById((Long) any()))
                .thenReturn(existingStudent);
        // exercise
        studentController.updateEntity(existingStudent, modifiedStudent);
        // verify
        InOrder inOrder = inOrder(studentRepository, studentView);
        inOrder.verify(studentRepository).update(modifiedStudent);
        inOrder.verify(studentView).entityUpdated(existingStudent,
                modifiedStudent);
    }

    @Test
    public void testUpdateEntityWhenEntityDoesNotExist()
            throws RepositoryException {
        // setup
        Student existingStudent = new Student("John");
        Student modifiedStudent = new Student("Jane");
        when(studentRepository.findById((Long) any())).thenReturn(null);
        // exercise
        studentController.updateEntity(existingStudent, modifiedStudent);
        // verify
        verify(studentView).showError("No existing entity: " + existingStudent,
                existingStudent);
        verifyNoMoreInteractions(ignoreStubs(studentRepository));
    }

    @Test
    public void testUpdateEntityWhenRepositoryExceptionIsThrownInFindByIdShouldCallShowError()
            throws RepositoryException {
        // setup
        String message = "message";
        Student existingStudent = new Student("John");
        Student modifiedStudent = new Student("Jane");
        when(studentRepository.findById((Long) any()))
                .thenThrow(new RepositoryException(message));
        // exercise
        studentController.updateEntity(existingStudent, modifiedStudent);
        // verify
        verify(studentView).showError("Repository exception: " + message,
                existingStudent);
        verifyNoMoreInteractions(ignoreStubs(studentRepository));
    }

    @Test
    public void testUpdateEntityWhenRepositoryExceptionIsThrownInUpdateShouldCallShowError()
            throws RepositoryException {
        // setup
        String message = "message";
        Student existingStudent = new Student("John");
        Student modifiedStudent = new Student("Jane");
        when(studentRepository.findById((Long) any()))
                .thenReturn(existingStudent);
        doThrow(new RepositoryException(message)).when(studentRepository)
                .update(modifiedStudent);
        // exercise
        studentController.updateEntity(existingStudent, modifiedStudent);
        // verify
        verify(studentView).showError("Repository exception: " + message,
                existingStudent);
        verifyNoMoreInteractions(ignoreStubs(studentRepository));
    }
}
