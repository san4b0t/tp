package seedu.job.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.job.commons.core.GuiSettings;
import seedu.job.commons.core.LogsCenter;
import seedu.job.logic.jobcommands.Command;
import seedu.job.logic.jobcommands.CommandResult;
import seedu.job.logic.jobcommands.exceptions.JobCommandException;
import seedu.job.logic.parser.JobBookCommandParser;
import seedu.job.logic.parser.exceptions.ParseException;
import seedu.job.model.jobapplication.JobApplication;
import seedu.job.model.jobapplication.Model;
import seedu.job.model.jobapplication.ReadOnlyJobBook;
import seedu.job.storage.DataStorage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;

    //New code
    private final DataStorage dataStorage;

    private final JobBookCommandParser jobBookCommandParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, DataStorage dataStorage) {
        this.model = model;
        this.dataStorage = dataStorage;
        jobBookCommandParser = new JobBookCommandParser();
    }

    @Override
    public CommandResult execute(String commandText) throws JobCommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = jobBookCommandParser.parseCommand(commandText);
        commandResult = command.execute(model);

        // Only save if the command indicates it should trigger a save operation
        if (commandResult.shouldSave()) {
            try {
                dataStorage.saveJobApplicationData(model.getJobBook().getApplicationList());
            } catch (AccessDeniedException e) {
                throw new JobCommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
            } catch (IOException ioe) {
                throw new JobCommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
            }
        }

        return commandResult;
    }

    @Override
    public ReadOnlyJobBook getJobBook() {
        return model.getJobBook();
    }

    @Override
    public ObservableList<JobApplication> getFilteredApplicationList() {
        return model.getFilteredApplicationList();
    }

    @Override
    public ObservableList<JobApplication> getFilteredApplicationsList() {
        return model.getFilteredApplicationList();
    }

    @Override
    public Path getJobBookFilePath() {
        return model.getJobBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
