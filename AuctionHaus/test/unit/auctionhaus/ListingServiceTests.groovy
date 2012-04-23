package auctionhaus



import grails.test.mixin.*
import org.junit.*
import grails.test.mixin.domain.DomainClassUnitTestMixin

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ListingService)
@TestMixin(DomainClassUnitTestMixin)
class ListingServiceTests {


    void testCustomerCreateFailsWithoutValidParameters() {



        mockDomain(Listing)
        def listingService = new ListingService()

        //create a listing without valid parameters
        def listingInstance = new Listing()

        //the service should return a null since the listing won't validate without setting valid parameters
        assert listingService.createListing(listingInstance) == null
        //the customer count should be 0 since we didnt successfully add a customer
        assert Listing.count == 0


    }


    void testCustomerCreateSucceedsWithValidParameters() {



        mockDomain(Listing)
        def listingService = new ListingService()

        //create a listing with valid parameters
        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"asdfasdf@yahoo.com",password: "1234567",createdDate: new Date())
        def listingInstance = new Listing(listingName:"test listing", listingEndDateTime: testEndDateTime, startingBidPrice: 10, seller:  testSeller)

        //the service should return the customer instance upon success
        def listingResult = listingService.createListing(listingInstance)
        
        //check that the service returned the listing instance
        assert listingResult == listingInstance

        //check the service saved the customer
        assert Listing.count == 1

    }

}
