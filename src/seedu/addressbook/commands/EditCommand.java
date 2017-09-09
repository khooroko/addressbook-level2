package seedu.addressbook.commands;

import seedu.addressbook.common.Messages;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.Name;
import seedu.addressbook.data.person.Phone;
import seedu.addressbook.data.person.Email;
import seedu.addressbook.data.person.Address;
import seedu.addressbook.data.person.Person;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.data.person.UniquePersonList;

public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits information of the person identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX, FIELD, NEW INFO\n"
            + "Example 1: " + COMMAND_WORD + " 1 " + "name " + "Jacob Dough\n"
            + "Example 2: " + COMMAND_WORD + " 1 " + "phone " + "91234567";

    public static final String MESSAGE_EDIT_FIELD_INVALID = "Field is invalid. Accepted fields are name, phone, email, and address.";
    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edit successful. Person details: %1$s";

    public final String field;
    public final String newInfo;


    public EditCommand(int targetVisibleIndex, String field, String newInfo) {
        super(targetVisibleIndex);
        this.field = field;
        this.newInfo = newInfo;
    }

    @Override
    public CommandResult execute() {
        try {
            final Person target = getTargetWritablePerson();
            switch (field) {
                case "name":
                    target.setName(newInfo);
                    break;
                case "phone":
                    target.setPhone(newInfo);
                    break;
                case "email":
                    target.setEmail(newInfo);
                    break;
                case "address":
                    target.setAddress(newInfo);
                    break;
                default:
                    throw new IllegalValueException(MESSAGE_EDIT_FIELD_INVALID);
            }
            return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, target.toString()));

        } catch (IllegalValueException ive) {
            return new CommandResult(ive.getMessage());
        } catch (IndexOutOfBoundsException ie) {
            return new CommandResult(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
    }
}
