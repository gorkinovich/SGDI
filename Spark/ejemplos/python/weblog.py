#!/usr/bin/python
# -*- coding: UTF-8 -*-

"""
//******************************************************************************************
// SGDI, Práctica 1, Apartado 1: WEBLOG
// Dan Cristian Rotaru y Gorka Suárez García
//******************************************************************************************

Consideremos el log del servidor Web (weblog.txt, 47.778 líneas) que se puede encontrar en
el Campus Virtual, obtenido de http://ita.ee.lbl.gov/html/contrib/EPA-HTTP.html. Calcular
el número peticiones que han obtenido un código 302 cada día (no es necesario mostrar los
días en los que no se ha producido ninguna petición con código de respuesta 302). Las
líneas del log representan accesos al servidor, y están compuestas por los siguientes
campos separados por espacios:

+ host que realiza la petición (nombre de host o dirección IP)
+ fecha en el formato [DD:HH:MM:SS], donde DD es el día y HH:MM:SS es la hora.
+ Petición entre comillas. ¡Atención! La petición suele estar formada por 3 partes
  separadas por espacios: verbo HTTP (GET, POST, HEAD...), el recurso accedido y,
  opcionalmente, la versión de HTTP usada. El recurso accedido puede contener
  espacios y comillas.
+ Código HTTP de respuesta: 200, 404, 302...
+ Número de bytes de la contestación.

En la URL anterior se puede encontrar información más detallada.

Ejemplo:
141.243.1.172 [29:23:53:25] "GET /Software.html HTTP/1.0" 200 1497
"""

from mrjob.job import MRJob

class MRWeblog(MRJob):

    # MAP -> key:_, line:string
    def mapper(self, key, line):
        fields = line.split()
        if fields[-2] == "302":
            day = int(fields[1][1:3])
            yield day, 1

    # REDUCE -> key:int, line:[int]
    def reducer(self, key, values):
        yield key, sum(values)

if __name__ == '__main__':
    MRWeblog.run()
