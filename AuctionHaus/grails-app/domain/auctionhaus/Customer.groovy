package auctionhaus

import com.grailsrocks.authentication.AuthenticationService

// Ben Williams

class Customer {
    String login
    int status = AuthenticationService.STATUS_NEW     // must be set to AuthenticationService.STATUS_NEW at init
    String email
    String password
    Date   createdDate
    boolean admin = false

    static hasMany = [listings: Listing, bids: Bid]
    static mappedBy = [listings: "seller", bids: "bidder"]

    static constraints = {
        
        login nullable:true
        status nullable:true
        admin nullable:true


        email email:true,blank:false,unique: true
        password blank:false
        createdDate nullable: false,blank:false

    }
}
