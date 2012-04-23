package auctionhaus



import org.junit.*
import grails.test.mixin.*

@TestFor(BidController)
@Mock(Bid)
class BidControllerTests {


    def populateValidParams(params) {


      assert params != null

        BigDecimal startingBidPrice = 10.00
        // Create a test end date and time that is one day from today
        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"aore@yahoo.com",password: "1234567",createdDate: new Date())

        def testList = new Listing(listingName: "Apple TV",listingEndDateTime: testEndDateTime, startingBidPrice: 10.00, seller:testSeller)


        
        params["bidAmount"] = 20
        params["bidDateTime"] = new Date()
        params["bidder"] = testSeller
        params["listing"] = testList
    }

    void testIndex() {
        controller.index()
        assert "/bid/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.bidInstanceList.size() == 0
        assert model.bidInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.bidInstance != null
    }


  //  L-7: The detail page for the listing allows a new bid to be placed (unit test of the controller action that handles this requirement)
    void testSave() {

        //since valid params are not set up, this should cause validation errors
        controller.save()

     //bid controller will redirect to listing show page to display errors
        assert response.redirectedUrl == '/listing/show'

       // error message will be in the flash.message field of the controller
        assert controller.flash.message != null

        response.reset()

        populateValidParams(params)
        //a save should succeed using valid parameters
        controller.save()

        //message indicating save was successful will be in the flash.message field of the controller
        assert controller.flash.message != null
        //test save was successful
        assert Bid.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/bid/list'


        populateValidParams(params)
        def bid = new Bid(params)

        assert bid.save() != null

        params.id = bid.id

        def model = controller.show()

        assert model.bidInstance == bid
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/bid/list'


        populateValidParams(params)
        def bid = new Bid(params)

        assert bid.save() != null

        params.id = bid.id

        def model = controller.edit()

        assert model.bidInstance == bid
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/bid/list'

        response.reset()


        populateValidParams(params)
        def bid = new Bid(params)

        assert bid.save() != null

        // test invalid parameters in update
        params.id = bid.id

        params["bidder"] = null // a null bidder is in invalid parameter

        controller.update()

        assert view == "/bid/edit"
        assert model.bidInstance != null

        bid.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/bid/show/$bid.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        bid.clearErrors()

        populateValidParams(params)
        params.id = bid.id
        params.version = -1
        controller.update()

        assert view == "/bid/edit"
        assert model.bidInstance != null
        assert model.bidInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/bid/list'

        response.reset()

        populateValidParams(params)
        def bid = new Bid(params)

        assert bid.save() != null
        assert Bid.count() == 1

        params.id = bid.id

        controller.delete()

        assert Bid.count() == 0
        assert Bid.get(bid.id) == null
        assert response.redirectedUrl == '/bid/list'
    }
}
