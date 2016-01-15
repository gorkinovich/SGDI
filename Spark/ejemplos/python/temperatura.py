#!/usr/bin/python
# -*- coding: UTF-8 -*-

"""
//******************************************************************************************
// SGDI, Práctica 1, Apartado 2: TEMPERATURA
// Dan Cristian Rotaru y Gorka Suárez García
//******************************************************************************************

Ahora vamos a utilizar el fichero JCMB_last31days.csv que registra diversos parámetros
recogidos por una estación meteorológica en la Universidad de Edimburgo, obtenido de
http://www.ed.ac.uk/schools-departments/geosciences/weather-station/download-weather-
data, con 45.000 líneas. Estos datos han sido recogido minuto a minuto durante 31 días.
El formato del fichero es de valores separados por comas (CSV), con los siguientes 17
campos en cada línea:

 0: index,
 1: year,
 2: day of the year,
 3: time,
 4: atmospheric pressure (mBar),
 5: rainfall (mm),
 6: wind speed (m/s),
 7: wind direction (degrees),
 8: surface temperature (C),
 9: relative humidity (%),
10: wind_max (m/s),
11: Tdew (C),
12: wind_chill (C),
13: uncalibrated solar flux (Kw/m2),
14: calibrated solar flux (Kw/m2),
15: battery (V),
16: not used

Queremos procesar el fichero para conocer, para cada día, la diferencia máxima y
mínima entre la temperatura en superficie (surface temperature) y la sensación térmica
(wind chill).

Ejemplo:
101,2013,277,1158,1002,7.6,.167,89.2,15.35,99.6,.75,15.3,15.35,.067,.101,13.59,0
"""

from mrjob.job import MRJob

class MRTemperature(MRJob):

    # MAP -> key:_, line:string
    def mapper(self, key, line):
        fields = line.split(",")
        surfaceTemperature = float(fields[8])
        windChill = float(fields[12])
        date = fields[1] + " " + fields[2]
        diff = surfaceTemperature - windChill
        yield date, diff

    # REDUCE -> key:string, line:[float]
    def reducer(self, key, values):
        data = [item for item in values]
        yield key, (max(data), min(data))

if __name__ == '__main__':
    MRTemperature.run()
