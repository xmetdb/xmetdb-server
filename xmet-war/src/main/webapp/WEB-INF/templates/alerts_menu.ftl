			<#assign freq_hint='Optional, if no alert frequency is selected, retrieve your saved search from the profile page.'>
				<#assign alert_hint='Save your search and configure the frequency of e-mail update alerts.'>
				
				<a class='half-bottom' title='${username}, ${alert_hint}' href='#' onClick="$('#saveSearch').toggle(); return false;">Save this search</a>
	
				<div class="ui-widget-content ui-corner-all remove-bottom" style='display: none;' id='saveSearch'>
				<div style="padding:5px;" class='help;'>
				<form action='${xmet_root}/myaccount/alert' method='POST' class='remove-bottom'>
					<input type='hidden' name='name' value='/protocol'/>
					<input type='hidden' name='query' value='${xmet_query}'/>
					<input type='hidden' name='qformat' value='FREETEXT'/>
					<input type='hidden' name='username' value='${username}'/>
					<label for='rfrequency' title='${freq_hint}'>Frequency of e-mail alert</label>
					<select class='half-bottom' name='rfrequency' style='width:8em;'>;
					  <option value="monthly" >Monthly</option>
					  <option value="weekly">Weekly</option>
					  <option value="daily">Daily</option>				
					  <option value="" title='${freq_hint}'>Never</option>
				    </select>
					<input class='remove-bottom' type='submit' title='${alert_hint} ${freq_hint}' value='Save'/>
				</form>
				</div>
				</div>