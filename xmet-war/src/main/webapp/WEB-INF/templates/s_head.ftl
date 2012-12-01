	<!-- Basic Page Needs
  ================================================== -->
	<title>Xenobiotics Metabolism Database</title>
	<meta name="robots" content="index,follow"><META NAME="GOOGLEBOT" CONTENT="index,FOLLOW">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="description" content="Xenobiotics Metabolism Database">
	<meta name="author" content="${creator}">

	<!-- Mobile Specific Metas
  ================================================== -->
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

	<!-- CSS
  ================================================== -->
	<link rel="stylesheet" href="/xmetdb/skeleton/stylesheets/base.css" type="text/css">
	<link rel="stylesheet" href="/xmetdb/skeleton/stylesheets/skeleton-fluid.css" type="text/css">
	<link rel="stylesheet" href="/xmetdb/skeleton/stylesheets/layout.css" type="text/css">

	<!--[if lt IE 9]>
		<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
	
	
	<!-- XMET specific css
	================================================== -->
	<link href="/xmetdb/skeleton/stylesheets/xmet.css"     rel="stylesheet"  type="text/css">
	<link href="/xmetdb/style/jquery-ui-1.8.18.custom.css" rel="stylesheet" type="text/css">
	<link href="/xmetdb/style/jquery.dataTables.css"       rel="stylesheet" type="text/css">	
		
	
	<!-- Favicons
	================================================== -->
	<link rel="shortcut icon" href="/xmetdb/images/favicon.ico">
	<link rel="apple-touch-icon" href="/xmetdb/style/images/apple-touch-icon.png">
	<link rel="apple-touch-icon" sizes="72x72" href="/xmetdb/style/images/apple-touch-icon-72x72.png">
	<link rel="apple-touch-icon" sizes="114x114" href="/xmetdb/style/images/apple-touch-icon-114x114.png">


		<!-- JQuery
	================================================== -->
	<script type='text/javascript' src='/xmetdb/jquery/jquery-1.7.2.min.js'></script>
	<script type='text/javascript' src='/xmetdb/jquery/jquery-ui-1.8.18.custom.min.js'></script>
	<script type='text/javascript' charset='utf8' src='/xmetdb/jquery/jquery.dataTables-1.9.0.min.js'></script>
	<script type='text/javascript' src='/xmetdb/jquery/jquery.cookies.2.2.0.min.js'></script>
	
	<!-- XMET JS
	================================================== -->
	<script type='text/javascript' src='/xmetdb/scripts/xmetdb.js'></script>
	
	<!-- Footer
	================================================== -->
<script type="text/javascript">
<!-- show the footer when the mouse is near -->
	$(document).ready( function () {
    	$('div#footer-in').mouseenter( function () {
    		$('div#footer').stop().animate({bottom: '15px'}, 'fast');
    	});
    	$('div#footer-out').mouseleave( function () {
    		$('div#footer').stop().animate({bottom: '-17px'}, 'slow');
    	});
    });
</script>