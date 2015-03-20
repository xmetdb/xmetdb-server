{
    "apiVersion":  "${xmet_version_short}",
    "swaggerVersion": "1.2",
    "apis": [
        {
            "path": "/protocol",
            "description": "Observations. A XMetDB Observation is a record of an experiment, transforming a Substrate to a Product."
        },
        {
            "path": "/chemical",
            "description": "XMetDB chemical structures"
        },
        {
            "path": "/catalog",
            "description": "Enzymes. XMetDB is populated with a list of the common enzymes."
        },
        {
            "path": "/stats",
            "description": "XMetDB Statistics"
        }                                             
    ],
	<#include "/apidocs/authz.ftl" >
	<#include "/apidocs/info.ftl" >  
}