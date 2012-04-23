
<%@ page import="auctionhaus.Bid; auctionhaus.Listing" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'listing.label', default: 'Listing')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>


    <g:javascript library="jquery" plugin="jquery"></g:javascript>

    <g:javascript>
    window.setInterval(update, 5000);

    function update() {
        jQuery.ajax({url:'listing/refreshbid',
            success: function (d) {
                jQuery('#list-bid').replaceWith(d);

            }})
    }

    </g:javascript>

		<a href="#show-listing" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="createuserlisting"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-listing" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list listing">
			
				<g:if test="${listingInstance?.listingName}">
				<li class="fieldcontain">
					<span id="listingName-label" class="property-label"><g:message code="listing.listingName.label" default="Listing Name" /></span>
					
						<span class="property-value" aria-labelledby="listingName-label"><g:fieldValue bean="${listingInstance}" field="listingName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${listingInstance?.startingBidPrice}">
				<li class="fieldcontain">
					<span id="startingBidPrice-label" class="property-label"><g:message code="listing.startingBidPrice.label" default="Starting Bid Price" /></span>
					
						<span class="property-value" aria-labelledby="startingBidPrice-label"><g:fieldValue bean="${listingInstance}" field="startingBidPrice"/></span>
					
				</li>
				</g:if>

                <g:if test="${listingInstance?.showDescription}">
			    	<g:if test="${listingInstance?.listingDescription}">
			    	<li class="fieldcontain">
				    	<span id="listingDescription-label" class="property-label"><g:message code="listing.listingDescription.label" default="Listing Description" /></span>


                            <span class="property-value" aria-labelledby="seller-label"><g:link controller="listing" action="hideDescription" id="${listingInstance?.id}">Hide Description</g:link></span>


						    <span class="property-value" aria-labelledby="listingDescription-label"><g:fieldValue bean="${listingInstance}" field="listingDescription"/></span>
					
		    		</li>
			    	</g:if>
                </g:if>


                <g:if test="${!listingInstance?.showDescription}">
                    <g:if test="${listingInstance?.listingDescription}">
                        <li class="fieldcontain">


                            <span class="property-value" aria-labelledby="seller-label"><g:link controller="listing" action="showDescription" id="${listingInstance?.id}">Show Description</g:link></span>



                        </li>
                    </g:if>
                </g:if>





				<g:if test="${listingInstance?.listingEndDateTime}">
				<li class="fieldcontain">
					<span id="listingEndDateTime-label" class="property-label"><g:message code="listing.listingEndDateTime.label" default="Listing End Date Time" /></span>
					
						<span class="property-value" aria-labelledby="listingEndDateTime-label"><g:formatDate date="${listingInstance?.listingEndDateTime}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${listingInstance?.winner}">
				<li class="fieldcontain">
					<span id="winner-label" class="property-label"><g:message code="listing.winner.label" default="Winner" /></span>
					
						<span class="property-value" aria-labelledby="winner-label"><g:link controller="customer" action="show" id="${listingInstance?.winner?.id}">${listingInstance?.winner?.encodeAsHTML()}</g:link></span>

				</li>
				</g:if>
			
				<g:if test="${listingInstance?.bids}">
				<li class="fieldcontain">
					<span id="bids-label" class="property-label"><g:message code="listing.bids.label" default="Most Recent Bid" /></span>

						<span class="property-value" aria-labelledby="bids-label"><g:link controller="bid" action="show" id="${listingInstance?.getMostRecentBid()?.id}">${listingInstance?.getMostRecentBid()?.encodeAsHTML()}</g:link></span>

					
				</li>
				</g:if>
			
				<g:if test="${listingInstance?.listingCreatedDate}">
				<li class="fieldcontain">
					<span id="listingCreatedDate-label" class="property-label"><g:message code="listing.listingCreatedDate.label" default="Listing Created Date" /></span>
					
						<span class="property-value" aria-labelledby="listingCreatedDate-label"><g:formatDate date="${listingInstance?.listingCreatedDate}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${listingInstance?.seller}">
				<li class="fieldcontain">
					<span id="seller-label" class="property-label"><g:message code="listing.seller.label" default="Seller" /></span>
					
						<span class="property-value" aria-labelledby="seller-label"><g:link controller="customer" action="show" id="${listingInstance?.seller?.id}">${listingInstance?.seller?.email?.tokenize("@")?.first().encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>





<g:formRemote name="myform" method="post" update="list-bid" url="[action:'createbid', params:['listing.id': listingInstance?.id  ]]">
    <li class="fieldcontain">
                    <div class="fieldcontain">
                        <label for="bidAmountField">

                            <span class="required-indicator">Bid Amount</span>
                        </label>
                        <g:field type="number" step="0.1" name="bidAmountField" required="" />
                    </div>


                    <span class="button"><g:actionSubmit class="save" action="update" value="Place Bid" /></span>

                </li>
 </g:formRemote>


    </ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${listingInstance?.id}" />
					<g:link class="edit" action="edit" id="${listingInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>

    <div id="list-bid" class="content scaffold-list" role="main">


        <ol class="property-list listing">


                <li class="fieldcontain">
                    <span id="listingName-label" class="property-label"><g:message code="listing.listingName.label" default="Minimum Bid Price" /></span>
                    <div class="fieldcontain">
                        <label for="minBidAmountField">

                            <span class="required-indicator"> "${ !Bid.highestBidForListingId(listingInstance?.id)?.list().isEmpty() ? Bid.highestBidForListingId(listingInstance?.id).list().first().bidAmount + 0.5 : 0.5 }"</span>
                        </label>

                </li>

         </ol>


        <table>
            <thead>
            <tr>

                <g:sortableColumn property="bidDateTime" title="${message(code: 'bid.bidDateTime.label', default: 'Bid Date Time')}" />

                <g:sortableColumn property="bidAmount" title="${message(code: 'bid.bidAmount.label', default: 'Bid Amount')}" />

                <th><g:message code="bid.bidder.label" default="Bidder" /></th>

                <th><g:message code="bid.listing.label" default="Listing" /></th>

            </tr>
            </thead>
            <tbody>
            <g:each in="${Bid.bidsForListingId(listingInstance?.id).list()}" status="i" var="bidInstance">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                    <td><g:link controller="bid" action="show" id="${bidInstance.id}">${fieldValue(bean: bidInstance, field: "bidDateTime")}</g:link></td>

                    <td>${fieldValue(bean: bidInstance, field: "bidAmount")}</td>

                    <td>${fieldValue(bean: bidInstance, field: "bidder")}</td>

                    <td>${fieldValue(bean: bidInstance, field: "listing")}</td>

                </tr>
            </g:each>
            </tbody>
        </table>

    </div>

	</body>
</html>
