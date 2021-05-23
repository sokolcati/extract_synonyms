This repository contains source code with realization
of keyword extraction methods and synonym extraction methods.

----- Source files: -----

1. Main.java

2. FilesFindingInDirectory.java
	Class for creating list of all TEX-files from specified directory.
	* add files to list
	* iterates throw list
	* break text into sections

3. Clean.java
	Class for text pre-processing.
	* clean text from stop-words and punctuation marks.
	* clean output of MyStem
	* convert file to string
	* normalize text and etc.

4. TF-IDF.java
	Class for counting TF-IDF.
	* count TF
	* count IDF
	* all secondary functions

5. Weight.java
	Class for storage of words with TF-IDF their measure.
	* creation
	* printing
	* marking like keyword

6. CohensKappa.java
	Class with counting Cohen's Kappa
	for two sets of extracted keywords.

7. make_syn_dict.py
	script to making dictionary from terms of libmeta
	note: libmeta.ru does not like a lot of appeals so
			it may lead to crash

----- Before starting: -----

1. Install MyStem
   Download last version for any os here: https://yandex.ru/dev/mystem/

2. File: Main.java
   Strings: 9-12
   To do:
	* change DOC_PATH like way to directory with files
	* change TERM_MARKUP like way to out.txt
	* change MYSTEM_FILE like way to file with text for MyStem
	* change MYSTEM_PATH like way to MyStem

----- How to use: -----

1. Simple run:
   (this values are currently set)
   IS_NEW_MYSTEM = false
   JUST_ONE_TERM = ""
   SAVE_KEYWORDS = ""

2. IS_NEW_MYSTEM
	"IS_NEW_MYSTEM = true" requires a lot of resources
	optional if it's not first start
	optional if input.txt was downloaded

3. JUST_ONE_TERM
	"JUST_ONE_TERM = "поле" " does not check all words
	pritns results only for "поле"
	might be any word instead of "поле"

4. SAVE_KEYWORDS
	SAVE_KEYWORDS = "path/to/file" saves keywords in file
	otherwise does not save
