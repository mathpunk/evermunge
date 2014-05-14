(ns munger.mongo
  (:require [monger.collection :as m])
  (:use [monger.operators])
  (:use [munger.connect]))

;; Examples of querying Mongo
;; ===========================================================================================
;; To get clj data structures, use these, passing in the collection name and a map of the query
;;   m/find-one-as-map collection query-map
;;   m/find-maps collection query-map

;;
;; Both monger.collection/find and monger.collection/find-maps take 3rd argument that specifies what fields need to be retrieved. Pass them
;; as a vector of keywords.

(def bullshit (m/find-one-as-map "evernotes" {:tags "bullshit"} [:id :tags] ))
(:tags bullshit)


;; But must they?
(def bullshit (m/find-one-as-map "evernotes" {:tags "bullshit"} ))

;; Description of Evernote data
;; ===========================================================================================
;;


;; Accessing a collection to get a map
;; ===========================================================================================


;; ============================================================================================
;; Loading a subset of fields
;; ===========================================================================================



;; ============================================================================================
;; Helpers
;; ============================================================================================

(defn tags [results]
  (flatten (map #(:tags %) results )))

;; ============================================================================================
;; Research tags, their frequencies, selections
;; ============================================================================================

;; This gets all the maps from the morgue or morgue archive notebook.
(def research-tags (m/find-maps "evernotes" {$or [{:notebook "morgue"},
                                            {:notebook "morgue archive"}]} ["tags"]))


;; Frequency information on research tags
(frequencies (tags research-tags))
;; Unique research tags
(def research-vocab (distinct (tags research-tags)))
(research-vocab)

;; ## Top 50, top 100
(take 50 (reverse (sort-by val (frequencies (tags research-tags)))))





(spit "/home/thomas/work/datasci/hello-bars/data.clj" (reverse (sort-by val (frequencies (tags research-tags)))))








