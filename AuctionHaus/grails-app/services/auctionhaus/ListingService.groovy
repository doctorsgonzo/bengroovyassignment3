package auctionhaus

class ListingService {

    def createListing(Listing listingInstance) {

        return listingInstance.save(flush: true)
        
    }
}
