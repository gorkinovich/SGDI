#!/usr/bin/python
# -*- coding: UTF-8 -*-

"""
//******************************************************************************************
// SGDI, Práctica 1, Apartado 4: INDEX
// Dan Cristian Rotaru y Gorka Suárez García
//******************************************************************************************

Utilizando los archivos de texto plano “Adventures_of_Huckleberry_Finn.txt”, “Hamlet.txt”
y “Moby_Dick.txt”, generar un índice inverso de palabras y archivos en los que aparecen.
No se debe hacer distinción entre mayúsculas y minúsculas, y las palabras no deben
contener signos de puntuación. El índice no debe contener todas las palabras que aparecen
en los libros, únicamente aquellas que sean populares, es decir, que aparecen más de 20
veces en alguno de los libros. A cada palabra popular del índice le debe acompañar una
lista de parejas (libro, número de apariciones) ordenado por número de apariciones, y este
listado contendrá todos los libros donde aparece la palabra, incluidos aquellos donde
aparezca 20 veces o menos.

El resultado será un listado como el siguiente:
...
"wind"       "(Moby Dick.txt, 66), (Adventures of Huckleberry Finn.txt, 13)"
"windlass"   "(Moby Dick.txt, 21)"
...

Pista: será necesario obtener el nombre del fichero del que se ha obtenido la tupla, y
este valor no aparece en las claves ni los valores que se leen de los ficheros. Habrá que
acceder a ellos utilizando el entorno (Mrjob) o el argumento context (Hadoop).
"""

import os
from operator import itemgetter
from mrjob.job import MRJob

# Para obtener las palabras de una línea:
def splitter(victim):
    for chunk in victim.split():
        word = ""
        for item in chunk:
            if item.isalpha():
                word += item.lower()
            elif len(word) > 0:
                yield word
                word = ""

class MRIndex(MRJob):

    # MAP -> key:_, line:string
    def mapper(self, key, line):
        fileName = os.environ['map_input_file']
        for word in splitter(line):
            yield word, fileName

    # REDUCE -> key:string, line:[string]
    def reducer(self, key, values):
        files = [item for item in values]
        if len(files) > 0:
            # Contar las apariciones por fichero:
            counter = {}
            for item in files:
                if item in counter:
                    counter[item] += 1
                else:
                    counter[item] = 1
            # Cambiar el formato y comprobar que son populares:
            words = []; makeYield = False
            for k, v in counter.iteritems():
                words.append((v, "(" + k + ", " + str(v) + ")"))
                if v > 20:
                    makeYield = True
            # Fusionar la lista de elementos y lanzarlo:
            if makeYield and len(words) > 0:
                words = sorted(words, key=itemgetter(0), reverse=True)
                msg = itemgetter(1)(words[0])
                for item in words[1:]:
                    msg += (", " + itemgetter(1)(item))
                yield key, msg

if __name__ == '__main__':
    MRIndex.run()
