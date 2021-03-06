package apt.project.frontend.view.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import apt.project.backend.domain.BaseEntity;
import apt.project.frontend.view.MainFrame;
import apt.project.frontend.view.View;

public class BasePanel<T extends BaseEntity> implements View<T> {

    private JPanel panel;
    private MainFrame mainFrame;
    private String headerText;

    protected JButton btnAdd;
    protected JButton btnModify;
    protected JButton btnDelete;
    protected DefaultListModel<T> listModel;
    protected JList<T> list;

    public BasePanel(JPanel panel, String headerText) {

        this.panel = panel;
        this.headerText = headerText;

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 0.0,
                Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
        panel.setLayout(gridBagLayout);

        btnAdd = new JButton("Add");
        GridBagConstraints gbc_btnAdd = new GridBagConstraints();
        gbc_btnAdd.insets = new Insets(0, 0, 0, 5);
        gbc_btnAdd.gridx = 0;
        gbc_btnAdd.gridy = 1;
        panel.add(btnAdd, gbc_btnAdd);

        btnModify = new JButton("Modify");
        btnModify.setEnabled(false);
        GridBagConstraints gbc_btnModify = new GridBagConstraints();
        gbc_btnModify.insets = new Insets(0, 0, 0, 5);
        gbc_btnModify.gridx = 1;
        gbc_btnModify.gridy = 1;
        panel.add(btnModify, gbc_btnModify);

        btnDelete = new JButton("Delete");
        btnDelete.setEnabled(false);
        GridBagConstraints gbc_btnDelete = new GridBagConstraints();
        gbc_btnDelete.gridx = 2;
        gbc_btnDelete.gridy = 1;
        panel.add(btnDelete, gbc_btnDelete);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridwidth = 3;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;

        JLabel lblListOfCourses = new JLabel(this.headerText);
        lblListOfCourses.setHorizontalAlignment(SwingConstants.CENTER);
        scrollPane.setColumnHeaderView(lblListOfCourses);
        panel.add(scrollPane, gbc_scrollPane);

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setName("entityList");
        scrollPane.setViewportView(list);
        list.addListSelectionListener(e -> {
            boolean enable = list.getSelectedIndex() != -1;
            btnDelete.setEnabled(enable);
            btnModify.setEnabled(enable);
        });

    }

    @Override
    public void showAll(List<T> entities) {
        entities.stream().forEach(listModel::addElement);
    }

    @Override
    public void entityAdded(T entity) {
        listModel.addElement(entity);
        mainFrame.resetErrorLabel();
    }

    @Override
    public void entityDeleted(T entity) {
        listModel.removeElement(entity);
        mainFrame.resetErrorLabel();
    }

    @Override
    public void entityUpdated(T modifiedEntity) {
        listModel.set(listModel.indexOf(modifiedEntity), modifiedEntity);
        mainFrame.resetErrorLabel();
    }

    @Override
    public void showError(String label) {
        mainFrame.displayErrorLabel(label);
    }

    public JPanel getPanel() {
        return panel;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    DefaultListModel<T> getListModel() {
        return listModel;
    }

    void clearListModel() {
        listModel.clear();
    }

}
