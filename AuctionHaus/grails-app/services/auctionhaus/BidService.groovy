package auctionhaus

class BidService {

    def createBid(Bid bidInstance) {

        if (bidInstance?.listing?.listingEndDateTime ? bidInstance?.listing?.listingEndDateTime.compareTo(new Date()) < 0 : false)
        {
            return null
        }


        return bidInstance.save(flush: true)

    }
}
