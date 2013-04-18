<div class='helptitle' style='font-weight:bold;'>XMetDB admin</div>
<div class='helpcontent'>
Users<a href='#' class='chelp user'></a> |
Organisations <a href='#' class='chelp affiliation'></a>
Async jobs <a href='#' class='chelp task'></a>
Saved searches and alerts<a href='#' class='chelp nootification'></a>
Database <a href='#' class='chelp database'></a>
</div>

<div id="keys" style="display:none;">
  <ul>
    <li><a href="#database">Database</a></li>
    <li><a href="#user">Users</a></li>
    <li><a href="#affiliation">Affiliations</a></li>
    <li><a href="#task">Tasks</a></li>
    <li><a href="#notification">Notification</a></li>
  </ul>
  <div id="database">
    <p>
    curl -X GET ${xmet_root}/admin/database -d "dbname=newdb" -d "user=uname" -d "pass=pass"
    </p>
  </div>
  <div id="user">
    <p>List of registered users.</p>
  </div>
  <div id="affiliation">
	    <p>List of user affiliations, entered on registration.</p>
  </div>
  <div id="notification">
    <p>Saved searches and alerts</p>
  </div>
  <div id="tasks">
    <p>List of long running jobs in XMetDB.</p>
  </div>

</div>
