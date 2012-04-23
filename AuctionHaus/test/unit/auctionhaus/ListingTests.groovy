package auctionhaus



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */

//Amit Thakore and Ben Williams
@TestFor(Listing)
class ListingTests {

    //L-1: Listings have the following required fields: name, end date/time, and starting bid price (unit test)
    //test that if required fields are not present, there are validation errors of 'nullable' on the required fields (exceptional path)
    void testRequiredListingFieldsMissingCauseError()
    {
        mockForConstraintsTests(Listing)
        def listingNull = new Listing()

        assert  !listingNull.validate()

        assert "nullable" == listingNull.errors["listingName"]
        assert "nullable" == listingNull.errors["listingEndDateTime"]
        assert "nullable" == listingNull.errors["startingBidPrice"]


    }
    //L-1: Listings have the following required fields: name, end date/time, and starting bid price (unit test)
    //test that if required fields are  present, validation is successful (happy path)
    void testRequiredListingFieldsOK()
    {
        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"amit_thakore1@yahoo.com",password: "1234567",createdDate: new Date())

        def testList = new Listing(listingName: "Apple TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)

        assert testList.validate()

    }

    //L-2: Listings have the following optional fields: description (unit test)
    //test that it is possible to enter a description for a listing and validate successfully, no errors on description field (happy path)
    void testOptionalListingDescriptionFieldOK()
    {
        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"amit_thakore1@yahoo.com",password: "1234567",createdDate: new Date())

        def testList = new Listing(listingName: "Apple TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)

        //create a description for listing
        testList.listingDescription = "test description"

        //listing should validate without error
        assert testList.validate()

        //description should be same test as we created earlier
        assert (testList.listingDescription.compareTo("test description") == 0)
    }

    // L-3: Listings are required to have a seller (Customer) (unit test)
    // test that there is a validation error 'nullable' for a listing that has no seller
    void testListingSellerNullCausesError()
    {
        mockForConstraintsTests(Listing)
        def listNull = new Listing()

        assert  !listNull.validate()


        assert "nullable" == listNull.errors["seller"]




    }

    // L-3: Listings are required to have a seller (Customer) (unit test)
    //check that validation is successful and no nullable error when the seller is present (happy path)
    void testBidBidderNonNullOK()
    {
        mockForConstraintsTests(Listing)


        BigDecimal startingBidPrice = 10.00
        // Create a test end date and time that is one day from today
        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"amit_thakore1@yahoo.com",password: "1234567",createdDate: new Date())

        def testList = new Listing(listingName: "Apple TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)

        assert testList.validate()


        assert "nullable" != testList.errors["seller"]




    }

    //L-4: Listing descriptions must be less than 256 characters (unit test)
    //test that it is possible to enter a description for a listing under 256 characters and validate without error (happy path)
    void testListingDescriptionFieldOK()
    {
        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"amit_thakore1@yahoo.com",password: "1234567",createdDate: new Date())

        def testList = new Listing(listingName: "Apple TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)

        //create a description for listing
        testList.listingDescription = "test description"

        //listing should validate without error
        assert testList.validate()

    }

    //L-4: Listing descriptions must be less than 256 characters (unit test)
    //test that a listing over 256 characters causes validation error on description field (exceptional path)
    void testListingDescriptionFieldOversizeError()
    {
        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"amit_thakore1@yahoo.com",password: "1234567",createdDate: new Date())

        def testList = new Listing(listingName: "Apple TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)

        //create a description for listing
        testList.listingDescription = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"

        //listing should validate without error
        assert !testList.validate()
        assert testList.errors["listingDescription"] != null

    }


    //L-5: Listing end date/time must be in the future (unit test)
    //test that a listing with a date/time in future validates without error
    void testListingEndDateTimeInFutureOK()
    {

        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"amit_thakore1@yahoo.com",password: "1234567",createdDate: new Date())

        def testList = new Listing(listingName: "Apple TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)

        //listing should validate without error
        assert testList.validate()

    }
    //L-5: Listing end date/time must be in the future (unit test)
    //test that a listing with a date/time in past causes validation error on listingEndDateTime field
    void testListingEndDateTimeInPastCausesError()
    {

        def testEndDateTime = new Date() - 1
        def testSeller = new Customer(email:"amit_thakore1@yahoo.com",password: "1234567",createdDate: new Date())

        def testList = new Listing(listingName: "Apple TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)

        //listing should validate without error
        assert !testList.validate()
        assert testList.errors["listingEndDateTime"] != null

    }

    //L-6: Listing name must be less than 64 characters (unit test)
    //test that it is possible to enter a name for a listing under 64 characters and validate without error (happy path)
    void testListingNameFieldOK()
    {
        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"amit_thakore1@yahoo.com",password: "1234567",createdDate: new Date())

        def testList = new Listing(listingName: "Apple TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)

        //listing should validate without error
        assert testList.validate()

    }
    //L-6: Listing name must be less than 64 characters (unit test)
    //test that entering a name for a listing over 64 characters causes validation error on listingName field (happy path)
    void testListingNameFieldOversizeErrors()
    {
        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"amit_thakore1@yahoo.com",password: "1234567",createdDate: new Date())

        def testList = new Listing(listingName: "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)

        //listing should validate without error
        assert !testList.validate()
        assert testList.errors["listingName"] != null
    }

    //  L-7: Listing has a nullable field for the winner (Customer) (unit test)
    //test that a listing has a winner field (happy path)
    void testOptionalWinnerFieldOK()
    {
        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"amit_thakore1@yahoo.com",password: "1234567",createdDate: new Date())

        def testList = new Listing(listingName: "Apple TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)



        //listing should have a winner field that is null since the auction is not complete
        assert (testList.winner == null)

    }

}
