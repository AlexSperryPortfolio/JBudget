<%@include file="includes/header.jsp" %>
<%@include file="includes/navbar.jsp" %>
<div class="container">

	<input class="form-control mr-sm-2" placeholder="Search" type="text">
	<button class="btn btn-secondary my-2 my-sm-0" type="button" onClick="applyFilter()">Search</button>
	
	<table class="table table-striped table-hover">
    	<thead>
        	<tr>
            	<th class="text-center">Account Number</th>
            	<th class="text-center">Balance</th>
            	<th class="text-center">Check Column</th>
            	<th class="text-center">Credit</th>
            	<th class="text-center">Debit</th>
            	<th class="text-center">Description</th>
            	<th class="text-center">Post Date</th>
            	<th class="text-center">Status</th>
            	<th class="text-center">Type</th>
            </tr>
        </thead>
        <tbody>
        <c:set var = "credit" scope = "session" value = ""/>
        <c:set var = "debit" scope = "session" value = ""/>
        	<c:forEach var="transaction" items="${transactionList}">
        		<tr>
            		<td class="text-center">${transaction.accountNumber}</td>
                	<td class="text-center">${transaction.balance}$</td>
                	<td class="text-center">${transaction.checkColumn}</td>
                	<c:choose>
                	    <c:when test="${credit != transaction.credit}">
                	        <td class="text-center">${transaction.credit}$</td>
                	    </c:when>
                	    <c:otherwise>
                	        <td class="text-center"></td>
                	    </c:otherwise>
                	</c:choose>
                	<c:choose>
                        <c:when test="${debit != transaction.debit}">
                            <td class="text-center">${transaction.debit}$</td>
                        </c:when>
                        <c:otherwise>
                            <td class="text-center"></td>
                        </c:otherwise>
                    </c:choose>
                	<td class="text-center">${transaction.description}</td>
                	<td class="text-center">${transaction.postDate}</td>
                	<td class="text-center">${transaction.status}</td>

                    <c:set var = "credit" scope = "session" value = "${transaction.credit}"/>
                    <c:set var = "debit" scope = "session" value = "${transaction.debit}"/>
				</tr>
        	</c:forEach>
        </tbody>
	</table>
</div>

<%@include file="includes/footer.jsp" %>