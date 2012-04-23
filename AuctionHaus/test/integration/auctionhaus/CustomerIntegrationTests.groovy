package auctionhaus

import static org.junit.Assert.*
import org.junit.*
import grails.validation.ValidationException


//Amit Thakore and Ben Williams

class CustomerIntegrationTests extends GroovyTestCase{
    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }


    //    C-2: Email address must be a unique field (integration test)
    //   Test that saving two customers with identical email addresses causes validation exception (exceptional case)
    @Test
    void testUniqueEmailError(){


        def customer = new Customer()
        customer.email = "amitthakore16@gmail.com"
        customer.password = "123459"
        customer.createdDate  = new Date()
        customer.save()
        def customer1 = new Customer()
        customer1.email = "amitthakore16@gmail.com"
        customer1.password = "123459"
        customer1.createdDate  = new Date()

        //shouldFail(ValidationException){
          //  customer1.save()               //this should throw ValidationException
        //}

        assert (!customer1.save())
        
    }



    //    C-2: Email address must be a unique field (integration test)
    //   Test that saving two customers with different email addresses is successfully validated with no errors on email property (exceptional case)
    @Test
    void testUniqueEmailOK(){


        def customer = new Customer()
        customer.email = "amitthakore16@gmail.com"
        customer.password = "123459"
        customer.createdDate  = new Date()
        customer.save()
        def customer1 = new Customer()
        customer1.email = "amitthakore17@gmail.com"
        customer1.password = "123459"
        customer1.createdDate  = new Date()
        customer1.save()


        assert customer1.validate()
        assert null == customer1.errors["email"]
    }
}
