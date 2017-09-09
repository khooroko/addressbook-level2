package seedu.addressbook.data.person;

import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.tag.UniqueTagList;

import java.util.Objects;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Person implements ReadOnlyPerson {

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;

    private final UniqueTagList tags;
    
    public static final String MESSAGE_INVALID_NAME = "Invalid name.";
    public static final String MESSAGE_INVALID_PHONE = "Invalid phone.";
    public static final String MESSAGE_INVALID_EMAIL = "Invalid email.";
    public static final String MESSAGE_INVALID_ADDRESS = "Invalid address.";
    
    /**
     * Assumption: Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, UniqueTagList tags) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
    }

    /**
     * Copy constructor.
     */
    public Person(ReadOnlyPerson source) {
        this(source.getName(), source.getPhone(), source.getEmail(), source.getAddress(), source.getTags());
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public Phone getPhone() {
        return phone;
    }

    @Override
    public Email getEmail() {
        return email;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public UniqueTagList getTags() {
        return new UniqueTagList(tags);
    }

    public void setName(String name) throws IllegalValueException {
        try {
            this.name = new Name(name) ;
        } catch (IllegalValueException ive) {
            throw new IllegalValueException(MESSAGE_INVALID_NAME);
        }
    }

    public void setPhone(String phone) throws IllegalValueException {
        try {
            this.phone = new Phone(phone,this.phone.isPrivate()) ;
        } catch (IllegalValueException ive) {
            throw new IllegalValueException(MESSAGE_INVALID_PHONE);
        }
    }

    public void setEmail(String email) throws IllegalValueException {
        try {
            this.email = new Email(email,this.email.isPrivate()) ;
        } catch (IllegalValueException ive) {
            throw new IllegalValueException(MESSAGE_INVALID_EMAIL);
        }
    }
    
    public void setAddress(String address) throws IllegalValueException {
        try {
            this.address = new Address(address,this.address.isPrivate()) ;
        } catch (IllegalValueException ive) {
            throw new IllegalValueException(MESSAGE_INVALID_ADDRESS);
        }
    }

    /**
     * Replaces this person's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyPerson // instanceof handles nulls
                && this.hasSameData((ReadOnlyPerson) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        return getAsTextShowAll();
    }

}
