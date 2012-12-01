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
	
<script type="text/javascript">
<!--  initialize the toTop link and show it when the contents are scrolled down enough -->
	$(document).ready( function () {
		$('#toTop').click( function () {
			$('html, body').animate({scrollTop: '0'}, 1000);
		});
		$(window).scroll( function () {
					var h = $('#header').height();
					var p = $(window).scrollTop();
					if ( p > (h + 100) ) {
						$('#toTop').stop().animate({left: '-5px'}, 'fast');
					} else if ( p < (h + 50) ) {
						$('#toTop').stop().animate({left: '-30px'}, 'slow');
					}
				});
		});
</script>
		
<div id='footer-out'>
<div id='footer-in'>
<div id='footer'>
	<a class='footerLink' href='http://www.ideaconsult.net/'>IDEAconsult Ltd.</a> 
	(2012) on behalf of <a class='footerLink' href='http://xmetdb.org'>XMETDB</a>
</div>
</div>
</div>
		
<script type="text/javascript">
	var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
	document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>

