#!/usr/bin/python
# -*- coding: UTF-8 -*-

"""
//******************************************************************************************
// SGDI, Práctica 1, Apartado 3: HAPPINESS
// Dan Cristian Rotaru y Gorka Suárez García
//******************************************************************************************

Ahora utilizaremos el fichero happiness.txt, que contiene una valoración de “felicidad”
para más de 10.000 palabras inglesas. Este fichero ha sido obtenido del artículo Temporal
patterns of happiness and information in a global social network: Hedonometrics and
Twitter, http://arxiv.org/abs/1101.5120. El archivo está separado por tabuladores,
donde cada línea sigue el siguiente formato:

0: word
1: happiness_rank
2: happiness_average
3: happiness_standard_deviation
4: twitter_rank
5: google_rank
6: nyt_rank
7: lyrics_rank

Calcular aquellas palabras extremadamente tristes, es decir, aquellas cuya felicidad
media (happiness_average) está por debajo de 2 y que además tienen ranking de Twitter
(twitter_rank es diferente de --). La tarea MapReduce debe devolver una sola línea
(es decir, la fase REDUCE debe emitir una sola tupla) con el número de palabras
extremadamente tristes seguida de la lista de dichas palabras.

Ejemplo de salida
“Palabras muy tristes”	“pena, tristeza, enfermedad”
"""

from mrjob.job import MRJob

class MRHappiness(MRJob):

    # MAP -> key:_, line:string
    def mapper(self, key, line):
        fields = line.split("\t")
        hapAve = float(fields[2])
        twiRan = fields[4]
        if hapAve < 2.0 and twiRan != "--":
            yield "Palabras muy tristes", fields[0]

    # REDUCE -> key:string, line:[string]
    def reducer(self, key, values):
        words = [item for item in values]
        if len(words) > 0:
            msg = words[0]
            for item in words[1:]:
                msg += (", " + item)
            yield len(words), msg

if __name__ == '__main__':
    MRHappiness.run()
