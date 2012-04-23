package auctionhaus

import org.springframework.dao.DataIntegrityViolationException

class BidController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [bidInstanceList: Bid.list(params), bidInstanceTotal: Bid.count()]
    }

    def create() {

        [bidInstance: new Bid(params)]
         

    }

   // L-7: The detail page for the listing allows a new bid to be placed (unit test of the controller action that handles this requirement)
    def save() {
        def bidInstance = new Bid(params)  
        
        bidInstance.bidDateTime = new Date()

        if (!bidInstance.validate()) {

            flash.message = bidInstance.errors
            redirect(controller: "listing", action: "show", id: params["listing.id"])
            return
        }


        if (!bidInstance.save(flush: true)) {
            render(view: "create", model: [bidInstance: bidInstance])
            return
        }

      //  flash.message = message(code: 'default.created.message', args: [message(code: 'bid.label', default: 'Bid'), bidInstance.id])
        redirect(controller: "listing", action: "show", id: params["listing.id"], errors:bidInstance.errors)
    }

    def show() {
        def bidInstance = Bid.get(params.id)
        if (!bidInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bid.label', default: 'Bid'), params.id])
            redirect(action: "list")
            return
        }

        [bidInstance: bidInstance]
    }

    def edit() {
        def bidInstance = Bid.get(params.id)
        if (!bidInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bid.label', default: 'Bid'), params.id])
            redirect(action: "list")
            return
        }

        [bidInstance: bidInstance]
    }

    def update() {
        def bidInstance = Bid.get(params.id)
        if (!bidInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bid.label', default: 'Bid'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (bidInstance.version > version) {
                bidInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'bid.label', default: 'Bid')] as Object[],
                        "Another user has updated this Bid while you were editing")
                render(view: "edit", model: [bidInstance: bidInstance])
                return
            }
        }

        bidInstance.properties = params

        if (!bidInstance.save(flush: true)) {
            render(view: "edit", model: [bidInstance: bidInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'bid.label', default: 'Bid'), bidInstance.id])
        redirect(action: "show", id: bidInstance.id)
    }

    def delete() {
        def bidInstance = Bid.get(params.id)
        if (!bidInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bid.label', default: 'Bid'), params.id])
            redirect(action: "list")
            return
        }

        try {
            bidInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'bid.label', default: 'Bid'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'bid.label', default: 'Bid'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
