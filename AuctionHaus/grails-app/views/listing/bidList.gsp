<%--
  Created by IntelliJ IDEA.
  User: hcmobile1
  Date: 4/21/12
  Time: 4:27 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="auctionhaus.Bid" contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title></title>
</head>
<body>


<ol class="property-list listing">


    <li class="fieldcontain">
        <span id="listingName-label" class="property-label"><g:message code="listing.listingName.label" default="Bid successfully placed"  /></span>


    </li>
    <li class="fieldcontain">
        <span id="listingName-label" class="property-label"><g:message code="listing.listingName.label" default="Minimum Bid Price" /></span>
        <div class="fieldcontain">
            <label for="minBidAmountField">

                <span class="required-indicator"> "${ !Bid.highestBidForListingId(listingInstance?.id)?.list().isEmpty() ? Bid.highestBidForListingId(listingInstance?.id).list().first().bidAmount + 0.5 : 0.5}"</span>
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


</body>
</html>