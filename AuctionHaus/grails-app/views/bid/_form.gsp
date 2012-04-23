<%@ page import="auctionhaus.Bid" %>



<div class="fieldcontain ${hasErrors(bean: bidInstance, field: 'bidDateTime', 'error')} required">
	<label for="bidDateTime">
		<g:message code="bid.bidDateTime.label" default="Bid Date Time" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="bidDateTime" precision="day"  value="${bidInstance?.bidDateTime}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: bidInstance, field: 'bidAmount', 'error')} required">
	<label for="bidAmount">
		<g:message code="bid.bidAmount.label" default="Bid Amount" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="bidAmount" required="" value="${fieldValue(bean: bidInstance, field: 'bidAmount')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bidInstance, field: 'bidder', 'error')} required">
	<label for="bidder">
		<g:message code="bid.bidder.label" default="Bidder" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="bidder" name="bidder.id" from="${auctionhaus.Customer.list()}" optionKey="id" required="" value="${bidInstance?.bidder?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bidInstance, field: 'listing', 'error')} required">
	<label for="listing">
		<g:message code="bid.listing.label" default="Listing" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="listing" name="listing.id" from="${auctionhaus.Listing.list()}" optionKey="id" required="" value="${bidInstance?.listing?.id}" class="many-to-one"/>
</div>

