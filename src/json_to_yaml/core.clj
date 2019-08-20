(ns json-to-yaml.core
	(:gen-class)
	(:require [monger.core])
	(:require [monger.collection])
	(:require [yaml.core])
	(:import org.bson.types.ObjectId)
 )

(def mongo-database-id "test_database")
(def mongo-collection-id "test_collection")
(def mongo-connection-host "localhost")
(def mongo-connection-port 27017)
(def yaml-output-file "output/test.yml")

(defn -main
	"Convert Mongo Database JSON to a YAML file"
	[document-id]
	(println (str "DEBUG: Executing program with params [" document-id "]"))
	(let [
		mongo-connection (monger.core/connect {:host mongo-connection-host :port mongo-connection-port})
		mongo-database (monger.core/get-db mongo-connection mongo-database-id)
	]
		(def mongo-document
			(monger.collection/find-maps
				mongo-database
				mongo-collection-id
				{
					:_id (ObjectId. document-id)
				}
			)
		)

		(println (str
			"DEBUG: "
			"Searching for document with id "
			document-id
			" in "
			mongo-database-id "@" mongo-connection-host ":" mongo-connection-port "/" mongo-collection-id
		))

		(println "DEBUG: Search result:")
		(println mongo-document)

		(println "DEBUG: YAML:")
		(println (yaml.core/generate-string mongo-document))

		(print (str "DEBUG: writing YAML to file \"" yaml-output-file "\""))
		(spit yaml-output-file (yaml.core/generate-string mongo-document))

		(monger.core/disconnect mongo-connection)
	)
)
