# importing the requests library
import requests
import json
  
# OBBBBSSSSS     Tomcat Serveren skal PT være tændt.

endpoint = "http://localhost:8080/SemesterProjekt_3_war/data/ekg"


cpr= "1234567890" #CPR
dsparray = [1,2,3,4,5,6,7,8,9,23,32,41,252,3,562,34,421,3,0] #Dsp plot array
Headers={'Authorization': 'Bearer hemmeliglogin', #Loginkode
         'Identifier': cpr} 

  
# Post request, med endpoint, dataen og headers til genkendelse og sikkerhed.
r = requests.post(endpoint,  data = json.dumps(dsparray), headers = Headers )
  
#Modtage responbesked 
Response = r.text
print(Response)