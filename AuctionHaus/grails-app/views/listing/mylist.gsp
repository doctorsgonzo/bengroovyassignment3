
<%@ page import="auctionhaus.Listing" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'listing.label', default: 'My Listings')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<a href="#list-listing" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="createuserlisting"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
    </ul>
</div>
<div id="list-listing" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <g:sortableColumn property="listingName" title="${message(code: 'listing.listingName.label', default: 'Listing Name')}" />

            <g:sortableColumn property="startingBidPrice" title="${message(code: 'listing.startingBidPrice.label', default: 'Starting Bid Price')}" />

            <g:sortableColumn property="listingEndDateTime" title="${message(code: 'listing.listingEndDateTime.label', default: 'Listing End Date Time')}" />



            <th><g:message code="listing.winner.label" default="Winner" /></th>

            <g:sortableColumn property="listingCreatedDate" title="${message(code: 'listing.listingCreatedDate.label', default: 'Listing Created Date')}" />

            <th><g:message code="listing.numberOfBids.label" default="Number of Bids" /></th>

        </tr>
        </thead>
        <tbody>
        <g:each in="${listingInstanceList}" status="i" var="listingInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show" id="${listingInstance.id}">${fieldValue(bean: listingInstance, field: "listingName")}</g:link></td>

                <td>${fieldValue(bean: listingInstance, field: "startingBidPrice")}</td>



                <td><g:formatDate date="${listingInstance.listingEndDateTime}" /></td>

                <td>${fieldValue(bean: listingInstance, field: "winner")}</td>

                <td><g:formatDate date="${listingInstance.listingCreatedDate}" /></td>

                <td>${listingInstance.bids ? listingInstance.bids.size() : 0} </td>

            </tr>
        </g:each>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${listingInstanceTotal}" />
    </div>
</div>
</body>
</html>
