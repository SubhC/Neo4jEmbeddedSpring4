Import
-----------
LOAD CSV WITH HEADERS FROM "file:/Users/Subhasish/Documents/eShark/Work/RnD/GraphDB/Neo4jEmbeddedSpring4/movie-db/SwithcInfo.csv" AS csvLine
CREATE (C:Switch { NSN: csvLine.NSN, CN: csvLine.common_name, Price: csvLine.Price, UI: csvLine.UI, AAC: csvLine.AAC, Batch: csvLine.Batch})

Relation
-------------
LOAD CSV WITH HEADERS FROM "file:/Users/Subhasish/Documents/eShark/Work/RnD/GraphDB/Neo4jEmbeddedSpring4/movie-db/SwithcInfo.csv" AS csvLine
Match (s1:Switch { NSN: csvLine.NSN}), (s2:Switch { NSN: csvLine.ParentID})
MErge (s2)-[:CONNECTED_TO]->(s1)