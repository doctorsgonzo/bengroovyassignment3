package auctionhaus



import org.junit.*
import grails.test.mixin.*

@TestFor(ListingController)
@Mock(Listing)
class ListingControllerTests {


    def populateValidParams(params) {
      assert params != null

        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"asdfsdf@yahoo.com",password: "1234567",createdDate: new Date())

        params["listingName"] = "Apple TV"
        params["listingEndDateTime"] = testEndDateTime
        params["startingBidPrice"] = 10.00
        params["seller"] = testSeller
    }

    void testIndex() {
        controller.index()
        assert "/listing/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.listingInstanceList.size() == 0
        assert model.listingInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.listingInstance != null
    }

    void testSave() {
        
        controller.listingService = new ListingService()

        controller.save()

        assert model.listingInstance != null
        assert view == '/listing/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/listing/show/1'
        assert controller.flash.message != null
        assert Listing.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/listing/list'


        populateValidParams(params)
        def listing = new Listing(params)

        assert listing.save() != null

        params.id = listing.id

        def model = controller.show()

        assert model.listingInstance == listing

    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/listing/list'


        populateValidParams(params)
        def listing = new Listing(params)

        assert listing.save() != null

        params.id = listing.id

        def model = controller.edit()

        assert model.listingInstance == listing
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/listing/list'

        response.reset()


        populateValidParams(params)
        def listing = new Listing(params)

        assert listing.save() != null

        // test invalid parameters in update       '
        params["listingName"] = null //null listing name is an invalid parameter

        params.id = listing.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/listing/edit"
        assert model.listingInstance != null

        listing.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/listing/show/$listing.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        listing.clearErrors()

        populateValidParams(params)
        params.id = listing.id
        params.version = -1
        controller.update()

        assert view == "/listing/edit"
        assert model.listingInstance != null
        assert model.listingInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/listing/list'

        response.reset()

        populateValidParams(params)
        def listing = new Listing(params)

        assert listing.save() != null
        assert Listing.count() == 1

        params.id = listing.id

        controller.delete()

        assert Listing.count() == 0
        assert Listing.get(listing.id) == null
        assert response.redirectedUrl == '/listing/list'
    }
}
