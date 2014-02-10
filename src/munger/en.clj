(ns munger.en
  (:require [monger.collection :as m] [monger.core :as mc])
  (:use [monger.operators])
  (:import [org.bson.types ObjectId] [com.mongodb DB WriteConcern MongoOptions ServerAddress]))

;; Connecting to the machine and database
(defn connection [host db]
  (let [^MongoOptions opts (mc/mongo-options :threads-allowed-to-block-for-connection-multiplier 300)
        port 27017
        ^ServerAddress sa  (mc/server-address host port)]
    (do
      (mc/connect! sa opts)
      (mc/set-db! (mc/get-db db))
      )
    )
  )

(defn connect [ ]
  (connection "feuille" "livre"))

;; Presumably this is an arg to a function or whatever?
(connect)


;;;;;;;;;;;;;;;;;;;;;the basics;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(m/find-one "evernotes" {:tags []})

;; To get clj data structures, use these, passing in the collection name and a map of the query
;;   m/find-one-as-map COLL QMAP
;;   m/find-maps COLL QMAP

(def fucked (m/find-one-as-map "evernotes" {:tags "fucked"}))

fucked

(:tags fucked)



;;;;;;;;;;;;;;;;;;;;;;;;Loading a subset of fields;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Both monger.collection/find and monger.collection/find-maps take 3rd argument that specifies what fields need to be retrieved:

;; (m/find-one-as-map "accounts" {:username "happyjoe"} ["email" "username"])

;; This is useful to excluding very large fields from loading when you won't operate on them.

;; Fields can be specified as a document (just like in the MongoDB shell) but it is more common to pass them as a vector of keywords.
;; Monger will transform them into a document for you.

(def bullshit (m/find-one-as-map "evernotes" {:tags "bullshit"} ["id" "tags"]))

bullshit


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;Operators, limits, etc;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def swears (m/find-maps "evernotes" {:tags {$regex ".*(shit|fuck).*" $options "i"}} ["id" "tags"]))




;; omg
