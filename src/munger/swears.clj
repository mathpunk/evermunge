(ns munger.swears
  )
;; ============================================================================================
;; Query: Find all notes tagged with swears
;; ============================================================================================

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

;; Alright, go get those swears
(def tagged-with-swears (m/find-maps "evernotes" {:tags {$regex dirty-words $options "i"}} ["id" "tags"]))

;; (re-pattern dirty-words)

(def freq (frequencies (filter #(re-matches dirty-regex %) (tags tagged-with-swears))))

