package apt.project.frontend.view.swing;

import java.util.List;

import apt.project.backend.domain.Course;
import apt.project.backend.domain.Exam;

public class DialogManager {

    public DialogManager() {
        //
    }

    public String manageDialog(String label) {
        // CustomDialog dialog = new CustomDialog(label);
        // dialog.showDialog();
        // return dialog.getOutcome();
        return null;
    }

    public String manageDialog(String label, String value) {
        // CustomDialog dialog = new CustomDialog(label, value);
        // dialog.showDialog();
        // return dialog.getOutcome();
        return null;
    }

    // per questo metodo serve il repository di course e fare la diff con la
    // lista che viene passata (in realta è dialog che deve farlo)
    public Exam manageDialogExam(List<Course> exams) {
        // TODO Auto-generated method stub
        return null;
    }

}
