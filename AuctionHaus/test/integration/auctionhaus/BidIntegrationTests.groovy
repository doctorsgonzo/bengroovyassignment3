package auctionhaus

import static org.junit.Assert.*
import org.junit.*
import org.springframework.test.annotation.ExpectedException
import grails.validation.ValidationException

//Amit Thakore and Ben Williams

class BidIntegrationTests extends GroovyTestCase {

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }


    // B-5: The Bid amount must be at least .50 higher than the previous Bid for the same listing (integration test)
    //Test that adding a bid that is not 0.50 higher than previous bid causes validation exception on save. (exceptional case)
    @Test
    void testBidNotHighEnoughCausesError()
    {


        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"asdf@yahoo.com",password: "1234567",createdDate: new Date())


        def bidTest = new Bid(bidAmount: 20.0, bidDateTime: new Date(), bidder: testSeller)


        def bidTest2 = new Bid(bidAmount: 20.4, bidDateTime: new Date(), bidder: testSeller )


        testSeller.save()

        def testList = new Listing(listingName: "Apple TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)
        testList.save()

        testList.addToBids(bidTest)


        testList.addToBids(bidTest2)
        //since bidTest2 is only 0.40 greater than the last bid, it should cause a validation error

       // shouldFail(ValidationException){
         //   bidTest2.save()       //this should cause validation exception
        //}

        assert (!bidTest2.save())
        




    }


    // B-5: The Bid amount must be at least .50 higher than the previous Bid for the same listing (integration test)
    //Test that adding a bid that is 0.50 higher than previous bid  successfully validates, no error on bidAmount field. (happy path)
    @Test
    void testBidHighEnoughOK()
    {


        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"asdf@yahoo.com",password: "1234567",createdDate: new Date())


        def bidTest = new Bid(bidAmount: 20.0, bidDateTime: new Date(), bidder: testSeller)


        def bidTest2 = new Bid(bidAmount: 20.5, bidDateTime: new Date(), bidder: testSeller )


        testSeller.save()



        def testList = new Listing(listingName: "Apple TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)
        testList.save()

        testList.addToBids(bidTest)

        bidTest.save()

        testList.addToBids(bidTest2)
        //since bidTest2 is 0.50 greater than the last bid, it should not cause a validation error
        bidTest2.save()
        assert bidTest2.validate()

        assert bidTest2.errors['bidAmount'] == null



    }


    // B-6: When a Listing is saved, any new Bids added for the listing must be saved (integration test)
    // Test that bid is saved to db when added to list and list is saved (happy path)
    @Test
    void testListingSavedBidsAlsoSaved()
    {


        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"asdf@yahoo.com",password: "1234567",createdDate: new Date())


        def bidTest = new Bid(bidAmount: 20.0, bidDateTime: new Date(), bidder: testSeller)



        testSeller.save()

        def testList = new Listing(listingName: "Apple TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)


        testList.addToBids(bidTest)



        testList.save()


        //we should find bidTest in the db, because it was added to testList before call to testList.save
        assert (Bid.find(bidTest))




    }

    // B-6: When a Listing is saved, any new Bids added for the listing must be saved (integration test)
    // Test that bid is not saved to db when not added to list and list is saved (excaptional path)
    @Test
    void testListingNotSavedBidsAlsoNotSaved()
    {


        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"asdf@yahoo.com",password: "1234567",createdDate: new Date())


        def bidTest = new Bid(bidAmount: 20.0, bidDateTime: new Date(), bidder: testSeller)



        testSeller.save()

        def testList = new Listing(listingName: "Apple TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)


        testList.save()


        //we should find not bidTest in the db, because it was never saved nor added to testList before call to testList.save
        assert (!Bid.find(bidTest))



    }   
    
    //UI-1: The listing detail page will asynchronously load and display a list of the last 10 bids placed on the item showing the user, date/time, and amount. The implementation of the lookup of these results must be done with a Named Query. (Integration Test)
    //test the Bid class named query "bidsForListingId" returns the last ten bids for a listing in sorted order.
    // test will add 11 bids to a listing, and add 2 bids to a second listing
    // check that the named query on the first listing has 10 results, and checks the first and last results are what we expect based on the sort order, and check all 10 results
    // are for the first listing
    // test will also check that second listing has 2 results, and that they are sorted correctly, and check that both results are for the second listing
    @Test
    void testNamedQueryOnLast10BidsForListing()
    {

        //create a Customer to support creating a listing
        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"asdf@yahoo.com",password: "1234567",createdDate: new Date())




        testSeller.save()
        def bidTest = new Bid(bidAmount: 20.0, bidDateTime: new Date(), bidder: testSeller)



        
        //create a test listing
        def testList = new Listing(listingName: "Apple TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)


        //create a second test listing
        def testList2 = new Listing(listingName: "Google TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)

        testList.save()
        testList2.save()
        


        //add 11 test bids to the first listing
        for (int i = 0; i < 11; i++)
        {
            testList.addToBids(bidTest)
            testList.save()
            bidTest.save()
            bidTest = new Bid(bidAmount: bidTest.bidAmount + 0.5, bidDateTime: new Date(), bidder: testSeller)

          //  bidTest.bidAmount = bidTest.bidAmount + 0.5
        }

       // testList.save()
        
        bidTest.bidAmount = 20

        
        //add 2 test bids to the first listing
        for (int i = 0; i < 2; i++)
        {
            testList2.addToBids(bidTest)
            bidTest = new Bid(bidAmount: bidTest.bidAmount + 0.5, bidDateTime: new Date(), bidder: testSeller)
            testList2.save()
         
        }

                                       
        def namedQueryResult_testList1 = Bid.bidsForListingId(testList.id).list()
        def namedQueryResult_testList2 = Bid.bidsForListingId(testList2.id).list()
        
        //running named query bidsForListingId on testList  should return 10 results
        assert (namedQueryResult_testList1.size() == 10);


        //running named query bidsForListingId on testList2 should return 2 results
        assert (namedQueryResult_testList2.size() == 2);
        
        
        //test that sort order for the named query result on both lists is correct by testing that the first and last entries in each list have the bidAmount we expect
        //first bid in a named query on testList should have bidAmount 25.0
        assert (namedQueryResult_testList1.first().bidAmount == 25.0)
        //last bid in a named query on testList should have bidAmount 20.5
        assert (namedQueryResult_testList1.last().bidAmount == 20.5)

        //first bid in a named query on testList2 should have bidAmount 20.5
        assert (namedQueryResult_testList2.first().bidAmount == 20.5)
        //last bid in a named query on testList2 should have bidAmount 20.0
        assert (namedQueryResult_testList2.last().bidAmount == 20.0)
        
        
        // next test that the named query returns bids for the correct listing
        // test that all the results of the named query bidsForListingId on testList1 belong to testList1 
        for (int i = 0; i < namedQueryResult_testList1.size(); i++)
        {
            assert(namedQueryResult_testList1.get(i).listingId == testList.id)
        }

        // test that all the results of the named query bidsForListingId on testList2 belong to testList2
        for (int i = 0; i < namedQueryResult_testList2.size(); i++)
        {
            assert(namedQueryResult_testList2.get(i).listingId == testList2.id)
        }



    }

}
