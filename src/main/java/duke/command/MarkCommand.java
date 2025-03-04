package duke.command;

import duke.DukeException;
import duke.task.Task;
import duke.task.TaskList;

import duke.ui.Ui;

public class MarkCommand extends Command {
    private final int taskNum;

    public MarkCommand(int taskNum) {
        this.taskNum = taskNum;
    }

    @Override
    public String execute(TaskList tasks) {
        try {
            Task task = tasks.get(taskNum - 1);
            tasks.markAsDone(this.taskNum - 1);
            return Ui.markAsDone(task);
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException("Invalid task number...");
        }
    }
}
