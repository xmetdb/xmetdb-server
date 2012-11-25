<script type="text/javascript">
	$(document).ready(function() {
		loadEnzymesList("#search_enzyme");
		addSearchUI('#substratesearch','substrateSearchUI','${xmet_root}','substrateSearchForm');
		addSearchUI('#productsearch','productSearchUI','${xmet_root}','productSearchForm');
		$( "#buttonSubstrateSearch" ).click(function() {  toggleSearchUI('#substrateSearchUI','#buttonSubstrateSearch');  return false; });
		$( "#buttonProductSearch" ).click(function() {  toggleSearchUI('#productSearchUI','#buttonProductSearch');  return false; });
		$('form[name="substrateSearchForm"]').removeAttr('onsubmit')
        .submit(function(event){
        	runSearch('${queryService}',$(this).serialize());
            event.preventDefault();
            return false;
        });
		$('form[name="productSearchForm"]').removeAttr('onsubmit')
        .submit(function(event){
        	runSearch('${queryService}',$(this).serialize());
            event.preventDefault();
            return false;
        });        
	});
	
</script>
<script src="http://d3js.org/d3.v2.min.js?2.9.4"></script>

<style>
.background {
  fill: #fff;
}

.bond {
  stroke: black;
  stroke-width:3px;
}


.atom {
  stroke: #eee;
  fill: #eee;
  fill-opacity : 0.5;
  stroke-width:.6px;
}

.atom .text {
  stroke: #000;
  fill-opacity : 1;
}

</style>

<script>

var molecule = { 
	"atoms" : [
		{id: "n0" , x : 91.97, y : -35.50 , atom : false, selected : false},
		{id: "1" , x : 53.00, y: -58.00 , atom : true , selected : true},
		{id: "n1" ,  x : 53.00, y: -51.25 , atom : false , selected : false},
		{id: "2" ,  x : 91.97, y: 9.50 , atom : true , selected : false},
		{id: "3" ,  x : 91.97, y: -35.50, atom : true , selected : false},
		{id: "4" ,  x : 53.00, y: 32.00, atom : true , selected : false},
		{id: "n2" ,  x : 86.13, y: 6.13 , atom : false , selected : false},
		{id: "n3" ,  x : 53.00, y: 25.25 , atom : false , selected : false},
		{id: "5" ,  x : 14.03, y: 9.50, atom : true , selected : false},
		{id: "6" , x : 14.03, y: -35.50 , atom : true , selected : false},
		{id: "n4" ,  x : 19.87, y: 6.12 , atom : false , selected : false},
		{id: "n5" ,  x : 19.87, y: -32.13 , atom : false , selected : false},
		{id: "7" ,  x : -24.94, y: -58.00, atom: true , selected : false},
		{id: "8" ,  x : -24.94, y: 32.00, atom : true , selected : false},
		{id: "9" ,  x : 53.00, y: 77.00, atom : true , selected : false},
		{id: "10" ,  x : 91.97, y: 99.50, atom : true , selected : false},
		{id: "11" ,  x : 14.03, y: 99.50, atom : true , selected : false},
		{id: "n6" ,  x : 86.13, y: -32.12, atom : false, selected : false}
		
	
	],
	"bonds" : [
		{atoms:[0,1]},
		{atoms:[3,4]},
		{atoms:[2,17]},
		{atoms:[5,3]},
		{atoms:[6,7]},
		{atoms:[8,5]},
		{atoms:[9,8]},
		{atoms:[10,11]},
		{atoms:[1,9]},
		{atoms:[12,9]},
		{atoms:[13,8]},
		{atoms:[14,5]},
		{atoms:[14,15]},
		{atoms:[14,16]}
	]
};
</script>
<!-- 
 Looks nice; has tooltips, validation
 http://www.jformer.com/demos/validations/ 
-->


