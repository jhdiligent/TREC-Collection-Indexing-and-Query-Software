# TREC-Collection-Indexing-and-Query-Software
Designed a software to index and query a TREC collection, using Single-Pass In-Memory Indexing technique to build indexes and Vector Space Model to generate Cosine similarity ranking for collection with capacity for over 80000 documents.
1.  Download both directory.
2.	In CMD, type javac indexer\*.java
3.	In CMD, type jar cvf indexer.jar indexer\*.class, you can see a indexer.jar file under current directory
4.	In CMD, type javac query\*.java -classpath indexer.jar
5.	Run the program by typing java query.Querymain [query_desc.txt] [stoplist.txt] [trec_collection] in CMD
Note: [filename] is the path of each file or directory
