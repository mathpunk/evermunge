;; ===============================================================================
;; Issues with the data
;; ===============================================================================

;; Why does this guy have nearly 60 tags? Because it is a giant mash of many notes. See also the little todo data items on livre.

(def mess (m/find-one-as-map "evernotes" {:id 24932}))
(count (:tags mess))
