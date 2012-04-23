package auctionhaus



import grails.test.mixin.*
import org.junit.*
import grails.test.mixin.domain.DomainClassUnitTestMixin

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(BidService)
@TestMixin(DomainClassUnitTestMixin)
class BidServiceTests {
 


        void testBidCreateFailsWithoutValidParameters() {



            mockDomain(Bid)
            def bidService = new BidService()

            //create a bid without valid parameters
            def bidInstance = new Bid()

            //the service should return a null since the bid won't validate without setting valid parameters
            assert bidService.createBid(bidInstance) == null
            //the bid count should be 0 since we didnt successfully add a customer
            assert Bid.count == 0


        }


        void testBidCreateSucceedsWithValidParameters() {



            mockDomain(Bid)
            def bidService = new BidService()

            //create a bid with valid parameters

            // Create a test end date and time that is one day from today
            def testEndDateTime = new Date() + 1
            def testSeller = new Customer(email:"aore@yahoo.com",password: "1234567",createdDate: new Date())

            def testList = new Listing(listingName: "Apple TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)


            def bidInstance = new Bid(bidAmount: 20, bidDateTime: new Date(), bidder: testSeller, listing: testList )



            //the service should return the bid instance upon success
            def bidResult = bidService.createBid(bidInstance)

            //check that the service returned the listing instance
            assert bidResult == bidInstance

            //check the service saved the customer
            assert Bid.count == 1

        }



    void testBidCreateFailsWithExpiredListing() {




        mockDomain(Bid)
        def bidService = new BidService()

        //create a bid with valid parameters

        // Create a test end date and time that is one day from today
        def testEndDateTime = new Date() - 1
        def testSeller = new Customer(email:"aore@yahoo.com",password: "1234567",createdDate: new Date())

        def testList = new Listing(listingName: "Apple TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)


        def bidInstance = new Bid(bidAmount: 20, bidDateTime: new Date(), bidder: testSeller, listing: testList )



        //the service should return the bid instance upon success
        def bidResult = bidService.createBid(bidInstance)



        //the service should return a null since the bid won't validate without setting valid parameters
        assert bidResult == null
        //the bid count should be 0 since we didnt successfully add a customer
        assert Bid.count == 0


    }
}
