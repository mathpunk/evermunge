










;;;;;;;;;;;;;;;;;;;;;the basics;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; To get clj data structures, use these, passing in the collection name and a map of the query
;;   m/find-one-as-map collection query-map
;;   m/find-maps collection query-map

(def fucked (m/find-one-as-map "evernotes" {:tags "fucked"}))

(:tags fucked)

;;;;;;;;;;;;;;;;;;;;;;;;Loading a subset of fields;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Both monger.collection/find and monger.collection/find-maps take 3rd argument that specifies what fields need to be retrieved. Pass them
;; as a vector of keywords.

(def bullshit (m/find-one-as-map "evernotes" {:tags "bullshit"} ["id" "tags"]))

(:tags bullshit)

;;;;;;;;;;;;;;;;;;;;;;;:Messed-up Things;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Why does this guy have nearly 60 tags? Because it is a giant mash of many notes. I've added it to the TODOs on
;; livre.

(def mess (m/find-one-as-map "evernotes" {:id 24932}))
(count (:tags mess))





;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;QUERY EXPERIMENT 1: FINDING SWEAR-TAGS;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; The classic "7 Dirty Words", or "the Carlin list":
;; - shit
;; - piss
;; - cunt
;; - fuck
;; - cocksucker
;; - motherfucker
;; - tits
;; We start with a modified Carlin regular expression: (shit|piss|cunt|fuck|cock|tits)
;;
;; "Ass" is a problem, though. It is a mild swear, but it can be said on broadcast television after 10pm and so does not
;; fit into Carlin's "7 words you can never say on television" criterion. Upon examination, it turns out that most positive
;; "ass" tags are just substrings of clean words such as "class", "assault", etc.

(let [sets (map #(:tags %) (m/find-maps "evernotes" {:tags {$regex "ass" $options "i"}} ["tags"]))
      tags (flatten sets)]
  (distinct (filter #(re-matches #".*ass.*" %) tags)))

;; We find that the only real swears are "assholes" in a couple of variations. Therefore I include "assh" as something
;; to watch for, because it will pick up both "assholes" and "asshats", in case such term arises. It will not pick up
;; "assclowns" but it's not a perfect world. I've also added "dick" because, while it is a proper name, it is also a word
;; I found myself using when describing the behavior of salami commanders.
;;
;; NB: There is a string, and a regex, for use with monger's special operators or with Clojure.


(def dirty-words "(shit|piss|cunt|fuck|cock|tits|assh|dick)")
(def dirty-regex #".*(shit|piss|cunt|fuck|cock|tits|assh|dick).*")

(def tagged-with-swears (m/find-maps "evernotes" {:tags {$regex dirty-words $options "i"}} ["id" "tags"]))

(defn tags [results]
  (flatten (map #(:tags %) results )))

(tags tagged-with-swears)

(re-pattern dirty-words)




;;;;;;;;;;;;;;;;;;:QUERY 2: ALL THE TAGS;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def research-tags (m/find-maps "evernotes" {$or [{:notebook "morgue"},
                                            {:notebook "morgue archive"}]} ["tags"]))


(def research-vocab (distinct (tags research-tags)))

(frequencies (tags research-tags))


(spit "/home/thomas/tag-counts.txt" (reverse (sort-by val (frequencies (tags research-tags)))))
(spit "/home/thomas/swear-counts.txt" (reverse (sort-by val freq)))

freq

(def freq (frequencies (filter #(re-matches dirty-regex %) (tags tagged-with-swears))))



(take 50 (reverse (sort-by val (frequencies (tags research-tags)))))


(take 100 (reverse (sort-by val (frequencies (tags research-tags)))))







