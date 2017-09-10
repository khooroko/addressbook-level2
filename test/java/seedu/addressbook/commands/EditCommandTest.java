package seedu.addressbook.commands;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import seedu.addressbook.common.Messages;
import seedu.addressbook.data.AddressBook;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.Address;
import seedu.addressbook.data.person.Email;
import seedu.addressbook.data.person.Name;
import seedu.addressbook.data.person.Person;
import seedu.addressbook.data.person.Phone;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.data.person.UniquePersonList.PersonNotFoundException;
import seedu.addressbook.data.tag.UniqueTagList;
import seedu.addressbook.ui.TextUi;
import seedu.addressbook.util.TestUtil;

public class EditCommandTest {

    private AddressBook emptyAddressBook;
    private AddressBook addressBook;
    private AddressBook editedAddressBook;

    private List<ReadOnlyPerson> emptyDisplayList;
    private List<ReadOnlyPerson> listWithEveryone;
    private List<ReadOnlyPerson> editedListWithEveryone;

    @Before
    public void setUp() throws Exception {
        Person johnDoe = new Person(new Name("John Doe"), new Phone("61234567", false),
                new Email("john@doe.com", false), new Address("395C Ben Road", false),
                new UniqueTagList());
        Person janeDoe = new Person(new Name("Jane Doe"), new Phone("91234567", false),
                new Email("jane@doe.com", false), new Address("33G Ohm Road", false),
                new UniqueTagList());
        Person davidGrant = new Person(new Name("David Grant"), new Phone("61121122", false),
                new Email("david@grant.com", false), new Address("44H Define Road", false),
                new UniqueTagList());
        Person paulGrant = new Person(new Name("Paul Grant"), new Phone("61121122", false),
                new Email("paul@grant.com", false), new Address("44H Define Road", false),
                new UniqueTagList());

        // change name to John Dough
        Person johnEdited = new Person(new Name("John Dough"), new Phone("61234567", false),
                new Email("john@doe.com", false), new Address("395C Ben Road", false),
                new UniqueTagList());

        // change phone to 91111111
        Person janeEdited = new Person(new Name("Jane Doe"), new Phone("91111111", false),
                new Email("jane@doe.com", false), new Address("33G Ohm Road", false),
                new UniqueTagList());

        // change email to grant#david.com
        Person davidEdited = new Person(new Name("David Grant"), new Phone("61121122", false),
                new Email("grant@david.com", false), new Address("44H Define Road", false),
                new UniqueTagList());

        // change address to 12 West Coast Road
        Person paulEdited = new Person(new Name("Paul Grant"), new Phone("61121122", false),
                new Email("paul@grant.com", false), new Address("12 West Coast Road", false),
                new UniqueTagList());

        emptyAddressBook = TestUtil.createAddressBook();
        addressBook = TestUtil.createAddressBook(johnDoe, janeDoe, davidGrant, paulGrant);
        editedAddressBook = TestUtil.createAddressBook(johnEdited, janeEdited, davidEdited, paulEdited);

        emptyDisplayList = TestUtil.createList();

        listWithEveryone = TestUtil.createList(johnDoe, janeDoe, davidGrant, paulGrant);
    }

    @Test
    public void execute_emptyAddressBook_returnsPersonNotFoundMessage() {
        assertEditFailsDueToNoSuchPerson(1, emptyAddressBook, listWithEveryone);
    }

    @Test
    public void execute_noPersonDisplayed_returnsInvalidIndexMessage() {
        assertEditFailsDueToInvalidIndex(1, addressBook, emptyDisplayList);
    }

    @Test
    public void execute_targetPersonNotInAddressBook_returnsPersonNotFoundMessage()
            throws IllegalValueException {
        Person notInAddressBookPerson = new Person(new Name("Not In Book"), new Phone("63331444", false),
                new Email("notin@book.com", false), new Address("156D Grant Road", false),
                new UniqueTagList());
        List<ReadOnlyPerson> listWithPersonNotInAddressBook = TestUtil.createList(notInAddressBookPerson);

        assertEditFailsDueToNoSuchPerson(1, addressBook, listWithPersonNotInAddressBook);
    }

