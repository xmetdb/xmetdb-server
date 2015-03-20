{
    "apiVersion":  "${xmet_version_short}",
    "swaggerVersion": "1.2",
    "apis": [
        {
            "path": "/protocol",
            "description": "XMetDB observations"
        },
        {
            "path": "/chemical",
            "description": "XMetDB chemical structures"
        },
        {
            "path": "/catalog",
            "description": "Enzymes"
        }                                 
    ],
	<#include "/apidocs/authz.ftl" >
	<#include "/apidocs/info.ftl" >  
}