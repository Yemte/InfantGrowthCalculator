The infant growth calculator android mobile application is the thesis project for completing my bachelor degree in IT.
The application allows the user to calculate baby’s weight gain rate, height increase rate and head circumference increase rate for boys and girls from birth up to one year.


It compares the result with world health organization (WHO) growth rate percentile standard charts. 
It's built based on world health organization child growth per-centile standards of tables and charts for both boys and girls from birth up to 12 months. The tables were used to generate the application’s calculation formula shown below.


AG = (BW - pn)							             
AGPW = pm-pn							              
GPW =((AG))/AGPW   

Percentw = (GPW *(((Pmth%-Pnth%))/100))+((Pnth%)/100)		 
 
BW = birth weight in kilogram user input.

Pn = the nearest smaller percentile world health organization standard value weight in kilogram relative to the given BW in the database.

AG = the weight difference between the nearest smaller percentile world health organization standard value Pn & the birth weight BW.

Pm = the nearest larger world health organization standard value weight in kilogram relative to the given BW in the database.

AGPW == the weight difference between the nearest smaller percentile WHO standard value Pn & the nearest larger world health 
organization standard value Pm.

GPW = the ratio of AG per AGPW.

Pnth% = the nearest smaller percentile of world health organization growth rate standard of a given age 

Pmth% = the nearest larger percentile of world health organization growth rate standard of a given age 

Percentw = the calculated weight per age percentile.



The calculator receives birth weight  current weight ,age in weeks/months and gender to calculate weight gain .
It receives birth height, current height,age and gender to calculate hight increase
It receives birth head circumference, current head circumference, gender and age to calculate head circumference increase.
calculates the percentile growth rate. 
It displays the result in the form of text and generates a graph  for the result comparing it with  world health organization standard charts.
