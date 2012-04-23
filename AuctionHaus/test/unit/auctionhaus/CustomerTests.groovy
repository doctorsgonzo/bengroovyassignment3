package auctionhaus

import grails.test.mixin.*
import org.junit.*
import java.awt.print.Book
import com.sun.tools.internal.xjc.generator.util.ExistingBlockReference


/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */

//Amit Thakore and Ben Williams

@TestFor(Customer)

class CustomerTests
{

    // C-1 Test Email,password and created date are required fields
    // Create customer object with null values and test if making them nullable gives errors. (exceptional path)
    void testCustomerNullFieldsCauseError()
    {
        mockForConstraintsTests(Customer)

        def customerNull = new Customer()

        assert  !customerNull.validate()

        assert "nullable" == customerNull.errors["email"]
        assert "nullable" == customerNull.errors["password"]
        assert "nullable" == customerNull.errors["createdDate"]

    }


    // C-1 Test Email,password and created date are required fields
    // Create customer object and fill required values and test if validation successful with no nullable errors. (happy path)
    void testCustomerNonNullFieldsOK()
    {
        mockForConstraintsTests(Customer)

        def customer = new Customer()
        customer.email = "amitthakore16@gmail.com"
        customer.password = "123456"
        customer.createdDate  = new Date()
        assert customer.validate()

        assert customer.errors["email"] != "nullable"
        assert customer.errors["password"] != "nullable"
        assert customer.errors["createdDate"] != "nullable"
    }

    //C-3: Verify that email is a valid email address
    // Create customer object with valid email address and test validation succeeds with no error on email property. (happy path)
    void testCustomerEmailOK()
    {
        mockForConstraintsTests(Customer)
        def customerEmailOK = new Customer()
        customerEmailOK.email = "amitthakore16@gmail.com"
        customerEmailOK.password = "123456"
        customerEmailOK.createdDate  = new Date()
        assert customerEmailOK.validate()
        assert "email" != customerEmailOK.errors["email"]
    }
    //C-3: Verify that email is a valid email address
    // Create customer object with invalid email address and test validation fails with error on email property. (exceptional path)
    void testCustomerEmailError()
    {
        mockForConstraintsTests(Customer)



        def customerEmail = new Customer()
        customerEmail.email = "amitthakore16gmail.com"
        customerEmail.password = "123456"
        customerEmail.createdDate  = new Date()

        assert !customerEmail.validate()
        assert "email" == customerEmail.errors["email"]

    }


    //C-4: Password must be between 6-8 characters (unit test)
    // Create customer object with invalid password and test validation fails with error on password property. (exceptional path)
 /*   void testCustomerPasswordError()
    {
        mockForConstraintsTests(Customer)

        def customerPassWord = new Customer()
        customerPassWord.email = "amitthakore16@gmail.com"
        customerPassWord.password = "123459999996"
        customerPassWord.createdDate  = new Date()

        assert !customerPassWord.validate()

        assert "size" == customerPassWord.errors["password"]


    }     */

    //C-4: Password must be between 6-8 characters (unit test)
    // Create customer object with valid password and test validation succeeds with no error on password property. (happy path)
    void testCustomerPasswordOK()
    {
        mockForConstraintsTests(Customer)

        def customerPassWord = new Customer()
        customerPassWord.email = "amitthakore16@gmail.com"
        customerPassWord.password = "123459"
        customerPassWord.createdDate  = new Date()

        assert customerPassWord.validate()
        //C-4: Password must be between 6-8 characters (unit test)
        assert "size" != customerPassWord.errors["password"]


    }

}