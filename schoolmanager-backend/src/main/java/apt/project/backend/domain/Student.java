package apt.project.backend.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Student extends BaseEntity {

    @Column(unique = true)
    private String name;

    @OneToMany(
            mappedBy = "student",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Exam> exams;

    public Student() {
        this.setExams(new ArrayList<>());
    }

    public Student(String name) {
        this.name = name;
        this.setExams(new ArrayList<>());
    }

    public void merge(Student entity) {
        if (entity.getName() != null) {
            this.name = entity.getName();
        }
    }

    public void addExam(Exam exam) {
        this.exams.add(exam);
        exam.setStudent(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student [name=" + name + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    public boolean removeExam(Exam exam) {
        boolean res = exams.remove(exam);
        if (res) {
            exam.setStudent(null);
        }
        return res;
    }

    public int findExam(Exam exam) {
        return exams.stream()
                .filter(e -> e.getCourse().getTitle()
                        .equals(exam.getCourse().getTitle()))
                .map(exams::indexOf).findFirst().orElse(-1);
    }

}
