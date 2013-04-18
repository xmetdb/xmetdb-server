<div class='helptitle' style='font-weight:bold;'>XMetDB admin</div>
<div class='helpcontent'>
Database info<a href='#' class='chelp database'></a>
<br/>
How to create the database<a href='#' class='chelp create'></a>
</div>

<div id="keys" style="display:none;">
  <ul>
    <li><a href="#database">Database</a></li>
    <li><a href="#create">Create database</a></li>
    <li><a href="#mysqladminuser">MySQL user</a></li>
  </ul>
  <div id="database">
    <p>
    Displays information about the current database
    </p>
  </div> 
  <div id="create">
    <p>
    Use the following <a href="http://curl.haxx.se/">cURL</a> command 
	<pre>
		curl -X POST \ 
		-d "dbname=xmetdb" \ 
		-d "user=mysqladminuser" \ 
		-d "pass=mysqladminpass" \
        ${xmet_root}/admin/database 
	</pre>
	 Database tables will be created, only if the database exists,
    is empty and the database name is the same as configured server side.
    </p>
  </div>
  <div id="mysqladminuser">
    <p>
    Existing MySQL user with sufficient privileges to create database tables.
    </p>
   </div>   
</div>
