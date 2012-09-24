<script type="text/javascript">

var observation;

$(document).ready(function() {
				      $.ajax({
				          dataType: "json",
				          url: "${xmet_request}",
				        	  //"/xmetdb/protocol/XMETDB2?media=application%2Fjson",
				          success: function(data, status, xhr) {
				        	  observation = data.observations[0];
				        	  $('span#xmet_id').replaceWith("<a href='"+ observation["uri"] + "'>" + observation["identifier"] + "</a>");
				        	  $('span#xmet_experiment').replaceWith(observation["description"] + " (" + observation["title"] + ")");
				        	  //$('span#xmet_substrate').replaceWith(observation.Substrate.dataset.uri);
				        	  //$('span#xmet_product').replaceWith(observation.Product.dataset.uri);
				        	  $('span#xmet_reference').replaceWith("TODO");
				        	  
				        	  loadEnzyme(observation);
				        	  if ((observation.Substrate.dataset.structure === undefined) || (observation.Substrate.dataset.structure==null)) 
				        		  loadStructures(observation.Substrate.dataset.uri,"span#xmet_substrate");
				        	  
				        	  if ((observation.Product.dataset.structure === undefined) || (observation.Product.dataset.structure==null)) 
				        		  loadStructures(observation.Product.dataset.uri,"span#xmet_product");
				          },
				          error: function(xhr, status, err) { },
				          complete: function(xhr, status) { }
				       });
				      
				      
} );

function loadEnzyme(observation) {
	 if ((observation.enzyme.code === undefined) || (observation.enzyme.code ==null)) {
	      $.ajax({
	          dataType: "json",
	          url: observation.uri + "/endpoint?max=1&media=application%2Fjson",
	          success: function(data, status, xhr) {
	        	  observation.enzyme.code = data[0].code;
	        	  observation.enzyme.name = data[0].name;
	        	  $('span#xmet_enzyme').replaceWith(observation.enzyme.code + "&nbsp;" +  observation.enzyme.name );
	          },
	          error: function(xhr, status, err) { },
	          complete: function(xhr, status) { }
	       });
	 } else {
		 $('span#xmet_enzyme').replaceWith(observation.enzyme.code + "&nbsp;" +  observation.enzyme.name );
	 }	
}

/**
 * Load structures from remote Ambit dataset uri via JSONP
 */
function loadStructures(datasetURI, tag) {
		 
	      $.ajax({
	          dataType: "jsonp",
	          url: datasetURI + "?media=application%2Fx-javascript",
	          success: function(data, status, xhr) {
	        	  var images = "";
	        	  data.dataEntry.forEach(function img(element, index, array) {
	        		  images += cmp2image(element.compound.URI);
	        		  images += "&nbsp;";
	        	  });
	        	  $(tag).replaceWith(images);	
	          },
	          error: function(xhr, status, err) { 
	        	  console.log(err);
	          },
	          complete: function(xhr, status) { }
	       });
}
</script>				       