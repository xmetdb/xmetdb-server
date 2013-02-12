XMetDB web application
=============

Deployment
----

Deploy xmetdb.war to a servlet container (e.g. Apache [TomCat](http://tomcat.apache.org/) or run locally by 

    mvn tomcat:run -P xmet

Context help
----

The help files are in [WEB-INF/templates/help](https://github.com/xmetdb/xmetdb-server/tree/master/xmet-war/src/main/webapp/WEB-INF/templates/help).
The web pages themselves are in the [WEB-INF/templates](https://github.com/xmetdb/xmetdb-server/tree/master/xmet-war/src/main/webapp/WEB-INF/templates) folder. 
Both are [FreeMarker](http://freemarker.sourceforge.net/) templates, which means HTML + server preprocessed fields. 

Every page loads its help page with Javascript. For example the [Enzymes](https://github.com/xmetdb/xmetdb-server/blob/master/xmet-war/src/main/webapp/WEB-INF/templates/s_endpoints_body.ftl) page 
loads the [help file enzyme.ftl](https://github.com/xmetdb/xmetdb-server/blob/master/xmet-war/src/main/webapp/WEB-INF/templates/help/enzyme.ftl) 
at line 40:

    loadHelp("${xmet_root}","enzyme");  

### Help formatting
The help pages contain a title, content and context specific entries.

#### Help title
A div with class 'helptitle'. 

    <div class='helptitle' style='font-weight:bold;'>Enzymes how to</div>
    
#### help content    
A div with class 'helpcontent'. Any valid html could be placed inside the div. 

    <div class='helpcontent'>
    <span class='help'><i>It is not recommended to edit enzymes if there are already assigned observations!</i></span>
    </div>

#### Context help entries
The context help entries are placed with a div with id 'keys'. The links to the entries are in unnumbered list ( ul )
where the href indicates the id of the div, containing the actual context specific help entry. 
The formatting is borrowed from [jQuery Tabs](http://jqueryui.com/tabs/)

    <div id="keys" style="display:none;">
    <ul>
    <li><a href="#add_enzyme">Add enzyme</a></li>
    <li><a href="#delete_enzyme">Delete enzyme</a></li>
    <li><a href="#edit_code">Edit enzyme code</a></li>
    </ul>
    
      <div id="add_enzyme">
      <p>Use the "Add" button at the bottom of the table.</p>
      </div>
      <div id="delete_enzyme">
      <p>Use the "Remove" button at the bottom of the table.</p>
      </div>  
      <div id="edit_code">
      <p>Double click on the table cell (column Code) to edit. When ready, click "Save changes button". 
      To cancel, click elsewhere.</p>
      </div>
    <div>
    
The divs with id (e.g. add_enzyme, delete_enzyme) above may contain any valid html. The div content will be shown on the right, 
when the user clicks on a context help link anywhere within the page. The links are "a href" tags, with class 'chelp' (mandatory)
and another class which is the same as the div id to be displayd. For example the following "a" tag will result in a clickable (i) icon
which upon clicking will load the content of the div with id "add_enzyme". 

     <a href='#' class='chelp add_enzyme'></a> 
     
The links could be placed anywhere on the page or in the help page itself.     

