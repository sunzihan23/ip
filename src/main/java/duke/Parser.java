package duke;

import duke.command.*;
import duke.ui.Ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * The Parser class is responsible for parsing user input and executing corresponding actions.
 * It interacts with the TaskList and Ui classes to manage tasks and user interface interactions.
 */
public class Parser {
    /**
     * Parses user input and executes corresponding actions.
     *
     * @param input The user input to be parsed and acted upon.
     * @throws DukeException If there is an issue with parsing or executing the input.
     */
    public static Command parse(String input) throws DukeException {
        String[] parts = input.split(" ", 2);
        String action = parts[0];
        String details = parts.length == 1 ? "" : parts[1];
        switch (action) {
        case "bye":
        case "b":
            return new ExitCommand();
        case "list":
        case "l":
            return new ListCommand();
        case "mark":
        case "m":
            try {
                return new MarkCommand(Integer.parseInt(details));
            } catch (NumberFormatException e) {
                throw new DukeException("Enter an integer as the task number!");
            }
        case "find":
        case "f":
            return new FindCommand(details);
        case "todo":
        case "t":
            if (details.isEmpty()) {
                throw new DukeException("So what exactly do you want to do?");
            }
            return new AddCommand(details);
        case "deadline":
        case "d":
            if (details.isEmpty()) {
                throw new DukeException("So what exactly is the task?");
            }
            try {
                String[] subParts = details.split(" /by ", 2);
                LocalDateTime dateTime = formatDateTime(subParts[1]);
                return new AddCommand(subParts[0], dateTime);
            } catch (Exception e) {
                throw new DukeException("OOPS! The time format should be DD/MM/YYYY HHmm");
            }
        case "event":
        case "e":
            if (details.isEmpty()) {
                throw new DukeException("So what exactly is the task?");
            }
            try {
                String[] taskPart = details.split(" /from ", 2);
                String[] timePart = taskPart[1].split(" /to ", 2);
                LocalDateTime from = formatDateTime(timePart[0]);
                LocalDateTime to = formatDateTime(timePart[1]);
                return new AddCommand(taskPart[0], from, to);
            } catch (Exception e) {
                throw new DukeException("OOPS! The time format should be DD/MM/YYYY HHmm");
            }
        case "delete":
        case "del":
            try {
                return new DeleteCommand(Integer.parseInt(details));
            } catch (NumberFormatException e) {
                throw new DukeException("Enter an integer as the task number!");
            }
        case "help":
        case "h":
            return new HelpCommand();
        default:
            throw new DukeException("I have no idea what that means...\n"
                    + "Try entering 'help' to see what I can do!");
        }
    }

    /**
     * Parses a string into a LocalDateTime object using a specific format.
     *
     * @param input The input string representing a date and time.
     * @return A LocalDateTime object parsed from the input.
     * @throws DateTimeParseException If the input cannot be parsed into a valid LocalDateTime.
     */
    private static LocalDateTime formatDateTime(String input) throws DateTimeParseException {
        return LocalDateTime.parse(input, DateTimeFormatter.ofPattern("d/M/yyyy HHmm"));
    }
 }
