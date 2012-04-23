package auctionhaus

class CustomerService {

    def createCustomer( Customer customerInstance ) {

        
        return customerInstance.save(flush: true)
        
    }
}
