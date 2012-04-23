package auctionhaus



import grails.test.mixin.*
import org.junit.*
import grails.test.mixin.domain.DomainClassUnitTestMixin

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(CustomerService)
@TestMixin(DomainClassUnitTestMixin)

class CustomerServiceTests {



    void testCustomerCreateFailsWithoutValidParameters() {



        mockDomain(Customer)
        def customerService = new CustomerService()
        
        //create a customer without valid parameters
        def customerInstance = new Customer()

        //the service should return a null since the customer won't validate without setting valid parameters
        assert customerService.createCustomer(customerInstance) == null
        //the customer count should be 0 since we didnt successfully add a customer
        assert Customer.count == 0
        

    }


    void testCustomerCreateSucceedsWithValidParameters() {



        mockDomain(Customer)
        def customerService = new CustomerService()



            //populate valid parameters
        def customerInstance = new Customer(email:"asdf@asdf.com", password: "frefrefr", createdDate: new Date() )

        //the service should return the customer instance upon success
        def customerResult = customerService.createCustomer(customerInstance)

        //check that the service returned the customer instance
        assert customerResult == customerInstance
        assert customerResult.email == "asdf@asdf.com"
        assert customerResult.password == "frefrefr"

        //check the service saved the customer
        assert Customer.count == 1






    }
}
