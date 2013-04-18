<div class='helptitle' style='font-weight:bold;'>XMetDB jobs</div>
<div class='helpcontent'>
This is the Task service<a href='#' class='chelp task_service'>?</a>, managing the long running jobs in XMetDB.
<br/>
Browse: 
<a href="${xmet_root}/task?max=100">All tasks</a> |
<a href="${xmet_root}/task?search=Running">Running</a> |
<a href="${xmet_root}/task?search=Completed">Completed</a> |
<a href="${xmet_root}/task?search=Queued">Queued</a> |
<a href="${xmet_root}/task?search=Error">Error</a>
</div>

<div id="keys" style="display:none;">
  <ul>
    <li><a href="#task_service"></a></li>
  </ul>
  <div id="task_service">
    Asynchronous jobs are handled via an intermediate Task resource. Defined in OpenTox
    <a href="http://ambit.sourceforge.net/api_task.html" target=_blank title='Application Programming interface'>API</a>.
    The completed tasks expire in 2h.
  </div>
</div>      