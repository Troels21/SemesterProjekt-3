# -*- coding: utf-8 -*-
"""
Created on Mon Jan 10 08:57:19 2022

@author: Spille
"""

# importing the requests library
import requests
import json
  
def postFiltEKG(cpr, filteredarray):
    
     #OBBBBSSSSS     Tomcat Serveren skal PT være tændt.

    endpoint = "https://ekg3.diplomportal.dk/data/ekgSessions/measurements"


    dsparray = filteredarray #Dsp plot array
    print(dsparray)
    Headers={'Authorization': 'Bearer hemmeliglogin', #Loginkode
             'Identifier': cpr} 
    
      
    # Post request, med endpoint, dataen og headers til genkendelse og sikkerhed.
    r = requests.post(endpoint,  data = json.dumps(dsparray), headers = Headers )
      
    #Modtage responbesked 
    Response = r.text
    