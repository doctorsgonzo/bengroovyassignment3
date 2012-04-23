package auctionhaus

import org.springframework.dao.DataIntegrityViolationException
import com.sun.tools.javac.util.List

class ListingController {

    def authenticationService

    def listingService
    def bidService
    
    def bidSuccess = false;
    def bidError = false;
    def bidInstance

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {


       // params.max = Math.min(params.max ? params.int('max') : 10, 100)
     //   M-2: The main landing page shows up to 5 listings at a time
        params.max = 5

        
        //M-1: The main landing page shows listings sorted by the date they were created (most recent first)
        if (!params.sort) //check that the user hasn't chosen to sort on a different parameter
        {
            params.sort = 'listingCreatedDate'
            params.order = 'desc'
        }



       // M-4: Only listings with a end date/time that is in the future are visible on the main page
        //this is the suggested solution from the assignment feedback:
        
        def listingInstance = Listing.findAllByListingEndDateTimeGreaterThan(new Date(), params)

        [listingInstanceList: listingInstance, listingInstanceTotal: Listing.countByListingEndDateTimeGreaterThan(new Date()) ]
        

    }

    def mylist() {
        // params.max = Math.min(params.max ? params.int('max') : 10, 100)
        //   M-2: The main landing page shows up to 5 listings at a time
        params.max = 5
                                

        //M-1: The main landing page shows listings sorted by the date they were created (most recent first)
        if (!params.sort) //check that the user hasn't chosen to sort on a different parameter
        {
            params.sort = 'listingCreatedDate'
            params.order = 'desc'
        }



        // M-4: Only listings with a end date/time that is in the future are visible on the main page
        //this is the suggested solution from the assignment feedback:

        def listingInstance = Listing.findAllBySeller(Customer.findByLogin(auth.user()))
            //Listing.findAllByListingEndDateTimeGreaterThan(new Date(), params)
                                                                 

        [listingInstanceList: listingInstance, listingInstanceTotal: Listing.findAllBySeller(Customer.findByLogin(auth.user())) ]


    }

     //the create listing is now an admin only page since it allows you to create a listing for any customer
    //if you are not an admin user, trying to access this page will redirect to the main page (see the AdministratorFilter)
    def create() {
        [listingInstance: new Listing(params)]
    }

   //   S-6: A logged in user can create a new listing via a simple form; newly created listings will appear in the "My Listings" view

    def createuserlisting() {


        if (!authenticationService.isLoggedIn(request))
        {
            render("Must be logged in to create listing")
        }
                 
        //params.put("seller",  Customer.findByLogin(auth.user()))
        def listingInstance = new Listing(params)

        
        [listingInstance: listingInstance]
    }




    
    def createbid() {
        
         bidInstance = new Bid(params)
        
        bidInstance.bidDateTime = new Date()
        bidInstance.bidder = Customer.findByLogin(auth.user())


        bidInstance.bidAmount = Float.parseFloat(params.bidAmountField)



        if (authenticationService.isLoggedIn(request))
        {
            bidError = false
        }
        else
        {
            bidError=true
        }

        def bidServiceResult = bidService.createBid(bidInstance)

        if (bidError || !bidServiceResult)
        {
            
            if (!bidServiceResult)
            {
                bidError = true
            }
     
            render(view: "bidListFailedBid", model: [listingInstance: bidInstance.listing])
            return
        }
        bidSuccess = true
     //   flash.message = message(code: 'default.created.message', args: [message(code: 'listing.label', default: 'Bid'), bidInstance.id])
        render(view: "bidList", model: [listingInstance: bidInstance.listing])
        [listingInstance: bidInstance.listing]




    }





    def refreshbid() {

      //  def bidInstance = new Bid(params)

      //  bidInstance.bidDateTime = new Date()
      ////  bidInstance.bidder = Customer.findByLogin(auth.user())

      //  bidInstance.bidAmount = Float.parseFloat(params.bidAmountField)


        if (bidError)
        {
           
            render(view: "bidListFailedBid", model: [listingInstance: bidInstance.listing])
            return
        }

        //flash.message = message(code: 'default.created.message', args: [message(code: 'listing.label', default: 'Bid'), bidInstance.id])
        if (bidSuccess)
        {
        render(view: "bidList", model: [listingInstance: bidInstance.listing])
        [listingInstance: bidInstance.listing]
        }


    }
    
    
    
    
    def save() {
        def listingInstance = new Listing(params)


        if (!listingService.createListing(listingInstance)) {
            render(view: "create", model: [listingInstance: listingInstance])
            return
        }

     
		flash.message = message(code: 'default.created.message', args: [message(code: 'listing.label', default: 'Listing'), listingInstance.id])
        redirect(action: "show", id: listingInstance.id)
    }



    def saveuserlisting() {
        def listingInstance = new Listing(params)


        listingInstance.seller = Customer.findByLogin(auth.user())

        if (!listingService.createListing(listingInstance)) {
            render(view: "create", model: [listingInstance: listingInstance])
            return
        }


        flash.message = message(code: 'default.created.message', args: [message(code: 'listing.label', default: 'Listing'), listingInstance.id])
        redirect(action: "show", id: listingInstance.id)
    }


    def hideDescription() {
        def listingInstance = Listing.get(params.id)

        listingInstance.showDescription = false


        redirect(action: "show", id: listingInstance.id)


    }


    def showDescription() {
        def listingInstance = Listing.get(params.id)

        listingInstance.showDescription = true


        redirect(action: "show", id: listingInstance.id)


    }




    def show() {
        def listingInstance = Listing.get(params.id)
        if (!listingInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'listing.label', default: 'Listing'), params.id])
            redirect(action: "list")
            return
        }

        if (listingInstance.listingEndDateTime.compareTo(new Date()) < 0)
        {
            render("Listing expired")
        }
        
        
        [listingInstance: listingInstance]
    }

    def edit() {
        def listingInstance = Listing.get(params.id)
        if (!listingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'listing.label', default: 'Listing'), params.id])
            redirect(action: "list")
            return
        }

        [listingInstance: listingInstance]
    }

    def update() {
        def listingInstance = Listing.get(params.id)
        if (!listingInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'listing.label', default: 'Listing'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (listingInstance.version > version) {
                listingInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'listing.label', default: 'Listing')] as Object[],
                          "Another user has updated this Listing while you were editing")
                render(view: "edit", model: [listingInstance: listingInstance])
                return
            }
        }

        listingInstance.properties = params

        if (!listingInstance.save(flush: true)) {
            render(view: "edit", model: [listingInstance: listingInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'listing.label', default: 'Listing'), listingInstance.id])
        redirect(action: "show", id: listingInstance.id)
    }

    def delete() {
        def listingInstance = Listing.get(params.id)
        if (!listingInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'listing.label', default: 'Listing'), params.id])
            redirect(action: "list")
            return
        }

        try {
            listingInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'listing.label', default: 'Listing'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'listing.label', default: 'Listing'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
