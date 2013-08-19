package au.edu.swin.ict.road.classbuilder.testSuite;

public class Person {
    private String firstName;

    public Person() {
	firstName = "This";
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String toString() {
	return "Hello World";
    }
}
