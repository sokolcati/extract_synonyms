# -*- coding: utf-8 -*-
import requests
from bs4 import BeautifulSoup

# Output file
out_file = open('syn_dict.txt', 'w')

# Processing of dictionaries
libmeta = []  # a list of string representing the diffthes.txt
codes = []
with open('libmeta.txt', 'UTF-8') as libmeta:
    line_type = 0
    for line in libmeta:
        if line_type == 1: # this string contain link with 'show'
            line_type = 0
            link_num = line[41:-12]
            codes.append(link_num)
        elif (line.find("<lbm:Concept rdf:about") == 0) and (line[-2] == '>'):
            line_type = 1 # this string contain term
useless = 1
with open('specfunc.txt', 'UTF-8') as libmeta:
    line_type = 0
    for line in libmeta:
        if line.find('<lbm:ConceptGroup rdf:about="http://libmeta.ru/thesaurus/group/specmain"> </lbm:ConceptGroup>') > -1:
            useless = 0
        if line_type == 1: # this string contain link with 'show'
            line_type = 0
            link_num = line[41:-12]
            codes.append(link_num)
        elif (line.find("<lbm:Concept rdf:about") == 0) and (line[-2] == '>') and (useless == 0):
            line_type = 1 # this string contain term
useless = 1
with open('diffthes.txt', 'UTF-8') as libmeta:
    line_type = 0
    for line in libmeta:
        if line.find('<lbm:ConceptGroup rdf:about="http://libmeta.ru/thesaurus/group/add"> </lbm:ConceptGroup>') > -1:
            useless = 0
        if line_type == 1: # this string contain link with 'show'
            line_type = 0
            link_num = line[41:-12]
            codes.append(link_num)
        elif (line.find("<lbm:Concept rdf:about") == 0) and (line[-2] == '>') and (useless == 0):
            line_type = 1 # this string contain term
            
# find synonyms and related terms
for code in codes:
    url = 'http://libmeta.ru/concept/' + code
    r = requests.get(url)
    ryy = r.text
    rel = ryy.find("<lbm:familyRelation type=\"http://libmeta.ru/relation/family#related\">")
    while (rel > 0):
        rel += min(ryy[rel + 101 : ].find('#'), ryy[rel + 101 : ].find('concept/')) + 102
        if ryy[rel - 1] == 'c':
            rel += 7
        if ryy[rel - 1] == '\"':
            rel += ryy[rel : ].find('#') + 1
        end = ryy[rel :].find("/>")
        out_file.write(url)
        out_file.write(" - ")
        out_file.write(ryy[rel : rel + end - 1].encode('utf-8'))
        out_file.write('\n')
        ryy = ryy[rel + end : ]
        rel = ryy.find("<lbm:familyRelation type=\"http://libmeta.ru/relation/family#related\">")
    ryy = r.text
    rel = ryy.find("<lbm:familyRelation type=\"http://libmeta.ru/relation/family#synonomy") + 1
    while (rel > 0):
        if ryy[rel + 100 : ].find('#') > -1:
            if ryy[rel + 100 : ].find('concept/') > -1:
                rel += min(ryy[rel + 100 : ].find('#'), ryy[rel + 100 : ].find('concept/'))
            else:
                rel += ryy[rel + 100 : ].find('#')
        else:
            rel += ryy[rel + 100 : ].find('concept/')
        if ryy[rel + 100] == 'c':
            rel += 7
        end = ryy[rel + 100 :].find("/>")
        out_file.write(url)
        out_file.write(" - ")
        out_file.write(ryy[rel + 101 : rel + 100 + end - 1].encode('utf-8'))
        out_file.write('\n')
        ryy = ryy[rel + end + 100 :]
        rel = ryy.find("<lbm:familyRelation type=\"http://libmeta.ru/relation/family#synonomy")

# Close out_file
out_file.close()
