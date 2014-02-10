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


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;:SWEARS;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; The classic "7 Dirty Words", or "the Carlin list":
;; - shit
;; - piss
;; - cunt
;; - fuck
;; - cocksucker
;; - motherfucker
;; - tits

;; We start with a modified Carlin list:
;;
;;         #"(shit|piss|cunt|fuck|cock|tits)"

;; "Ass" is a problem, because it is a mild swear that can be said on broadcast television after 10pm. It turns out that the
;; only "ass" tags that are not "class" or "assault" or whatever, as demonstrated with this:

(let [sets (map #(:tags %) (m/find-maps "evernotes" {:tags {$regex "ass" $options "i"}} ["tags"]))
      tags (flatten sets)]
  (distinct (filter #(re-matches #".*ass.*" %) tags)))

;; We find that the only real swears are "assholes" in a couple of variations. Therefore I include "assh" as something
;; to watch for, because it will pick up both "assholes" and "asshats", in case such term arises. It will not pick up
;; "assclowns" but it's not a perfect world. I've also added "dick" because I distinctly recall referring to tagging
;; stories about actions of certain salami commanders with the word "dicks".
;;
;; So the final swears list shall be:

(def dirty-words "(shit|piss|cunt|fuck|cock|tits|assh|dick)")

;; Note that this is a string rather than a regex, because this is getting fed to monger's specialized operators.

(map #(:tags %) (m/find-maps "evernotes" {:tags {$regex dirty-words $options "i"}} ["id" "tags"]))


;; NOTE: SOmething is wrong, because there is allegedly a single document with like 40 tags?
;; Which one is that?

(def en (m/find-maps "evernotes" {:tags {$regex dirty-words $options "i"}} ["id" "tags"]))

(filter #(> (count (:tags %)) 10) en)

(def mess (m/find-one-as-map "evernotes" {:id 24932}))


(count (:tags mess))


;; Okay, that's one messed up note. So now I'm going to see if there are a lot of things with many many tags.
;; Except, I don't know how. So I'll wait on that. I've added a note to our "special cases" list.
