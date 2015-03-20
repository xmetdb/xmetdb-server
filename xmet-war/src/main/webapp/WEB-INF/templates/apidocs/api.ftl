{
    "apiVersion":  "${xmet_version_short}",
    "swaggerVersion": "1.2",
    "apis": [
        {
            "path": "/protocol",
            "description": "XMetDB observations. An <a href='http://www.xmetdb.org/wiki/Observation'>Observation</a> is a record of an experiment, transforming a Substrate to a Product. "
        },
        {
            "path": "/chemical",
            "description": "XMetDB chemical structures"
        },
        {
            "path": "/catalog",
            "description": "XMetDB is populated with a list of the <a href='http://www.xmetdb.org/wiki/API#Enzymes'>common enzymes>/a>. "
        },
        {
            "path": "/stats",
            "description": "Statistics"
        }                                             
    ],
	<#include "/apidocs/authz.ftl" >
	<#include "/apidocs/info.ftl" >  
}