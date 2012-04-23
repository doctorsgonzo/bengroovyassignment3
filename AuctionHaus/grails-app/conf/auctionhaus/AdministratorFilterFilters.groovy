package auctionhaus

class AdministratorFilterFilters {

    def filters = {
        all(controller:'*', action:'*') {
            before = {

            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }


        adminFilter(uri:"/customer/**") {
            // Redirect to main landing page if not logged in
            before = {

                    return applicationContext.authenticationService.filterRequest( request,
                            response, "${request.contextPath}/" )

            }
        }

        adminFilterListing(uri:"/listing/create") {
            // Redirect to main landing page if not logged in
            before = {

                return applicationContext.authenticationService.filterRequest( request,
                        response, "${request.contextPath}/" )

            }
        }




    }
}
