package auctionhaus



import org.junit.*
import grails.test.mixin.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.groovy.grails.commons.GrailsApplication

@TestFor(CustomerController)
@Mock(Customer)
class CustomerControllerTests {



    def populateValidParams(params) {
      assert params != null
        
        
        params["email"] = "test@emailaddress.com"
        params["password"] = "password"
        params["createdDate"] = new Date()

    }


    def populateValidParamsForBids(params) {


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


    def populateValidParamsForListing(params) {
        assert params != null

        def testEndDateTime = new Date() + 1
        def testSeller = new Customer(email:"amit_thakore1@yahoo.com",password: "1234567",createdDate: new Date())

        params["listingName"] = "Apple TV"
        params["listingEndDateTime"] = testEndDateTime
        params["startingBidPrice"] = 10.00
        params["seller"] = testSeller
    }

    void testIndex() {
        controller.index()
        assert "/customer/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.customerInstanceList.size() == 0
        assert model.customerInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.customerInstance != null
    }

    void testSave() {

        controller.customerService = new CustomerService()
         //mock config statement to throw exception on validation error
   /*     mockConfig( '''
grails.gorm.failOnError = true
        ''')   */

       // grailsApplication.config.grails.gorm.failOnError = true
     //   controller.grailsApplication = grailsApplication
       // def grailsApplication
    //    controller.grailsApplication.config.grails.gorm.failOnError = true
      //  grailsApplication.config.grails.gorm.failOnError = true

   //     def mockedConfig = new ConfigObject()
   //     mockedConfig.grails.gorm.failOnError = true

  //      GrailsApplication.metaClass.getConfig = {-> mockedConfig }
                   //     GrailsApplication.metaClass.
       // def cfg = [config: [grails: [ gorm : [failOnError: true]]]]

       //grailsApplication = cfg


    //    GrailsApplication.metaClass.getConfig = {-> mockedConfig }

      //  controller.grailsApplication.config
       // controller.grailsApplication = [config: mockedConfig]
      // controller.grailsApplication  = {-> mockedConfig }
      // grailsApplication = {-> mockedConfig }
     //   ConfigurationHolder.config = mockedConfig
     //  grailsApplication = [config: mockedConfig]
       // controller.grailsApplication.config =new ConfigSlurper().parse("""
         //       grails.gorm.failOnError = true
           //     """)


     //   grailsApplication.config.grails.gorm.failOnError = true
    //    controller.grailsApplication = grailsApplication
    //    grailsApplication.config.grails.gorm.failOnError = true

        //couldn't get mock config to work to make this test pass...
        controller.save()

        assert model.customerInstance != null
        assert view == '/customer/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/customer/show/1'
        assert controller.flash.message != null
        assert Customer.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/customer/list'


        populateValidParams(params)
        def customer = new Customer(params)

        assert customer.save() != null

        params.id = customer.id

        def model = controller.show()

        assert model.customerInstance == customer
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/customer/list'


        populateValidParams(params)
        def customer = new Customer(params)

        assert customer.save() != null

        params.id = customer.id

        def model = controller.edit()

        assert model.customerInstance == customer
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/customer/list'

        response.reset()


        populateValidParams(params)
        def customer = new Customer(params)

        assert customer.save() != null

        // test invalid parameters in update
        params.id = customer.id

        params["email"] = "not_a_valid_email"


        controller.update()

        assert view == "/customer/edit"
        assert model.customerInstance != null

        customer.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/customer/show/$customer.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        customer.clearErrors()

        populateValidParams(params)
        params.id = customer.id
        params.version = -1
        controller.update()

        assert view == "/customer/edit"
        assert model.customerInstance != null
        assert model.customerInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    //this test checks that you can't delete a customer if there are no customers,
    //and also tests that you can successfully delete a customer with no listings or bids
    void testDeleteSucceedsForCustomerWithZeroBidsAndZeroListings() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/customer/list'

        response.reset()

        populateValidParams(params)
        def customer = new Customer(params)

        assert customer.save() != null
        assert Customer.count() == 1

        params.id = customer.id

        //customer with no bids or listings can be deleted

        controller.delete()

        assert Customer.count() == 0
        assert Customer.get(customer.id) == null
        assert response.redirectedUrl == '/customer/list'


    }




        //this tests that trying to delete a customer who has a bid causes an error
    void testDeleteFailsForCustomerWithBids() {


        populateValidParams(params)
        def customer = new Customer(params)


        populateValidParamsForBids(params)

        customer.addToBids(new Bid(params))

        assert customer.save() != null
        assert Customer.count() == 1

        params.id = customer.id

        //customer with bid cannot be deleted

        controller.delete()

        //delete should fail since customer has a bid
        assert Customer.count() == 1
        assert flash.message != null
        assert response.redirectedUrl == '/customer/show/1'


    }


    //this tests that trying to delete a customer who has a listing causes an error
    void testDeleteFailsForCustomerWithListing() {


        populateValidParams(params)
        def customer = new Customer(params)


        populateValidParamsForListing(params)

        customer.addToListings(new Listing(params))

        assert customer.save() != null
        assert Customer.count() == 1

        params.id = customer.id

        //customer with listing cannot be deleted

        controller.delete()

        //delete should fail since customer has a listing
        assert Customer.count() == 1
        assert flash.message != null
        assert response.redirectedUrl == '/customer/show/1'


    }

}
