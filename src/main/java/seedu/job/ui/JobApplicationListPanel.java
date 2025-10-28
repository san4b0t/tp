package seedu.job.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.job.commons.core.LogsCenter;
import seedu.job.model.jobapplication.JobApplication;

/**
 * Panel containing the list of job application.
 */
public class JobApplicationListPanel extends UiPart<Region> {
    private static final String FXML = "JobApplicationListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(JobApplicationListPanel.class);

    @FXML
    private ListView<JobApplication> jobApplicationListView;

    private JobApplication recentlyModifiedApplication;

    /**
     * Creates a {@code JobApplicationListPanel} with the given {@code ObservableList}.
     */
    public JobApplicationListPanel(ObservableList<JobApplication> jobApplicationList) {
        super(FXML);
        jobApplicationListView.setItems(jobApplicationList);
        jobApplicationListView.setCellFactory(listView -> new JobApplicationListViewCell());
    }

    /**
     * Sets the recently modified application and refreshes the view to apply highlighting.
     */
    public void setRecentlyModifiedApplication(JobApplication application) {
        this.recentlyModifiedApplication = application;
        jobApplicationListView.refresh();
    }

    /**
     * Custom {@code ListCell} that displays the graphics of
     * a {@code Job Application} using a {@code JobApplicationCard}.
     */
    class JobApplicationListViewCell extends ListCell<JobApplication> {
        @Override
        protected void updateItem(JobApplication jobApplication, boolean empty) {
            super.updateItem(jobApplication, empty);

            if (empty || jobApplication == null) {
                setGraphic(null);
                setText(null);
            } else {
                int displayedIndex = getIndex() + 1;
                boolean isRecentlyModified = jobApplication.equals(recentlyModifiedApplication);
                setGraphic(new JobApplicationCard(jobApplication, displayedIndex, isRecentlyModified).getRoot());
            }
        }
    }

}
