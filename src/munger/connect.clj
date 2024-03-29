(ns munger.connect
  (:require [monger.collection :as m] [monger.core :as mc])
  (:use [monger.operators])
  (:import [org.bson.types ObjectId] [com.mongodb DB WriteConcern MongoOptions ServerAddress]))

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

