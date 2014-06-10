<select  id="${id}" name="${id}">
	<#if (all == true)  >
		<option value="">ALL</option>
	</#if>
	<#if (list?exists) && (list?size>1)  >
		<#list list as item>
			<#if item.k==value>
				<option value="${item.k}" selected="selected">${item.v}</option>
			<#else>
				<option value="${item.k}">${item.v}</option>
			</#if>
		</#list>
	</#if>
</select>
