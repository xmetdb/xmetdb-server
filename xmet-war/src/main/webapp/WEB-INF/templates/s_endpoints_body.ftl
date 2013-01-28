<#include "/s_html.ftl">
<head>
  <#include "/s_head.ftl">
  <script type='text/javascript' src='${xmet_root}/scripts/jendpoints.js'></script>
  <script type='text/javascript' src='${xmet_root}/jquery/jquery.jeditable.js'></script>
  <script type='text/javascript' src='${xmet_root}/jquery/jquery.dataTables.editable.js'></script>
  <script type='text/javascript' src='${xmet_root}/jquery/jquery.validate.min.js'></script>
  <script type='text/javascript'>
$(document).ready(function() {
	$.ajaxSetup({cache:false});//while dev
  	var oTable = defineEndpointsTable("${xmet_request_json}","${xmet_root}");

    <!-- Details panel -->	
	$('#endpoints tbody td .zoomxmet').live(
			'click',
			function() {
				var nTr = $(this).parents('tr')[0];
				if (oTable.fnIsOpen(nTr)) {
					$(this).removeClass("ui-icon-folder-open");
					$(this).addClass("ui-icon-folder-collapsed");
					this.title='Click to show XMetDB observations';
					oTable.fnClose(nTr);
				} else {
					$(this).removeClass("ui-icon-folder-collapsed");
					$(this).addClass("ui-icon-folder-open");
					this.title='Click to close XMetDB observations list';
					var id = 'v'+getID();
					oTable.fnOpen(nTr, fnEndpointXMETDBList(oTable,nTr,id),	'details');
											       
				}
		});
} );
</script>

<script type="text/javascript">
jQuery(document).ready(function()
{
    jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}/catalog" title="XMetDB enzymes">Enzymes</a></li>');
    jQuery("#breadCrumb").jBreadCrumb();
})
</script>
</head>
<body>

<div class="container columns" style="margin:0;padding:0;">
		<#include "/s_banner.ftl">
	 	<#include "/s_menu_enzyme.ftl">
		
		<!-- Page Content
		================================================== -->
		<div class="twelve columns ui-widget-content ui-corner-all" style="padding:0;" >
		<div class='row remove-bottom'>
		</div>
		<table id='endpoints' class='row' cellpadding='0' border='0' width='100%' cellspacing='0' style="margin:0;padding:0;" >
		<thead>
		<th>XMetDB observations</th>
		<th>Code</th>
		<th>Name</th>
		<th>UniProt ID</th>
		<th>Alleles</th>
		</thead>
		<tbody></tbody>
		</table>
		
		</div>
		
		<!-- Right column and footer
		================================================== -->
		<#include "/s_help.ftl">
		<#include "/s_footer.ftl">
		
	<form id="formAddNewEnzyme" action="#" title="Add new enzyme">
	    <label for="code">Enzyme code</label><input type="text" name="code" size="16" id="code" class="required" rel="0" />
	    <label for="name">Enzyme name</label><input type="text" size="255" name="name" id="name" class="required" rel="1" />
	    <label for="uniprot">UNIPROT ID</label><input type="text" name="uniprot" id="uniprot"/>
	    <label for="alleles">Alleles</label><textarea name="alleles" id="alleles" rel="2" ></textarea>
	</form>				
</div><!-- container -->

		<#include "/scripts-welcome.ftl">
		

</body>
</html>