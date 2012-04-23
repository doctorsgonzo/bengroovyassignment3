package auctionhaus

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion

// Ben Williams

class Bid {
    BigDecimal bidAmount
    Date    bidDateTime
    //Bids are required to be for a listing (unit Test)
    // Bids are required to have a bidder(unit Test)
    static belongsTo = [listing : Listing, bidder :Customer]

//UI-1: The listing detail page will asynchronously load and display a list of the last 10 bids placed on the item showing the user, date/time, and amount. The implementation of the lookup of these results must be done with a Named Query. (Integration Test)
    static namedQueries = {
        bidsForListingId { listingID ->
            eq("listing.id", listingID )
            order("bidDateTime", "desc")
            maxResults(10)

        }


        highestBidForListingId { listingID ->
            eq("listing.id", listingID )
            order("bidAmount", "desc")
            maxResults(1)

        }


    }

    static constraints = {
        // Bid Date time is a required field
        bidDateTime blank:false
        // Bid are required to have a Bid amount
        // The Bid amount must be at least .50 higher than the previous Bid for the same listing (integration test)
        bidAmount nullable: false, validator: {val, obj ->
            if (obj.listing) {
                //check whether bid value is al least .50 higher than the previous Bid for the listing
                def isGood = (val >= (obj.listing.getNextHighestBid(val, obj.bidDateTime) + 0.5))
                if (!isGood) {
                    return ('The Bid amount must be at least .50 higher than the previous Bid for the same listing')
                }
                return isGood
            }

        }




    }


}