    @Test
    public void execute_invalidIndex_returnsInvalidIndexMessage() {
        assertEditFailsDueToInvalidIndex(0, addressBook, listWithEveryone);
        assertEditFailsDueToInvalidIndex(-1, addressBook, listWithEveryone);
        assertEditFailsDueToInvalidIndex(listWithEveryone.size() + 1, addressBook, listWithEveryone);
    }

    @Test
    public void execute_invalidArgs_returnsInvalidFieldMessage() {
        assertEditFailsDueToInvalidField(1, "1", addressBook, listWithEveryone);
        assertEditFailsDueToInvalidField(1, "invalidField", addressBook, listWithEveryone);
        assertEditFailsDueToInvalidField(1, "invalid field", addressBook, listWithEveryone);
    }

    /**
     * Creates a new edit command.
     *
     * @param targetVisibleIndex of the person that we want to edit
     */
    private EditCommand createEditCommand(int targetVisibleIndex, String targetField, String targetInfo,
                                          AddressBook addressBook, List<ReadOnlyPerson> displayList) {

        EditCommand command = new EditCommand(targetVisibleIndex, targetField, targetInfo);
        command.setData(addressBook, displayList);

        return command;
    }

    /**
     * Executes the command, and checks that the execution was what we had expected.
     */
    private void assertCommandBehaviour(EditCommand editCommand, String expectedMessage,
                                        AddressBook expectedAddressBook, AddressBook actualAddressBook) {

        CommandResult result = editCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedAddressBook.getAllPersons(), actualAddressBook.getAllPersons());
    }

    /**
     * Asserts that the index is not valid for the given display list.
     */
    private void assertEditFailsDueToInvalidIndex(int invalidVisibleIndex, AddressBook addressBook,
                                                      List<ReadOnlyPerson> displayList) {

        String expectedMessage = Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

        EditCommand command = createEditCommand(invalidVisibleIndex, "name", Name.EXAMPLE, addressBook, displayList);
        assertCommandBehaviour(command, expectedMessage, addressBook, addressBook);
    }

    /**
     * Asserts that the field is not valid.
     */
    private void assertEditFailsDueToInvalidField(int validVisibleIndex, String invalidField, AddressBook addressBook,
                                                  List<ReadOnlyPerson> displayList) {

        String expectedMessage = EditCommand.MESSAGE_EDIT_FIELD_INVALID;

        EditCommand command = createEditCommand(validVisibleIndex, invalidField, Name.EXAMPLE, addressBook, displayList);
        assertCommandBehaviour(command, expectedMessage, addressBook, addressBook);
    }

    /**
     * Asserts that the person at the specified index cannot be edited, because that person
     * is not in the address book.
     */
    private void assertEditFailsDueToNoSuchPerson(int visibleIndex, AddressBook addressBook,
                                                      List<ReadOnlyPerson> displayList) {

        String expectedMessage = Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK;

        EditCommand command = createEditCommand(visibleIndex, "name", Name.EXAMPLE, addressBook, displayList);
        assertCommandBehaviour(command, expectedMessage, addressBook, addressBook);
    }

    /**
     * Asserts that the person at the specified index can be successfully edited.
     *
     * The addressBook passed in will not be modified (no side effects).
     *
     * @throws PersonNotFoundException if the selected person is not in the address book
     */
    private void assertEditSuccessful(int targetVisibleIndex, AddressBook addressBook,
                                          List<ReadOnlyPerson> displayList) throws PersonNotFoundException {

        /*ReadOnlyPerson targetPerson = displayList.get(targetVisibleIndex - TextUi.DISPLAYED_INDEX_OFFSET);

        AddressBook expectedAddressBook = TestUtil.clone(addressBook);
        expectedAddressBook.removePerson(targetPerson);
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, targetPerson);

        AddressBook actualAddressBook = TestUtil.clone(addressBook);

        EditCommand command = createEditCommand(targetVisibleIndex, actualAddressBook, displayList);
        assertCommandBehaviour(command, expectedMessage, expectedAddressBook, actualAddressBook);*/
    }
}
