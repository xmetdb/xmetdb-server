	<div class="row half-bottom ">	
     	<div class="two columns alpha"><label for="xmet_substrate">Substrate:<a href='#' class='chelp substrate'></a></label>
     		<br/>
     		<div id="sim_substrate"></div>
     	</div>
      		<div class="six columns omega">
      				<ul class='structresults' id="xmet_substrate"></ul>
      		</div>
    </div>		
	<div class="row half-bottom ">	
     	<div class="two columns alpha"><label for="xmet_product">Product:<a href='#' class='chelp product'></a></label>
     	<br/>
     	<div id="sim_product"></div>
     	</div>
      		<div class="six columns omega">
      			<ul class='structresults' id="xmet_product"></ul>
      		</div>
    </div>		
	<div class="row half-bottom ">	
     	<div class="three columns alpha"><label for="xmet_atom_uncertainty">Atom uncertainty:<a href='#' class='chelp atomuncertainty'></a></label></div>
      	<div class="three columns omega">
      		<span id="xmet_atom_uncertainty">&nbsp;</span>
      	</div>
       	<div class="three columns omega"><label for="xmet_product_amount">Product amount:<a href='#' class='chelp productamount'></a></label></div>
      	<div class="two columns omega">
      		<span id="xmet_product_amount">&nbsp;</span>
      	</div>
    </div>	    
	<div class="row half-bottom ">	
     	<div class="three columns alpha"><label for="xmet_experiment">Experiment:<a href='#' class='chelp experiment'></a></label></div>
      		<div class="six columns omega">
				<span id="xmet_experiment">&nbsp;</span>
      		</div>
    </div>		    
	<div class="row half-bottom ">	
     	<div class="three columns alpha"><label for="xmet_enzyme">Enzyme:<a href='#' class='chelp enzyme'></a></label></div>
      		<div class="eight columns omega">
      			<span id="xmet_enzyme">&nbsp;</span>
      		</div>
      		<div class="one column omega"><label>Allele</label></div>
 			<div class="two columns omega">
      			<span id="xmet_allele">&nbsp;</span>
      		</div>      		
    </div>	   
    <div class="row half-bottom">	
     	<div class="three columns alpha"><label for="xmet_reference">Reference:<a href='#' class='chelp reference'></a></label></div>
      		<div class="six columns omega">
      				<span id="xmet_reference">&nbsp;</span>
      				<a href='http://dx.doi.org/' target='doi' title='' id='xmet_doi' ><span id="info-link" class="ui-icon ui-icon-link" style="display: inline-block;"></span></a> 
      				<a href='#' id='sim_reference' title='Search for observations with the same reference'><span id="info-sim-ref" class="ui-icon ui-icon-search" style="display: inline-block;"></span></a>
      		</div>
    </div>	
	
	<div class='row half-bottom' > 	
		<div class='three columns alpha'>
			<label>Comment:</label>
		</div>
		<textarea name="xmet_comments" id="xmet_comments" title="Note" value="" row="1" class="eight columns omega remove-bottom" readonly></textarea>
		<div class='five columns omega'>&nbsp;</div>
	</div>
	<div class='row half-bottom' > 	
		<div class='three columns alpha'>
			<label>Status:</label>
		</div>
		<div class='two columns omega' id="xmet_curated"></div>
		<div class="two columns omega"><img id='imgcurated' style='display:none;' src='${xmet_root}/images/star.png'></div>
		<div class='four columns omega' ></div>
		<div class='two columns omega'><a href='#' title='Links' id='xmet_links'></a></div>
	</div>	
	<div class='row half-bottom' style="margin:5px;padding:5px;"><hr class='remove-bottom'/></div>		

	<div class='row remove-bottom' >
		<#if username??>
			<div class='three columns alpha'>Submitted by</div>
			<div class='three columns omega'><a href='${xmet_root}/user' id='submittedby'></a></div>
		<#else>
			<div class='six columns alpha'>&nbsp;</div>
		</#if>
		<div class='ten columns omega'>
			<span id='modifyURI'>
			<a id="xmet_editor" href="${xmet_root}/editor/"  title='Modify the observation'>Modify this observation</a><a href='#' class='chelp modifyobs'></a>
			|
			<a id="xmet_editorsom" href="${xmet_root}/protocol/"  title='Modify sites of metabolism'>Modify Sites of Metabolism</a><a href='#' class='chelp modifysom'></a>			
			|
			</span>
			<a title='Create new observation with the same content as this one.' href='#' onClick='document.forms["copyForm"].submit(); return false;'>Copy and create new observation</a><a href='#' class='chelp copyobs'></a>
			<#if xmetdb_curator?? && xmetdb_curator>
			|
			<a title='Curate this observation.'  href="${xmet_root}/curator/" id='xmet_curator'>Curate</a><a href='#' class='chelp hcurator'></a>
			</#if>						
			<form method='POST' name='copyForm' action='${xmet_root}/protocol' enctype='application/x-www-form-urlencoded' autocomplete name='xmet_copy'>
			<input type='hidden' name='source_uri' value='${xmet_request}'>
			</form>

		</div>
	</div>	