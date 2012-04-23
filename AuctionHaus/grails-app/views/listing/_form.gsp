<%@ page import="auctionhaus.Listing" %>



<div class="fieldcontain ${hasErrors(bean: listingInstance, field: 'listingName', 'error')} required">
	<label for="listingName">
		<g:message code="listing.listingName.label" default="Listing Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="listingName" maxlength="63" required="" value="${listingInstance?.listingName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: listingInstance, field: 'startingBidPrice', 'error')} required">
	<label for="startingBidPrice">
		<g:message code="listing.startingBidPrice.label" default="Starting Bid Price" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="startingBidPrice" required="" value="${fieldValue(bean: listingInstance, field: 'startingBidPrice')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: listingInstance, field: 'listingDescription', 'error')} ">
	<label for="listingDescription">
		<g:message code="listing.listingDescription.label" default="Listing Description" />
		
	</label>
	<g:textArea name="listingDescription" cols="40" rows="5" maxlength="255" value="${listingInstance?.listingDescription}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: listingInstance, field: 'listingEndDateTime', 'error')} required">
	<label for="listingEndDateTime">
		<g:message code="listing.listingEndDateTime.label" default="Listing End Date Time" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="listingEndDateTime" precision="day"  value="${listingInstance?.listingEndDateTime}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: listingInstance, field: 'winner', 'error')} ">
	<label for="winner">
		<g:message code="listing.winner.label" default="Winner" />
		
	</label>
	<g:select id="winner" name="winner.id" from="${auctionhaus.Customer.list()}" optionKey="id" value="${listingInstance?.winner?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>



<div class="fieldcontain ${hasErrors(bean: listingInstance, field: 'listingCreatedDate', 'error')} required">
	<label for="listingCreatedDate">
		<g:message code="listing.listingCreatedDate.label" default="Listing Created Date" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="listingCreatedDate" precision="day"  value="${listingInstance?.listingCreatedDate}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: listingInstance, field: 'seller', 'error')} required">
	<label for="seller">
		<g:message code="listing.seller.label" default="Seller" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="seller" name="seller.id" from="${auctionhaus.Customer.list()}" optionKey="id" required="" value="${listingInstance?.seller?.id}" class="many-to-one"/>
</div>

